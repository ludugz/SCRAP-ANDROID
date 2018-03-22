package eplus.scrap.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import eplus.scrap.R;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.SharePreferences;
import eplus.scrap.model.UserInfo;
import eplus.scrap.networking.BaseRestClient;
import eplus.scrap.networking.DataResponse;
import okhttp3.Request;
import okhttp3.Response;

import static eplus.scrap.networking.ApiKey.URL_GET_COUPON_ON_SHARE_SNS;
import static eplus.scrap.networking.ApiKey.URL_GET_USER_INFO;
import static eplus.scrap.networking.ApiKey.URL_LOGIN;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShareOnSNSFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShareOnSNSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShareOnSNSFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String PERMISSION = "publish_actions";


    private final String PENDING_ACTION_BUNDLE_KEY =
            "eplus.scrap:PendingAction";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int OUTOFTIME = 0;
    private static final int USED = -1;
    private static final int RECEIVEDCOUPON = 2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btShare;

    private OnFragmentInteractionListener mListener;
    private LinearLayout line_success;
    private LinearLayout line_err;
    private LinearLayout line_out_of_time;
    private UserInfo userInfo;
    private RelativeLayout reNointernet;
    private LinearLayout lineBtRetry;
    private int width;
    private int height;
    private LinearLayout line_top;
    private ViewGroup.MarginLayoutParams line_top_params;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private Button shareButton;
    private boolean isShareFB;
    private FragmentActivity myContext;
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
//            Log.d("HelloFacebook", "Canceled");
            //isShareFB = false;
        }

        @Override
        public void onError(FacebookException error) {
//            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));

        }

        @Override
        public void onSuccess(Sharer.Result result) {
//            Log.d("HelloFacebook", "Success!");
            getCouponOnShare();


        }


    };




    public ShareOnSNSFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShareOnSNSFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShareOnSNSFragment newInstance(String param1, String param2) {
        ShareOnSNSFragment fragment = new ShareOnSNSFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        myContext = getActivity();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                if(AccessToken.getCurrentAccessToken() == null){
                    System.out.println("not logged in yet");
                } else {
                    System.out.println("Logged in");
                }
            }
        });
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(userInfo.getUrl_download_app()))
                        .build();
                shareDialog.show(linkContent, ShareDialog.Mode.FEED);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, shareCallback);

    }
    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_on_sn, container, false);
        Toolbar toolbar = view.findViewById(R.id.my_awesome_toolbar);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated
        line_top = view.findViewById(R.id.line_top);
        line_top_params = (ViewGroup.MarginLayoutParams) line_top.getLayoutParams();
        final  int padding = (int) (width * 0.15);
        line_top_params.bottomMargin = padding;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
        line_success = view.findViewById(R.id.line_msg_success);
        line_success.setVisibility(View.GONE);
        line_err = view.findViewById(R.id.line_msg_err);
        line_err.setVisibility(View.GONE);
        line_out_of_time = view.findViewById(R.id.line_outoftime_msg);
        line_out_of_time.setVisibility(View.GONE);

        btShare = view.findViewById(R.id.bt_share);
        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userInfo != null) {
                    isShareFB = false;
                    line_top_params.bottomMargin = padding;
                    hiddenmsg();

                    String sAux = "\n" + userInfo.getTitle_share();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivityForResult(generateCustomChooserIntentWithoutFacebook(intent), 10);
                }
                else  {
                    Toast.makeText(getCurrentContext(), R.string.can_not_get_user_info,
                            Toast.LENGTH_SHORT).show();
                }

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
                    lineBtRetry.getBackground().clearColorFilter();
                    if(CommonFunc.isNetworkConnected(getActivity())) {
                        fetchUserInfo();
                        reNointernet.setVisibility(View.GONE);
                    }
                }
                return true;
            }
        });

        // Can we present the share dialog for photos?

        shareButton = view.findViewById(R.id.bt_share_fb);



        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ShareDialog.canShow(ShareLinkContent.class) && userInfo != null) {
                    isShareFB = true;
                    hiddenmsg();
                    Profile profile = Profile.getCurrentProfile();
                    AccessToken accessToken = AccessToken.getCurrentAccessToken();

                    if (profile != null && accessToken != null && accessToken.getPermissions().contains("publish_actions")) {

                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse(userInfo.getUrl_download_app()))
                                .build();
                        shareDialog.show(linkContent, ShareDialog.Mode.FEED);
                    } else {
                        List<String> permissions = Arrays.asList("publish_actions");
                        LoginManager.getInstance().logInWithPublishPermissions(ShareOnSNSFragment.this, permissions);
                    }

                }
            }
        });

        fetchUserInfo();
        return view;

    }

    private void hiddenmsg() {
        line_success.setVisibility(View.GONE);
        line_err.setVisibility(View.GONE);
        line_out_of_time.setVisibility(View.GONE);
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
                            if(jObject.getJSONObject("data") != null)
                                userInfo  = gson.fromJson(jObject.getJSONObject("data").toString(), UserInfo.class);




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

        BaseRestClient.get(getActivity(), URL_GET_USER_INFO, null, hearder, responseVeryfy, false);
    }
    private void getCouponOnShare() {
        String token = SharePreferences.getStringPreference(getCurrentContext(),"token");

        HashMap<String, String> hearder = new HashMap<>();
        if(!token.isEmpty()){
            hearder.put("token", token);
        }



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
                        if(jObject.getJSONObject("data") != null){
                            int status = jObject.getJSONObject("data").getInt("status");
                            switch (status){

                                case OUTOFTIME:
                                    line_out_of_time.setVisibility(View.VISIBLE);
                                    break;
                                case RECEIVEDCOUPON:
                                    line_success.setVisibility(View.VISIBLE);
                                    break;
                                case USED:
                                    line_err.setVisibility(View.VISIBLE);
                                    break;
                                default:
                                    break;
                            }
                        }

                    }

                    int space = (int) (width * 0.04);
                    line_top_params.bottomMargin = space;
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

        BaseRestClient.get(getActivity(), URL_GET_COUPON_ON_SHARE_SNS, null, hearder, responseVeryfy, true);

    }

    private void loadLogin() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        WebViewFragment fragment = WebViewFragment.newInstance(new WebViewFragment.OnCompleteListener() {
            @Override
            public void onComplete() {
            }
        }, URL_LOGIN,false,false);
        fragment.show(ft, "webview");
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(!isShareFB) {
            getCouponOnShare();
        }

    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    private Intent generateCustomChooserIntentWithoutFacebook(Intent prototype) {
        List<Intent> targetedShareIntents = new ArrayList<>();
        List<HashMap<String, String>> intentMetaInfo = new ArrayList<>();
        Intent chooserIntent;

        Intent dummy = new Intent(prototype.getAction());
        dummy.setType(prototype.getType());
        List<ResolveInfo> resInfo = getCurrentContext().getPackageManager().queryIntentActivities(dummy, 0);

        if (!resInfo.isEmpty()) {
            for (ResolveInfo resolveInfo : resInfo) {

                if (resolveInfo.activityInfo == null || resolveInfo.activityInfo.packageName.startsWith("com.facebook"))
                    continue;
                HashMap<String, String> info = new HashMap<>();
                info.put("packageName", resolveInfo.activityInfo.packageName);
                info.put("className", resolveInfo.activityInfo.name);
                info.put("simpleName", String.valueOf(resolveInfo.activityInfo.loadLabel(getCurrentContext().getPackageManager())));
                intentMetaInfo.add(info);
            }

            if (!intentMetaInfo.isEmpty()) {
                // sorting for nice readability
                Collections.sort(intentMetaInfo, new Comparator<HashMap<String, String>>() {
                    @Override
                    public int compare(HashMap<String, String> map, HashMap<String, String> map2) {
                        return map.get("simpleName").compareTo(map2.get("simpleName"));
                    }
                });
                // create the custom intent list
                for (HashMap<String, String> metaInfo : intentMetaInfo) {
                    Intent targetedShareIntent = (Intent) prototype.clone();
                    targetedShareIntent.setPackage(metaInfo.get("packageName"));
                    targetedShareIntent.setClassName(metaInfo.get("packageName"), metaInfo.get("className"));
                    targetedShareIntents.add(targetedShareIntent);
                }

                chooserIntent = Intent.createChooser(targetedShareIntents.remove(targetedShareIntents.size() - 1), "");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                return chooserIntent;
            }
        }

        return Intent.createChooser(prototype, "");
    }
    private boolean hasPublishPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }
    private void postStatusUpdate() {
        Profile profile = Profile.getCurrentProfile();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (profile != null && accessToken != null) {
            if (accessToken.getPermissions().contains("publish_actions")) {
                ShareLinkContent shareContent = new ShareLinkContent.Builder()
                        .setContentTitle("abc").setContentDescription("def")
                        .setContentUrl(Uri.parse("google.com"))
                        .build();

                ShareDialog shareDialog = new ShareDialog(getActivity());
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(getActivity(), "Share Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getActivity(), "Share Cancelled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        exception.printStackTrace();
                    }
                });

                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    shareDialog.show(shareContent);
                }
            }
        }else {
            List<String> permissions = Arrays.asList("publish_actions");
            LoginManager.getInstance().logInWithPublishPermissions(this, permissions);
        }
    }

}

