package eplus.scrap.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import eplus.scrap.EventBus.LoginEvent;
import eplus.scrap.R;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.SharePreferences;
import eplus.scrap.model.UserInfo;
import eplus.scrap.networking.BaseRestClient;
import eplus.scrap.networking.DataResponse;
import okhttp3.Request;
import okhttp3.Response;

import static eplus.scrap.Constants.SELECTION_COUPON;
import static eplus.scrap.Constants.SELECTION_COUPON_DETAIL;
import static eplus.scrap.networking.ApiKey.URL_ACCESS;
import static eplus.scrap.networking.ApiKey.URL_CHECK_MENU_AVAILALBE;
import static eplus.scrap.networking.ApiKey.URL_FAQ;
import static eplus.scrap.networking.ApiKey.URL_FOOD;
import static eplus.scrap.networking.ApiKey.URL_GETMEMBER_INFO;
import static eplus.scrap.networking.ApiKey.URL_GOODS;
import static eplus.scrap.networking.ApiKey.URL_INQUIRY;
import static eplus.scrap.networking.ApiKey.URL_LOGIN;
import static eplus.scrap.networking.ApiKey.URL_LOGOUT;

//import com.google.firebase.iid.FirebaseInstanceId;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
//    private static final int INVITERONLY = 1;
//    private static final int INVITEEONLY = 2;
//    private static final int BOTH = 3;
    private static final int NONE = 0;
    private static final int AVAILABLE = 1;
    //private static final int UNAVAILABLE = 0;

    // TODO: Rename and change types of parameters
    private int mValue;
    private String mCoupon_type;

    private OnFragmentInteractionListener mListener;
    private LinearLayout cardViewCoupon;
    private LinearLayout cardViewMember;
    private LinearLayout cardViewMemberInfo;
    private LinearLayout lineFAQ;
    private LinearLayout lineAccess;
    private TextView tvChangeMember;
    private TextView tvLogin;
    private FragmentActivity myContext;
    //private UserInfo userInfo;
    private LinearLayout line_share;
    private LinearLayout line_input_invite_code;
    private LinearLayout line_invite;

    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }
    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param value Parameter 1.
     * @param coupon_type Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(int value, String coupon_type) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, value);
        args.putString(ARG_PARAM2, coupon_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserVisibleHint(false);
        if (getArguments() != null) {
            mValue = getArguments().getInt(ARG_PARAM1);
            mCoupon_type = getArguments().getString(ARG_PARAM2);
        }
        myContext = getActivity();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            Log.d("MyTicket:","visible");
            if(getCurrentContext() != null)
            updateView();
            fetchMenu();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        //int height = display.getHeight();  // deprecated
        ImageView img_header = view.findViewById(R.id.my_awesome_toolbar);
        img_header.getLayoutParams().width = width;
        img_header.getLayoutParams().height =  (int) (width * 0.4173);
        if(mValue == SELECTION_COUPON) showCoupon("");
        else if (mValue == SELECTION_COUPON_DETAIL) showCoupon(mCoupon_type);
        initMemberInfo(view);
        initLogin(view);
        intCoupon(view);
        initShare(view);
        initInvite(view);
        initInviteCode(view);
        initAccess(view);
        initFAQ(view);
        initFood(view);
        initGoods(view);
        initInquiry(view);
        fetchMenu();
        return view;
    }

    private void fetchMenu() {
        HashMap<String, String> hearder = new HashMap<>();
         DataResponse responseVeryfy = new DataResponse(getCurrentContext()) {
            @Override
            public void onRealFail() {

            }
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getString("status").equals("200")) {
                        Boolean success = jObject.getBoolean("success");
                        if (success) {
                            if (jObject.getJSONObject("data") != null) {
//                                Log.d("Fetch Menu:",jObject.getJSONObject("data").toString());
                                int isShareAvailable = jObject.getJSONObject("data").getInt("status_share");
                                int isInviteAvailable = jObject.getJSONObject("data").getInt("status_invite");
                                if (isShareAvailable == AVAILABLE) {
                                    line_share.setVisibility(View.VISIBLE);
                                } else {
                                    line_share.setVisibility(View.GONE);
                                }
                                if (isInviteAvailable != NONE) {
                                    line_invite.setVisibility(View.VISIBLE);
                                    line_input_invite_code.setVisibility(View.VISIBLE);
                                } else {
                                    line_input_invite_code.setVisibility(View.GONE);
                                    line_invite.setVisibility(View.GONE);
                                }


                            }
                        }
                    }
                } catch (Exception e) {e.printStackTrace();

                }
            }
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    super.onResponse(call, response);
                } else {
                    super.onFailure(call, new IOException());
                }
            }
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                super.onFailure(call, e);
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        };

        BaseRestClient.get(getActivity(), URL_CHECK_MENU_AVAILALBE, null, hearder, responseVeryfy, false);
    }
    // Hàm này được gọi sau khi event được posted.
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginEvent event) {
        updateView();
    }
    @Override
    public void onStart() {
        super.onStart();
        //Đăng ký lắng nghe
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        //Hủy lắng nghe
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    private void initMemberInfo(View view) {
        tvChangeMember = view.findViewById(R.id.tv_change_member);

        cardViewMemberInfo = view.findViewById(R.id.card_view_member_info);
        cardViewMemberInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty() && CommonFunc.isNetworkConnected(getCurrentContext()))) {
                    loadwebwithurl(URL_GETMEMBER_INFO,false);//"https://scrap.nal.vn/index.php?dispatch=member.logout"
                }
            }
        });
        cardViewMemberInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    cardViewMemberInfo.getBackground().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            cardViewMemberInfo.getBackground().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    cardViewMemberInfo.getBackground().clearColorFilter();
                }
                return false;
            }
        });
    }

    private void initLogin(View view) {
        tvLogin = view.findViewById(R.id.tv_login_out);
        if (SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty()) {
            tvChangeMember.setEnabled(false);
            //tvChangeMember.setTextColor(Color.GRAY);
            cardViewMemberInfo.setVisibility(View.GONE);
            tvLogin.setText(getString(R.string.Login_logout_member_registration));


        } else {
            //tvChangeMember.setEnabled(true);
            tvLogin.setText(getString(R.string.logout));
            cardViewMemberInfo.setVisibility(View.VISIBLE);
        }

        cardViewMember = view.findViewById(R.id.card_view_member);
        cardViewMember.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    cardViewMember.getBackground().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            cardViewMember.getBackground().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    cardViewMember.getBackground().clearColorFilter();
                }
                return false;
            }
        });
        cardViewMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserVisibleHint(false);
                if (SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty() && CommonFunc.isNetworkConnected(getCurrentContext())) {
                    loadwebwithurl(URL_LOGIN + "&callback_url=scrap-app%3A//login-on",false);//"https://scrap.nal.vn/index.php?dispatch=member.logout"

                } else {
                    loadwebwithurl(URL_LOGOUT + "&callback_url=scrap-app%3A//logout",true);
                }
            }
        });
    }

    private void intCoupon(View view) {
        cardViewCoupon = view.findViewById(R.id.card_view_coupon);
        cardViewCoupon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    cardViewCoupon.getBackground().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            cardViewCoupon.getBackground().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    cardViewCoupon.getBackground().clearColorFilter();
                }
                return false;
            }
        });
        cardViewCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCoupon("");

            }
        });
    }

    private void initShare(View view) {
        line_share = view.findViewById(R.id.line_get_coupon);
        line_share.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    line_share.getBackground().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            line_share.getBackground().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    line_share.getBackground().clearColorFilter();
                }
                return false;
            }
        });
        line_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty() && CommonFunc.isNetworkConnected(getCurrentContext())) {
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    WebViewFragment fragment = new WebViewFragment().newInstance(new WebViewFragment.OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            if (!SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty())
                                showShareView();
                        }
                    }, URL_LOGIN + "&callback_url=scrap-app%3A//login-on",true,false);
                    fragment.show(ft, "webview");
                }
                else {
                    showShareView();
                }

            }
        });
    }

    private void initInvite(View view) {
        line_invite = view.findViewById(R.id.line_invite);
        line_invite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    line_invite.getBackground().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            line_invite.getBackground().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    line_invite.getBackground().clearColorFilter();
                }
                return false;
            }
        });
        line_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty() && CommonFunc.isNetworkConnected(getCurrentContext())) {
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    WebViewFragment fragment = new WebViewFragment().newInstance(new WebViewFragment.OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            if (!SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty())
                                showInviteView();
                        }
                    }, URL_LOGIN + "&callback_url=scrap-app%3A//login-on",true,false);
                    fragment.show(ft, "webview");
                }
                else {
                    showInviteView();
                }

            }
        });
    }

    private void initInviteCode(View view) {
        line_input_invite_code = view.findViewById(R.id.line_enter_invite_code);
        line_input_invite_code.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    line_input_invite_code.getBackground().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            line_input_invite_code.getBackground().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    line_input_invite_code.getBackground().clearColorFilter();
                }
                return false;
            }
        });
        line_input_invite_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty() && CommonFunc.isNetworkConnected(getCurrentContext())) {
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    WebViewFragment fragment = new WebViewFragment().newInstance(new WebViewFragment.OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            if (!SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty())
                                showEnterInviteCode();
                        }
                    }, URL_LOGIN + "&callback_url=scrap-app%3A//login-on",true,false);
                    fragment.show(ft, "webview");
                }
                else {
                    showEnterInviteCode();
                }


            }
        });
    }

    private void initAccess(View view) {
        lineAccess = view.findViewById(R.id.line_access);
        lineAccess.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    lineAccess.getBackground().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            lineAccess.getBackground().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    lineAccess.getBackground().clearColorFilter();
                }
                return false;
            }
        });
        lineAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                WebViewNEWSFragment fragment = new WebViewNEWSFragment().newInstance(new WebViewNEWSFragment.OnCompleteListener() {
                    @Override
                    public void onComplete() {

                    }
                }, URL_ACCESS,false);
                fragment.show(ft, "webview");
            }
        });
    }

    private void initFAQ(View view) {
        lineFAQ = view.findViewById(R.id.line_faq);
        lineFAQ.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    lineFAQ.getBackground().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            lineFAQ.getBackground().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    lineFAQ.getBackground().clearColorFilter();
                }
                return false;
            }
        });
        lineFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                WebViewNEWSFragment fragment = new WebViewNEWSFragment().newInstance(new WebViewNEWSFragment.OnCompleteListener() {
                    @Override
                    public void onComplete() {

                    }
                }, URL_FAQ,false);
                fragment.show(ft, "webview");
            }
        });
    }
    private void initFood(View view) {
        final LinearLayout line = view.findViewById(R.id.line_food);
        line.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    line.getBackground().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            line.getBackground().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    line.getBackground().clearColorFilter();
                }
                return false;
            }
        });
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                WebViewNEWSFragment fragment = new WebViewNEWSFragment().newInstance(new WebViewNEWSFragment.OnCompleteListener() {
                    @Override
                    public void onComplete() {

                    }
                }, URL_FOOD,false);
                fragment.show(ft, "webview");
            }
        });
    }
    private void initGoods(View view) {
        final LinearLayout line = view.findViewById(R.id.line_goods);
        line.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    line.getBackground().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            line.getBackground().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    line.getBackground().clearColorFilter();
                }
                return false;
            }
        });
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                WebViewNEWSFragment fragment = new WebViewNEWSFragment().newInstance(new WebViewNEWSFragment.OnCompleteListener() {
                    @Override
                    public void onComplete() {

                    }
                }, URL_GOODS,false);
                fragment.show(ft, "webview");
            }
        });
    }

    private void initInquiry(View view) {
        final LinearLayout line = view.findViewById(R.id.line_inquiry);
        line.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    line.getBackground().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            line.getBackground().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    line.getBackground().clearColorFilter();
                }
                return false;
            }
        });
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                WebViewNEWSFragment fragment = new WebViewNEWSFragment().newInstance(new WebViewNEWSFragment.OnCompleteListener() {
                    @Override
                    public void onComplete() {

                    }
                }, URL_INQUIRY,false);
                fragment.show(ft, "webview");
            }
        });
    }

    private void showInviteView() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ShareOnSNSIV20Fragment fragment = new ShareOnSNSIV20Fragment().newInstance("","");
        ft.add(R.id.fragment_menu, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack("INVITE");
        ft.commit();
    }
    private void showShareView() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ShareOnSNSFragment fragment = new ShareOnSNSFragment().newInstance("","");
        ft.add(R.id.fragment_menu, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack("SHARE");
        ft.commit();
    }
    private void showEnterInviteCode() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        EnterInviteCodeFragment fragment = new EnterInviteCodeFragment().newInstance("","");
        ft.add(R.id.fragment_menu, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack("INVITE");
        ft.commit();
    }
    private void loadwebwithurl(String url,boolean islogout) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        WebViewFragment fragment = new WebViewFragment().newInstance(new WebViewFragment.OnCompleteListener() {
            @Override
            public void onComplete() {
                updateView();


            }
        }, url,true,islogout);
        fragment.show(ft, "webview");
    }
    private void updateView(){
        if (SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty()) {
            tvChangeMember.setEnabled(false);
            //tvChangeMember.setTextColor(Color.GRAY);
            cardViewMemberInfo.setVisibility(View.GONE);
            tvLogin.setText(getString(R.string.Login_logout_member_registration));


        } else {
            //tvChangeMember.setEnabled(true);
            tvLogin.setText(getString(R.string.logout));
            cardViewMemberInfo.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }
        fetchMenu();
        if (SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty() && CommonFunc.isNetworkConnected(getCurrentContext())) {
            tvChangeMember.setEnabled(false);
            tvLogin.setText(getString(R.string.Login_logout_member_registration));


        } else {
            tvChangeMember.setEnabled(true);
            tvLogin.setText(getString(R.string.logout));
        }

    }
    private void showCoupon(String coupon_type) {

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        CouponFragment fragment =  CouponFragment.newInstance(coupon_type,"");
        ft.add(R.id.fragment_menu, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack("SHARE");
        ft.commit();
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        myContext = (FragmentActivity) context;
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
