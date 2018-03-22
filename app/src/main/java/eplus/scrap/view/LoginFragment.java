package eplus.scrap.view;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import eplus.scrap.R;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.SharePreferences;

import static eplus.scrap.networking.ApiKey.URL_LOGIN;


public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnCompleteListener mListener;
    private LinearLayout cardViewCoupon;
    private LinearLayout cardViewMember;
    private LinearLayout cardViewMemberInfo;
    private Button  btlgin;
    private LinearLayout btcancel;
    private FragmentActivity myContext;



    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }
    public LoginFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(OnCompleteListener listener) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.mListener = listener;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        ImageView img_header = view.findViewById(R.id.my_awesome_toolbar);
        img_header.getLayoutParams().width = width;
        img_header.getLayoutParams().height = (int) (width * 0.4173);
        btlgin = view.findViewById(R.id.bt_login);
        btlgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLogin();
            }
        });
        btcancel = view.findViewById(R.id.bt_cancel);
        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onComplete();
                }
                //SharePreferences.saveBooleanPreference(getCurrentContext(),"firstrun",true);
                getFragmentManager().popBackStack();


            }
        });
        TextView textView = view.findViewById(R.id.tv_cancel);
        SpannableString spanString = new SpannableString("今は登録しない");
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.NORMAL), 0, spanString.length(), 0);
        textView.setText(spanString);


        return view;
    }
    private void loadLogin() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        WebViewFragment fragment =  WebViewFragment.newInstance(new WebViewFragment.OnCompleteListener() {
            @Override
            public void onComplete() {
                try {
                    getFragmentManager().popBackStack();
                    if(mListener != null){
                        mListener.onComplete();
                    }
                }catch (Exception ex){
                    Crashlytics.logException(ex);
                }

            }
        },URL_LOGIN + "&callback_url=scrap-app%3A//login-on",true,false);
        fragment.show(ft, "webview");
    }
    private void loadwebwithurl(String url) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        WebViewFragment fragment =  WebViewFragment.newInstance(new WebViewFragment.OnCompleteListener() {
            @Override
            public void onComplete() {
                updateView();

            }
        }, url,true,false);
        fragment.show(ft, "webview");
    }
    private void updateView(){
    }
    @Override
    public void onResume()
    {
        super.onResume();

    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onComplete();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCompleteListener) {
            mListener = (OnCompleteListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnCompleteListener {
        void onComplete();
    }

}
