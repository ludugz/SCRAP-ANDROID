package eplus.scrap.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import eplus.scrap.R;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.SharePreferences;
import eplus.scrap.model.UserInfo;
import eplus.scrap.networking.BaseRestClient;
import eplus.scrap.networking.DataResponse;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static eplus.scrap.networking.ApiKey.URL_ENTER_INVITATION_CODE;
import static eplus.scrap.networking.ApiKey.URL_GET_USER_INFO;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EnterInviteCodeIV40Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EnterInviteCodeIV40Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterInviteCodeIV40Fragment extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int USED = -1;
    private static final int OUTOFTIME = 0;
    private static final int FRIENDRECEIVED = 1;
    private static final int YOURECEIVED = 2;
    private static final int YOURECEIVED2 = 3;
    private static final int INVALIDCODE = 4;
    private String mParam1;
    private String mParam2;

    private ImageButton ic_left_bt;
    private UserInfo userInfo;
    private LinearLayout lineOutOfTime;
    private LinearLayout lineUsed;
    private LinearLayout lineFriendReceived;
    private LinearLayout lineYouReceived;
    private LinearLayout lineInvalidCode;
    private LinearLayout lineSuccess;
    private RelativeLayout reNointernet;
    private LinearLayout lineBtRetry;
    private String inviteCode = "";
    protected OnCompleteListener mListener;
    private LinearLayout btcancel;
    private TextView editext;
    private FragmentActivity myContext;


    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }
    public interface OnCompleteListener {
        void onComplete();
    }
    public EnterInviteCodeIV40Fragment() {
        // Required empty public constructor
    }
    public static EnterInviteCodeIV40Fragment newInstance(OnCompleteListener listener) {
        EnterInviteCodeIV40Fragment fragment = new EnterInviteCodeIV40Fragment();
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
        final View view = inflater.inflate(R.layout.fragment_enter_invite_code_iv40, container, false);
        editext = view.findViewById(R.id.txt_invite_code);
        editext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    CommonFunc.hideSoftKeyboard(getActivity());
                    String code = String.valueOf(v.getText()).trim();
                    enterInviteCoe(code);
                    return false;
                }
                return false;
            }
        });
        ic_left_bt = view.findViewById(R.id.ic_left_bt);
        ic_left_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonFunc.hideSoftKeyboard(getActivity(),view);
                if(mListener != null){
                    mListener.onComplete();
                }
                dismiss();
            }
        });
        lineOutOfTime = view.findViewById(R.id.line_outoftime_msg);
        lineSuccess = view.findViewById(R.id.line_gotcoupon_msg);
        lineUsed = view.findViewById(R.id.line_used_msg);
        lineFriendReceived = view.findViewById(R.id.line_inviterony_msg);
        lineInvalidCode = view.findViewById(R.id.line_invalid_code_msg);

        Button btEnter = view.findViewById(R.id.bt_Enter);
        btEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonFunc.hideSoftKeyboard(getActivity(),view);
                String code = String.valueOf(editext.getText());
                if(code.isEmpty()) {
                    Toast.makeText(getCurrentContext(), R.string.invalid_invite_code,
                            Toast.LENGTH_SHORT).show();
                } else {
                    inviteCode = code;
                    enterInviteCoe(inviteCode);
                }
            }
        });
        //fetchUserInfo();
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
                        enterInviteCoe(inviteCode);
                        reNointernet.setVisibility(View.GONE);
                    }
                }
                return true;
            }
        });
        TextView textView = view.findViewById(R.id.tv_cancel);
        SpannableString spanString = new SpannableString("今は登録しない");
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.NORMAL), 0, spanString.length(), 0);
        textView.setText(spanString);
        btcancel = view.findViewById(R.id.bt_cancel);
        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonFunc.hideSoftKeyboard(getActivity(),view);
                if(mListener != null){
                    mListener.onComplete();
                }
                dismiss();


            }
        });
        return view;

    }
    private void hideAllMsg() {
        lineOutOfTime.setVisibility(View.GONE);
        lineSuccess.setVisibility(View.GONE);
        lineUsed.setVisibility(View.GONE);
        lineFriendReceived.setVisibility(View.GONE);
        lineInvalidCode.setVisibility(View.GONE);
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
                            if(userInfo.isAlready_enter_invitation_code()) {
                                lineSuccess.setVisibility(View.VISIBLE);
                                editext.setEnabled(false);
                            }




                        }
                    }
                } catch (JSONException e) {e.printStackTrace();

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
    private void enterInviteCoe(String invitecode) {
        hideAllMsg();
        String token = SharePreferences.getStringPreference(getCurrentContext(),"token");

        HashMap<String, String> hearder = new HashMap<>();
        if(!token.isEmpty()){
            hearder.put("token", token);
        }
        HashMap<String, String> params = new HashMap<>();
        //params.put("filter_type", "1");
        params.put("invitation_code",invitecode);



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
                        if(jObject.getJSONObject("data") != null){
                            int status = jObject.getJSONObject("data").getInt("status");
                            switch (status){
                                case USED :
                                    lineUsed.setVisibility(View.VISIBLE);
                                    break;
                                case OUTOFTIME:
                                    lineOutOfTime.setVisibility(View.VISIBLE);
                                    break;
                                case FRIENDRECEIVED:
                                    lineFriendReceived.setVisibility(View.VISIBLE);
                                    break;
                                case INVALIDCODE:
                                    lineInvalidCode.setVisibility(View.VISIBLE);
                                    break;
                                case YOURECEIVED:
                                    lineSuccess.setVisibility(View.VISIBLE);
                                    break;
                                case YOURECEIVED2:
                                    lineSuccess.setVisibility(View.VISIBLE);
                                    break;

                                default:
                                    break;
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

        BaseRestClient.get(getActivity(), URL_ENTER_INVITATION_CODE, params, hearder, responseVeryfy, true);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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



}
