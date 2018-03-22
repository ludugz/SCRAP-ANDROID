package eplus.scrap.view;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import eplus.scrap.EventBus.LoginEvent;
import eplus.scrap.R;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.SharePreferences;
import eplus.scrap.model.UserInfo;
import eplus.scrap.networking.BaseRestClient;
import eplus.scrap.networking.DataResponse;
import eplus.scrap.networking.Service;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static eplus.scrap.networking.ApiKey.URL_CHECK_MENU_AVAILALBE;
import static eplus.scrap.networking.ApiKey.URL_GET_USER_INFO;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends DialogFragment implements MyWebChromeClient.ProgressListener {
    private static final String ARG_PARAM = "URL" ;
    private static final String TAG = "WEBVIEW";
    private static final String ARG_PARAM_REMOVE_HEADER = "remove_header";
    private static final String LOGOUT_PARAM = "logout";
    private static final int NONE = 0;
    private static final int AVAILABLE = 1;
    protected OnCompleteListener mListener;
    boolean mRemoveHeader;
    private FragmentActivity myContext;
    private String mURL;
    private WebView mWebview;
    private ProgressBar mProgressBar;
    SharedPreferences prefs = null;
    boolean isLogout;
    boolean showiv40;

    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }
    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment newInstance(OnCompleteListener listener, String url,boolean removeheader, boolean logout) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, url);
        args.putBoolean(ARG_PARAM_REMOVE_HEADER,removeheader);
        args.putBoolean(LOGOUT_PARAM,logout);
        fragment.setArguments(args);
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mURL = getArguments().getString(ARG_PARAM);
            mRemoveHeader = getArguments().getBoolean(ARG_PARAM_REMOVE_HEADER);
            isLogout = getArguments().getBoolean(LOGOUT_PARAM);
        }
        setHasOptionsMenu(false);
        myContext = getActivity();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

