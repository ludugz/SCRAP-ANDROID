package eplus.scrap.view;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import eplus.scrap.R;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.SharePreferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewNEWSFragment extends DialogFragment implements MyWebChromeClient.ProgressListener {
    private static final String ARG_PARAM = "URL" ;
    private static final String TAG = "WEBVIEW";
    private static final String ARG_PARAM_REMOVE_HEADER = "remove_header";
    protected OnCompleteListener mListener;
    boolean mRemoveHeader;
    private FragmentActivity myContext;
    private String mURL;
    private WebView mWebview;
    private ProgressBar mProgressBar;


    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }
    public WebViewNEWSFragment() {
        // Required empty public constructor
    }

    public static WebViewNEWSFragment newInstance(OnCompleteListener listener, String url, boolean removeheader) {
        WebViewNEWSFragment fragment = new WebViewNEWSFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, url);
        args.putBoolean(ARG_PARAM_REMOVE_HEADER,removeheader);
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
        }
        setHasOptionsMenu(false);
        myContext = getActivity();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
        public void onUpdateProgress(int progressValue) {
            mProgressBar.setProgress(progressValue);

        }
    public static void viewInBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (null != intent.resolveActivity(context.getPackageManager())) {
            context.startActivity(intent);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

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
                        viewInBrowser(getCurrentContext(),mURL.trim());

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
            }
        });
        //toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.back_button));
        mProgressBar = view.findViewById(R.id.progressBar);
        this.mWebview = view.findViewById(R.id.webview);
        WebSettings settings = this.mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        mWebview.addJavascriptInterface(new WebAppInterface(getCurrentContext()),"Android");
        this.mWebview.setFocusable(true);
        //if(mRemoveHeader)  mURL = mURL + "&remove_header_footer=1&sl=ja";
        this.mWebview.loadUrl(mURL);
        this.mWebview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebview.setWebChromeClient(new MyWebChromeClient(this));
        mWebview.setWebViewClient(new WebViewClient() {
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
                handler.proceed("tmc", "tmc2017");
            }


            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
            }

        });


        return view;

    }

    private void shareTextUrl() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, mURL);

        startActivity(Intent.createChooser(share, "Share link!"));
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
