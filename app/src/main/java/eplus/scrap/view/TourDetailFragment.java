package eplus.scrap.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import eplus.scrap.EventBus.GetTourDetailEvent;
import eplus.scrap.R;
import eplus.scrap.adapter.TourDetailPagerAdapter;
import eplus.scrap.common.SharePreferences;
import eplus.scrap.model.TourDetail;
import eplus.scrap.networking.BaseRestClient;
import eplus.scrap.networking.DataResponse;
import eplus.scrap.view.tourdetail.BoxOfficeFragment;
import eplus.scrap.view.tourdetail.PriceDetailFragment;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static eplus.scrap.networking.ApiKey.GET_TOUR_DETAIL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TourDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TourDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourDetailFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TOUR_PARAM = "tourdetail";
    private static final String EVENT_ID_PARAM = "event_id";
    private static final String DATE_SELECT = "date_select";
    // TODO: Rename and change types of parameters
    private TourDetail mTourDetail;
    private String mParam2;
    FragmentActivity myContext;
    TourDetailPagerAdapter adapterViewPager;
    private OnFragmentInteractionListener mListener;
    //private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager vpPager;
    private int width;
    private int height;
    private String mEvent_id;
    private TextView toolbar_title;
    private List<Fragment> fragments;
    BoxOfficeFragment mBoxOfficeFragment;
    private String mDateSelect;
    public TourDetailFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TourDetailFragment newInstance(TourDetail tourDetail,String event_id,String date_select) {
        TourDetailFragment fragment = new TourDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(TOUR_PARAM, tourDetail);
        args.putString(EVENT_ID_PARAM,event_id);
        args.putString(DATE_SELECT,date_select);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTourDetail = getArguments().getParcelable(TOUR_PARAM);
            mEvent_id = getArguments().getString(EVENT_ID_PARAM);
            mDateSelect = getArguments().getString(DATE_SELECT);
        }
        setUserVisibleHint(false);
        if (getArguments() != null) {
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mTourDetail != null) {
            getTourDetail(String.valueOf(mTourDetail.getProduct_id()));
            toolbar_title.setText(mTourDetail.getProduct_name());
        }else if (!mEvent_id.isEmpty()) {
            getTourDetail(mEvent_id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated

        ImageView icBack = view.findViewById(R.id.ic_back);
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        RelativeLayout re_Header = view.findViewById(R.id.re_product_detail_header);
        re_Header.getLayoutParams().height = (int) (width * 0.4);


        int betweenSpace = (int) (width * 0.022);
        int heightTabbar = (int) (width * 0.173);
        tabLayout = view.findViewById(R.id.tabLayout_tourdetail);
        tabLayout.getLayoutParams().height = heightTabbar;
        ViewGroup.MarginLayoutParams params_tabbar = (ViewGroup.MarginLayoutParams) tabLayout.getLayoutParams();
        params_tabbar.leftMargin = params_tabbar.rightMargin = betweenSpace;
        params_tabbar.bottomMargin = (int) (width * 0.0666);
        tabLayout.addTab(tabLayout.newTab().setText("概要"));
        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
        //Log.d("Width/height:","" + width +"/" + height);
        for (int i=0; i<slidingTabStrip.getChildCount(); i++) {
            View v = slidingTabStrip.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();

            params.width = params.height = heightTabbar;
            params.rightMargin = betweenSpace;

        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        vpPager = view.findViewById(R.id.viewpager_detail);
        fragments = new Vector<>();
        mBoxOfficeFragment = BoxOfficeFragment.newInstance(mTourDetail,mDateSelect);
        fragments.add(mBoxOfficeFragment);
        adapterViewPager = new TourDetailPagerAdapter(getChildFragmentManager(),fragments,mTourDetail);
        ViewGroup.MarginLayoutParams params_vpPager = (ViewGroup.MarginLayoutParams) vpPager.getLayoutParams();
        params_vpPager.topMargin = (int) (width * 0.3466);

        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        vpPager.setOffscreenPageLimit(1);
        vpPager.setCurrentItem(0);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        re_Header.bringToFront();
        if(mTourDetail != null) {
            toolbar_title.setText(mTourDetail.getProduct_name());
        }
        return view;
    }

    private void fetchData (TourDetail tourDetail) {
        mTourDetail = tourDetail;
        EventBus.getDefault().post(new GetTourDetailEvent(tourDetail));
        int index = 0;
        for(TourDetail.TabsBean tabsBean : tourDetail.getTabs()) {
            tabLayout.addTab(tabLayout.newTab().setText(tabsBean.getName()));
            PriceDetailFragment fragment = PriceDetailFragment.newInstance(mTourDetail.getTabs().get(index ),index + 1);
            fragments.add(fragment);
            index ++;

        }
        toolbar_title.setText(tourDetail.getProduct_name());
        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
        int betweenSpace = (int) (width * 0.022);
        int heightTabbar = (int) (width * 0.173);
        //Log.d("Width/height:","" + width +"/" + height);
        for (int i=0; i<slidingTabStrip.getChildCount(); i++) {
            View v = slidingTabStrip.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.width = params.height = heightTabbar;
            params.rightMargin = betweenSpace;


        }

        adapterViewPager.notifyDataSetChanged();
        //vpPager.setOffscreenPageLimit(slidingTabStrip.getChildCount());

    }
    private void getTourDetail(String productId) {
        HashMap<String, String> param = new HashMap<>();
        String token = SharePreferences.getStringPreference(myContext, "token");

        HashMap<String, String> hearder = new HashMap<>();
        if (!token.isEmpty()) {
            hearder.put("token", token);
        }

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
                            fetchData(tourdetail);

                        }
                    }else{
                        Toast.makeText(myContext,getString(R.string.not_found_data),
                                Toast.LENGTH_SHORT).show();
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

        BaseRestClient.get(getActivity(), GET_TOUR_DETAIL + productId, param, hearder, responseVeryfy, true);

    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
         myContext = (FragmentActivity) context;
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