//        menu.clear();
//        getActivity().getMenuInflater().inflate(R.menu.main_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
        public void onUpdateProgress(int progressValue) {
            mProgressBar.setProgress(progressValue);

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                mListener.onComplete();
                return false;
            }
        });

        prefs = getCurrentContext().getSharedPreferences(getCurrentContext().getPackageName(), MODE_PRIVATE);
        Toolbar toolbar = view.findViewById(R.id.my_toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                int id = item.getItemId();
                switch (id){
                    case R.id.action_copy_link :
                        CommonFunc.setClipboard(getCurrentContext(),mURL);
                        break;
                    case R.id.action_open_brows:
                        try{
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(mURL.trim()));
                            startActivity(i);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }

                        break;
                    case R.id.action_share_via:
                        shareTextUrl();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setTitle(mURL);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
                if(mListener != null){
                    mListener.onComplete();
                }

            }
        });
        mProgressBar = view.findViewById(R.id.progressBar);
        this.mWebview = view.findViewById(R.id.webview);
        WebSettings settings = this.mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        mWebview.addJavascriptInterface(new WebAppInterface(getCurrentContext()),"Android");
        this.mWebview.setFocusable(true);
        String customAgent = this.mWebview.getSettings().getUserAgentString() + ", scrap_app_android";
        this.mWebview.getSettings().setUserAgentString(customAgent);
        Map<String, String> extraHeaders = new HashMap<String, String>();
        if (!SharePreferences.getStringPreference(getCurrentContext(), "token").isEmpty()){
            String token = SharePreferences.getStringPreference(getCurrentContext(), "token");
            extraHeaders.put("user_token",token);
        }
        if(isLogout) {
            String deviceToken = FirebaseInstanceId.getInstance().getToken();
            if(!deviceToken.isEmpty()) {
                extraHeaders.put("device_token",deviceToken);
            }
        }

        this.mWebview.loadUrl(mURL,extraHeaders);
        this.mWebview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebview.setWebChromeClient(new MyWebChromeClient(this));
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView webView, final String url) {

                if (url.startsWith("scrap-app://login-on")) {

                    String[] token = url.split("token=");
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    SharePreferences.saveStringPreference(getCurrentContext(),"token",token[1]);
                    if(!token[1].isEmpty()) Service.sendRegistrationToServer(getCurrentContext(),token[1],deviceToken);
                    if (prefs.getBoolean("firstrun", true)) {
                        if(showiv40) {
                            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                            Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("iv40");
                            if (prev != null) {
                                ft.remove(prev);
                            }
                            ft.addToBackStack(null);
                            EnterInviteCodeIV40Fragment fragment = EnterInviteCodeIV40Fragment.newInstance(new EnterInviteCodeIV40Fragment.OnCompleteListener() {
                                @Override
                                public void onComplete() {
                                    if(mListener != null){
                                        mListener.onComplete();
                                    }
                                    dismiss();
                                }
                            });
                            fragment.show(ft, "iv40");
                        }else {
                            dismiss();
                            if(mListener != null){
                                mListener.onComplete();
                            }

                        }
                    } else {
                        dismiss();
                        //mListener.onComplete();

                    }
                    EventBus.getDefault().post(new LoginEvent(""));
                    return true;
                } else if (url.startsWith("scrap-app://logout")){
                    SharePreferences.saveStringPreference(getCurrentContext(),"token","");
                    dismiss();
                    //mListener.onComplete();
                    EventBus.getDefault().post(new LoginEvent(""));
                    return true;
                }

                return false;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
                //mProgressBar.setProgress(1);
            }



            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onReceivedHttpAuthRequest(WebView view,
                                                  HttpAuthHandler handler, String host, String realm) {
                handler.proceed("is", "74123");
            }


            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
            }

        });

        fetchMenu();
        return view;

    }
    private void fetchUserInfo() {
        String token = SharePreferences.getStringPreference(getCurrentContext(),"token");

        HashMap<String, String> hearder = new HashMap<>();
        if(!token.isEmpty()){
            hearder.put("token", token);
        }



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
                            Gson gson = new Gson();
                            if(jObject.getJSONObject("data") != null) {
                                UserInfo userInfo = gson.fromJson(jObject.getJSONObject("data").toString(), UserInfo.class);
                                if(!userInfo.isAlready_enter_invitation_code()) {
                                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                                    Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("iv40");
                                    if (prev != null) {
                                        ft.remove(prev);
                                    }
                                    ft.addToBackStack(null);
                                    EnterInviteCodeIV40Fragment fragment = EnterInviteCodeIV40Fragment.newInstance(new EnterInviteCodeIV40Fragment.OnCompleteListener() {
                                        @Override
                                        public void onComplete() {
                                            if(mListener != null){
                                                mListener.onComplete();
                                            }
                                            dismiss();
                                        }
                                    });
                                    fragment.show(ft, "iv40");
                                }
                                else {
                                    if(mListener != null){
                                        mListener.onComplete();
                                    }
                                    dismiss();
                                }
                            } else {
                                if(mListener != null){
                                    mListener.onComplete();
                                }
                                dismiss();
                            }



                        }
                    }else{
                        if(mListener != null){
                            mListener.onComplete();
                        }
                        dismiss();
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
    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, mURL);

        startActivity(Intent.createChooser(share, "Share link!"));
    }
    private void fetchMenu() {
        String token = SharePreferences.getStringPreference(getCurrentContext(),"token");

        HashMap<String, String> hearder = new HashMap<>();
        if(!token.isEmpty()){
            hearder.put("token", token);
        }



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
                                int isInviteAvailable = jObject.getJSONObject("data").getInt("status_invite");
                                showiv40 = isInviteAvailable != NONE;
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
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity) context;
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

    }

    public interface OnCompleteListener {
        void onComplete();
    }

    class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            if(toast.equals("logout")) SharePreferences.saveStringPreference(mContext,"token","");
            else
            SharePreferences.saveStringPreference(mContext,"token",toast/*"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjMiLCJ0aW1lIjoxNTAzMzg3NjY4fQ.nzLVxzelhbg-mF5c9KOoMJ2BRQGp--GGEy9345u20X0"*/);
            if(mListener != null){
                mListener.onComplete();
            }
            if(getDialog().getWindow() != null){
                getDialog().getWindow()
                        .getAttributes().windowAnimations = R.style.DialogAnimation;
            }
            dismiss();

        }
    }


}
