package eplus.scrap.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import static eplus.scrap.networking.ApiKey.URL_CHECK_INVITATION_STATUS;
import static eplus.scrap.networking.ApiKey.URL_GET_USER_INFO;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShareOnSNSIV20Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShareOnSNSIV20Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShareOnSNSIV20Fragment extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int  OUTOFTIME = 0;
    private static final int  USED = -1;
    private static final int  FRIENDWILLRECEIVE = 1;
    private static final int  YOUWILLRECEIVE = 2;

    private String mParam1;
    private String mParam2;
    private Button btShare;

    private OnFragmentInteractionListener mListener;
    private ImageButton ic_left_bt;
    private UserInfo userInfo;
    private RelativeLayout reNointernet;
    private LinearLayout lineBtRetry;
    private TextView tvInviteCode;
    private TextView tvInviteStatus;
    private FragmentActivity myContext;



    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }
    public ShareOnSNSIV20Fragment() {
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
    public static ShareOnSNSIV20Fragment newInstance(String param1, String param2) {
        ShareOnSNSIV20Fragment fragment = new ShareOnSNSIV20Fragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_on_sn_iv20, container, false);
        LinearLayout line = view.findViewById(R.id.line_top);
        tvInviteCode = view.findViewById(R.id.tv_invite_code);
        tvInviteStatus = view.findViewById(R.id.tv_invite_status);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btShare = view.findViewById(R.id.bt_share);
        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userInfo != null) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    String sAux = userInfo.getTitle_invite();
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sAux);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, ""));
                } else  {
                    Toast.makeText(getCurrentContext(),R.string.can_not_get_user_info,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        ic_left_bt = view.findViewById(R.id.ic_left_bt);
        ic_left_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
        fetchUserInfo();
        checkInvite();
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
                            if(userInfo.getInvitation_code()!= null) {
                                tvInviteCode.setText(userInfo.getInvitation_code());
                                tvInviteCode.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CommonFunc.setClipboard(getActivity(), userInfo.getTitle_invite());
                                        CommonFunc.showDialogOneButton(getActivity(), "", "招待コードがコピーされました");
                                    }
                                });
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
    private void checkInvite() {
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
                            if(jObject.getJSONObject("data") != null){
                                int status = jObject.getJSONObject("data").getInt("status");
                                switch (status){
                                    case USED :
                                        showUsedMessage();
                                        break;
                                    case OUTOFTIME:
                                        showOutOfTimeMsg();
                                        break;
                                    case FRIENDWILLRECEIVE:
                                        showFriendReceiveMsg();
                                        break;
                                    case YOUWILLRECEIVE:
                                        showYouReceiveMsg();
                                        break;

                                    default:
                                        break;
                                }
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

        BaseRestClient.get(getActivity(), URL_CHECK_INVITATION_STATUS, null, hearder, responseVeryfy, false);
    }

    private void showUsedMessage() {
        tvInviteStatus.setText("クーポンは配信済みです\nクーポンは1回のみGetできます");
        tvInviteStatus.setTextColor(Color.RED);
    }
    private void showOutOfTimeMsg() {
        tvInviteStatus.setText("キャンペーン期間外です");
        tvInviteStatus.setTextColor(Color.RED);
    }
    private void showFriendReceiveMsg() {
        tvInviteStatus.setText("友達がこのコードを入力すると、\n友達にクーポンが配信されます");
        tvInviteStatus.setTextColor(Color.BLUE);
    }
    private void showYouReceiveMsg() {
        tvInviteStatus.setText("友達がこのコードを入力すると、\nあなたにクーポンが配信されます");
        tvInviteStatus.setTextColor(Color.BLUE);
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
