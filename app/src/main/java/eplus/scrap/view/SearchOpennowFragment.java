package eplus.scrap.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import eplus.scrap.R;
import eplus.scrap.adapter.NewSearchAdapter;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.SimpleHeaderDecoration;
import eplus.scrap.model.Pagination;
import eplus.scrap.model.TourDetail;
import eplus.scrap.networking.BaseRestClient;
import eplus.scrap.networking.DataResponse;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static eplus.scrap.networking.ApiKey.GET_TOUR_DETAIL;
import static eplus.scrap.networking.ApiKey.ITEM_PER_PAGE;
import static eplus.scrap.networking.ApiKey.SEARCH_TOUR_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchOpennowFragment extends Fragment {

    private static final String ARG_PARAM_KEYWORD = "keyword";
    private static final String ARG_PARAM_FROMDATE = "fromdate";
    private static final String ARG_PARAM_TODATE = "todate";
    private String mKeyword;
    private long mFromDate;
    private long mToDate;
    ArrayList<TourDetail> eventArrayLists;
    RecyclerView listView;
    private static NewSearchAdapter adapter;
    private ProgressBar progressBar;
    private boolean isLoadMore;
    private SwipeRefreshLayout swipeContainer;
    private Pagination pagination;
    protected OnCompleteListener mListener;
    private LinearLayout lineErrMsg;
    private RecyclerView.LayoutManager layoutManager;

    public interface OnCompleteListener {
        void onShowTour(TourDetail tourDetail);
        void onNointernet();
    }
    public SearchOpennowFragment() {
        // Required empty public constructor
    }
    public static SearchOpennowFragment newInstance(OnCompleteListener listener,String keyword, long fromdate, long todate) {
        SearchOpennowFragment fragment = new SearchOpennowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_KEYWORD, keyword);
        args.putLong(ARG_PARAM_FROMDATE,fromdate);
        args.putLong(ARG_PARAM_TODATE,todate);
        fragment.setArguments(args);
        fragment.mListener = listener;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mKeyword = getArguments().getString(ARG_PARAM_KEYWORD);
            mFromDate = getArguments().getLong(ARG_PARAM_FROMDATE);
            mToDate = getArguments().getLong(ARG_PARAM_TODATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_opennow, container, false);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        lineErrMsg = view.findViewById(R.id.line_nodata);
        listView = view.findViewById(R.id.listViewEvent);

        int numberOfColumns = 3;
        listView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        LinearLayout viewHeader = new LinearLayout(getActivity());
        viewHeader.setOrientation(LinearLayout.HORIZONTAL);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 200);
        viewHeader.setLayoutParams(lp);
        SimpleHeaderDecoration simpleHeaderDecoration = new SimpleHeaderDecoration(0,400,16, SimpleHeaderDecoration.HORIZONTAL);
        listView.addItemDecoration(simpleHeaderDecoration);

        swipeContainer = view.findViewById(R.id.swiperefresh);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                searchtours(null,"",CommonFunc.getDate(mFromDate,"yyyy/MM/dd"),CommonFunc.getDate(mToDate,"yyyy/MM/dd"),mKeyword,"open_now",false);
            }
        });
        if(CommonFunc.isNetworkConnected(getActivity())) {


        if(eventArrayLists != null && eventArrayLists.size() >0 ){
            //eventArrayLists = new ArrayList<>();
            updateData();
        }
        else
            searchtours(null,"",CommonFunc.getDate(mFromDate,"yyyy/MM/dd"),CommonFunc.getDate(mToDate,"yyyy/MM/dd"),mKeyword,"open_now",true);
        } else {
            mListener.onNointernet();
        }
        return view;
    }
    private void updateData() {
        adapter= new NewSearchAdapter(eventArrayLists, getActivity(), new NewSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TourDetail item) {
                showTourDetail(item);
            }
        });
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnBottomReachedListener(new NewSearchAdapter.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                if (pagination != null) {
                        if (pagination.getPage()  * 10 <= pagination.getTotal_items() && isLoadMore) {
                            searchtourmore("", "", CommonFunc.getDate(mFromDate, "yyyy/MM/dd"), CommonFunc.getDate(mToDate, "yyyy/MM/dd"), mKeyword, "open_now",false);
                        } else  isLoadMore = false;
                    }
            }


        });



    }
    private void searchtours(String lang_code, String specify_date, String date_from, String date_to, String key_word, String show_type, boolean loading ) {
        HashMap<String, String> param = new HashMap<>();

        if(!specify_date.isEmpty()){
            param.put("specify_date", specify_date);
        }
        if(!date_from.isEmpty()){
            param.put("date_from", date_from);
        }
        if(!date_to.isEmpty()){
            param.put("date_to", date_to);
        }
        if(!key_word.isEmpty()){
            param.put("keyword", key_word );
        }
        if(!show_type.isEmpty()){
            param.put("show_type",show_type);
        }
        param.put("page", "1" );
        param.put("item_per_page",ITEM_PER_PAGE);




        DataResponse responseVeryfy = new DataResponse(getContext()) {
            @Override
            public void onRealFail() {
                mListener.onNointernet();
            }
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getString("status").equals("200")) {
                        Gson gson = new Gson();
                        Boolean success = jObject.getBoolean("success");
                        if (success) {
                            eventArrayLists = new ArrayList<>();
                            JSONObject pagine = jObject.getJSONObject("params");
                            pagination = gson.fromJson(pagine.toString(), Pagination.class);
                            JSONArray arrayEvent =  jObject.getJSONArray("data");
                            for(int i=0; i<arrayEvent.length(); i++){
                                JSONObject json_data = arrayEvent.getJSONObject(i);

                                TourDetail event = gson.fromJson(json_data.toString(), TourDetail.class);
                                eventArrayLists.add(event);
                            }

                            if(eventArrayLists.isEmpty()) {
                                lineErrMsg.setVisibility(View.VISIBLE);
                                isLoadMore = false;
                            }
                            else {
                                lineErrMsg.setVisibility(View.GONE);
                                updateData();
                                isLoadMore = true;

                            }
                            swipeContainer.setRefreshing(false);
                        }
                    }else{
                        swipeContainer.setRefreshing(false);
                        lineErrMsg.setVisibility(View.VISIBLE);
                        isLoadMore = false;
                    }
                } catch (Exception e) {e.printStackTrace();

                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    super.onResponse(call, response);
                } else {
                    super.onFailure(call, new IOException());
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        };
        isLoadMore = false;
         BaseRestClient.get(getActivity(), SEARCH_TOUR_URL, param, null, responseVeryfy, loading);
    }
    private void getTourDetail(String productId) {
        HashMap<String, String> param = new HashMap<>();

        DataResponse responseVeryfy = new DataResponse(getContext()) {
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getString("status").equals("200")) {
                        Boolean success = jObject.getBoolean("success");
                        if (success) {
                            JSONObject data =  jObject.getJSONObject("data");
                            Gson gson = new Gson();
                            TourDetail tourdetail = gson.fromJson(data.toString(), TourDetail.class);
                            showTourDetail(tourdetail);

                        }
                    }
                } catch (Exception e) {e.printStackTrace();

                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    super.onResponse(call, response);
                } else {
                    super.onFailure(call, new IOException());
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        };

        BaseRestClient.get(getActivity(), GET_TOUR_DETAIL + productId, param, null, responseVeryfy, true);

    }
    private void showTourDetail(TourDetail tourDetail) {
        mListener.onShowTour(tourDetail);
//        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//        TourDetailFragment fragment = new TourDetailFragment().newInstance(tourDetail);
//        ft.replace(R.id.fragment_search_opennow, fragment);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        ft.addToBackStack("Tour_Detail");
//        ft.commit();
    }
    private void searchtourmore(String lang_code, String specify_date, String date_from, String date_to, String key_word, String show_type, boolean loadMore ) {
        HashMap<String, String> param = new HashMap<>();

        if(!specify_date.isEmpty()){
            param.put("specify_date", specify_date);
        }
        if(!date_from.isEmpty()){
            param.put("date_from", date_from);
        }
        if(!date_to.isEmpty()){
            param.put("date_to", date_to);
        }
        if(!key_word.isEmpty()){
            param.put("keyword", key_word );
        }
        if(!show_type.isEmpty()){
            param.put("show_type",show_type);
        }
            int page = pagination.getPage() + 1;
            param.put("page", "" + page);

        param.put("item_per_page",ITEM_PER_PAGE);

        DataResponse responseVeryfy = new DataResponse(getContext()) {
            @Override
            public void onRealFail() {
                swipeContainer.setRefreshing(false);
                mListener.onNointernet();
            }
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
                try {

                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getString("status").equals("200")) {
                        Gson gson = new Gson();
                        Boolean success = jObject.getBoolean("success");
                        if (success) {
                            JSONObject pagine = jObject.getJSONObject("params");
                            pagination = gson.fromJson(pagine.toString(), Pagination.class);
                            JSONArray arrayEvent =  jObject.getJSONArray("data");
                            ArrayList<TourDetail> arrayList = new ArrayList<>();
                            for(int i=0; i<arrayEvent.length(); i++){
                                JSONObject json_data = arrayEvent.getJSONObject(i);

                                TourDetail event = gson.fromJson(json_data.toString(), TourDetail.class);
                                arrayList.add(event);
                            }
                            CommonFunc.hideLoadingView();
                            if(arrayList.isEmpty()) {
                                //lineErrMsg.setVisibility(View.VISIBLE);
                                isLoadMore = false;
                            }
                            else {
                                lineErrMsg.setVisibility(View.GONE);
                                isLoadMore = true;
                                eventArrayLists.addAll(arrayList);
                                //adapter.ad(arrayList);
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }
                } catch (Exception e) {e.printStackTrace();

                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    super.onResponse(call, response);
                } else {
                    super.onFailure(call, new IOException());
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        };
        isLoadMore = false;
        BaseRestClient.get(getActivity(), SEARCH_TOUR_URL, param, null, responseVeryfy, false);
    }
}
