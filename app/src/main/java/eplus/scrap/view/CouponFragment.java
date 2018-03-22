package eplus.scrap.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import eplus.scrap.EventBus.LoginEvent;
import eplus.scrap.R;
import eplus.scrap.adapter.CouponAdapter;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.SharePreferences;
import eplus.scrap.model.Coupon;
import eplus.scrap.model.Pagination;
import eplus.scrap.model.UserInfo;
import eplus.scrap.networking.BaseRestClient;
import eplus.scrap.networking.DataResponse;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static eplus.scrap.networking.ApiKey.GET_COUPON;
import static eplus.scrap.networking.ApiKey.URL_GET_USER_INFO;
import static eplus.scrap.networking.ApiKey.URL_LOGIN;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CouponFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CouponFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CouponFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static boolean m_iAmVisible;
    WebViewFragment fragment;
    // TODO: Rename and change types of parameters
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private CouponAdapter adapter;
    private ArrayList<Coupon> listCoupon;
    private String localTAG = "CouponFragment";
    private Pagination pagination;
    private FragmentActivity myContext;
    private LinearLayout lineErrMsg;
    private boolean flag_loading;
    private RelativeLayout reInviteCoupon;
    private UserInfo userInfo;
    private ImageButton ic_left_bt;
    private RelativeLayout reNointernet;
    private LinearLayout lineBtRetry;
    private boolean itemOnClick;
    private LinearLayout lgview;
    private Button btlgin;
    private LinearLayout line_lging;
    private String mCoupon_type;
    private SwipeRefreshLayout swipeContainer;
    public CouponFragment() {
        // Required empty public constructor
    }
    public static CouponFragment newInstance(String coupon_type, String param2) {
        CouponFragment fragment = new CouponFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, coupon_type);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCoupon_type = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void showEnterInviteCode() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        EnterInviteCodeFragment fragment = new EnterInviteCodeFragment().newInstance("","");
        ft.add(R.id.fragment_coupon, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack("INVITE");
        ft.commit();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginEvent event) {
        if (!SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty()) {
            lgview.setVisibility(View.GONE);
            if(CommonFunc.isNetworkConnected(getCurrentContext())) {

               loadCoupon();
            }
        }else {
            lgview.setVisibility(View.VISIBLE);
            swipeContainer.setVisibility(View.GONE);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        //Đăng ký lắng nghe
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        //Hủy lắng ngh]\
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coupon, container, false);
        ImageView imgBG = view.findViewById(R.id.img_bg);
        imgBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        reNointernet = view.findViewById(R.id.re_no_internet_layout);
        reNointernet.setVisibility(View.GONE);
        lineBtRetry = view.findViewById(R.id.line_bt_retry);
        lineBtRetry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    lineBtRetry.getBackground().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            lineBtRetry.getBackground().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    if(CommonFunc.isNetworkConnected(getCurrentContext())) {
                        lineBtRetry.getBackground().clearColorFilter();
                        loadCoupon();
                        reNointernet.setVisibility(View.GONE);
                    }
                }
                return true;
            }
        });

        ic_left_bt = view.findViewById(R.id.ic_left_bt);
        ic_left_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        reInviteCoupon = view.findViewById(R.id.re_invite_coupon);
        reInviteCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterInviteCode();
            }
        });
        lineErrMsg = view.findViewById(R.id.line_nodata);
        listView = view.findViewById(R.id.list_coupon);
        LinearLayout viewHeader = new LinearLayout(getCurrentContext());
        viewHeader.setOrientation(LinearLayout.HORIZONTAL);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 400);
        viewHeader.setLayoutParams(lp);

        listView.addFooterView(viewHeader, null, false);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(flag_loading == true)
                    {
                        getCoupon_more();
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!itemOnClick) {
                    itemOnClick = true;
                    Coupon coupon = listCoupon.get(position);
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog_coupon_detail");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    CouponDetailFragment fragment = new CouponDetailFragment().newInstance(new CouponDetailFragment.OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            itemOnClick = false;
                        }
                    }, coupon);
                    fragment.show(ft, "dialog_ticket_detail");
                }


            }
        });
        swipeContainer = view.findViewById(R.id.swiperefresh);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
                if(CommonFunc.isNetworkConnected(getActivity())) {
                    getCoupon(false);
                    reNointernet.setVisibility(View.GONE);
                } else {
                    reNointernet.setVisibility(View.VISIBLE);
                }
            }
        });
        if(CommonFunc.isNetworkConnected(getCurrentContext())) {
            loadCoupon();
        } else {
            reNointernet.setVisibility(View.VISIBLE);
        }
        reNointernet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        initlogin(view);
        if (!SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty()) {
            lgview.setVisibility(View.GONE);
            if(!mCoupon_type.isEmpty())
            getCouponDetail(mCoupon_type);
        }
        return view;
    }
    private void showCouponDetail(Coupon coupon) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog_coupon_detail");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        CouponDetailFragment fragment = new CouponDetailFragment().newInstance(new CouponDetailFragment.OnCompleteListener() {
            @Override
            public void onComplete() {
                itemOnClick = false;
            }
        }, coupon);
        fragment.show(ft, "dialog_ticket_detail");
    }
    private void initlogin(View view) {
        line_lging = view.findViewById(R.id.line_login);
        lgview = view.findViewById(R.id.re_login);
        btlgin = view.findViewById(R.id.bt_login);
        btlgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line_lging.setVisibility(View.GONE);
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                Fragment prev = getChildFragmentManager().findFragmentByTag("webview");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                WebViewFragment fragment = new WebViewFragment().newInstance(new WebViewFragment.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        if (!SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty()) {
                            loadCoupon();
                            if(!mCoupon_type.isEmpty())
                                getCouponDetail(mCoupon_type);
                            lgview.setVisibility(View.GONE);

                        } else {
                            lgview.setVisibility(View.VISIBLE);
                            line_lging.setVisibility(View.VISIBLE);
                        }
                    }
                }, URL_LOGIN + "&callback_url=scrap-app%3A//login-on",true,false);
                fragment.show(ft, "webview");
            }
        });

    }

    private void loadCoupon() {
        if (!SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty()) {
            getCoupon(true);
            fetchUserInfo();
        }
    }
    private void getCouponDetail(String id) {
        String token = SharePreferences.getStringPreference(getCurrentContext(),"token");

        HashMap<String, String> hearder = new HashMap<>();
        if(!token.isEmpty()){
            hearder.put("token", token);
        }

        DataResponse responseVeryfy = new DataResponse(getCurrentContext()) {
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getString("status").equals("200")) {
                        Boolean success = jObject.getBoolean("success");
                        if (success) {
                            Gson gson = new Gson();
                            if(jObject.getJSONObject("data") != null) {
                                Coupon coupon = gson.fromJson(jObject.getJSONObject("data").toString(), Coupon.class);
                                showCouponDetail(coupon);
                                mCoupon_type = null;
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

        BaseRestClient.get(getActivity(), GET_COUPON + "/"+ id, null, hearder, responseVeryfy, false);

    }
    private void fetchUserInfo() {
        String token = SharePreferences.getStringPreference(getCurrentContext(),"token");

        HashMap<String, String> hearder = new HashMap<>();
        if(!token.isEmpty()){
            hearder.put("token", token);
        }



        DataResponse responseVeryfy = new DataResponse(getCurrentContext()) {
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getString("status").equals("200")) {
                        Boolean success = jObject.getBoolean("success");
                        if (success) {
                            Gson gson = new Gson();
                            if(jObject.getJSONObject("data") != null) {
                                userInfo = gson.fromJson(jObject.getJSONObject("data").toString(), UserInfo.class);
                                //if(!userInfo.isAlready_enter_invitation_code()) reInviteCoupon.setVisibility(View.VISIBLE);
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

        BaseRestClient.get(getActivity(), URL_GET_USER_INFO, null, hearder, responseVeryfy, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        m_iAmVisible = isVisibleToUser;
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }
    }
    private void getCoupon_more() {
        String token = SharePreferences.getStringPreference(getCurrentContext(),"token");

        HashMap<String, String> hearder = new HashMap<>();
        if(!token.isEmpty()){
            hearder.put("token", token);
        }
        HashMap<String, String> params = new HashMap<>();
        //params.put("filter_type", "1");
        flag_loading = false;
        params.put("items_per_page","10");
        int page = pagination.getPage() + 1;
        params.put("page",""+page);


        DataResponse responseVeryfy = new DataResponse(getCurrentContext()) {
            @Override
            public void onRealFail() {
                reNointernet.setVisibility(View.VISIBLE);
            }
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getString("status").equals("200")) {
                        Boolean success = jObject.getBoolean("success");
                        if (success) {
                            Gson gson = new Gson();
                            JSONObject pagine = jObject.getJSONObject("params");
                            pagination = gson.fromJson(pagine.toString(), Pagination.class);
                            JSONArray arrayEvent =  jObject.getJSONArray("data");
                            for(int i=0; i<arrayEvent.length(); i++){
                                JSONObject json_data = arrayEvent.getJSONObject(i);
                                Coupon coupon = gson.fromJson(json_data.toString(), Coupon.class);
                                listCoupon.add(coupon);

                            }

                            if(listCoupon.size() > 0) {
                                flag_loading = true;
                                adapter.notifyDataSetChanged();
                            } else {
                                flag_loading = false;
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

        BaseRestClient.get(getActivity(), GET_COUPON, params, hearder, responseVeryfy, false);
    }

    private void getCoupon(boolean loading) {
        String token = SharePreferences.getStringPreference(getCurrentContext(),"token");

        HashMap<String, String> hearder = new HashMap<>();
        if(!token.isEmpty()){
            hearder.put("token", token);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("items_per_page","10");


        DataResponse responseVeryfy = new DataResponse(getCurrentContext()) {
            @Override
            public void onRealFail() {
                reNointernet.setVisibility(View.VISIBLE);
            }
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getString("status").equals("200")) {
                        Boolean success = jObject.getBoolean("success");
                        if (success) {
                            Gson gson = new Gson();
                            JSONObject pagine = jObject.getJSONObject("params");
                            pagination = gson.fromJson(pagine.toString(), Pagination.class);
                            JSONArray arrayEvent =  jObject.getJSONArray("data");
                            listCoupon = new ArrayList<>();
                                for(int i=0; i<arrayEvent.length(); i++){
                                    JSONObject json_data = arrayEvent.getJSONObject(i);

                                    Coupon coupon = gson.fromJson(json_data.toString(), Coupon.class);
                                    listCoupon.add(coupon);

                                }
                            flag_loading = true;
                            if(listCoupon.size() > 0) {

                                adapter = new CouponAdapter(listCoupon, getCurrentContext());
                                listView.setAdapter(adapter);

                                lineErrMsg.setVisibility(View.GONE);
                            } else {
                                lineErrMsg.setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        lineErrMsg.setVisibility(View.VISIBLE);
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

        BaseRestClient.get(getCurrentContext(), GET_COUPON, params, hearder, responseVeryfy, loading);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext=(FragmentActivity) context;
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }
}
