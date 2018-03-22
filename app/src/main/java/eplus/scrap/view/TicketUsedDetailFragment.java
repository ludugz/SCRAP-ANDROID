package eplus.scrap.view;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurDialogFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import eplus.scrap.R;
import eplus.scrap.SmartAsyncPolicyHolder;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.ScrapContant;
import eplus.scrap.model.Ticket;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketUsedDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketUsedDetailFragment extends BlurDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final static int WIDTH = 500;
    private static int WHITE = 0xFFFFFFFF;
    private static int BLACK = 0xFF000000;
    protected OnCompleteListener mListener;
    // TODO: Rename and change types of parameters
    private Ticket mTicket;
    private FragmentActivity myContext;
    private ImageView iv_close;
    private TextView tv_amount;
    private TextView tv_seat_num;
    private TextView tv_date_time;
    private TextView tv_hour_start;
    private TextView tv_hour_end;
    private TextView tv_title_of_tour ;

    public TicketUsedDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TicketUsedDetailFragment newInstance(OnCompleteListener listener, Ticket ticket) {
        TicketUsedDetailFragment fragment = new TicketUsedDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, ticket);
        fragment.setArguments(args);
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        if(getDialog().getWindow() != null){
            getDialog().getWindow()
                    .getAttributes().windowAnimations = R.style.DialogAnimation;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTicket = getArguments().getParcelable(ARG_PARAM1);
        }
        myContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ticket_used_detail, container, false);
        if(getDialog().getWindow() != null){
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        initViewDetail(view);
        fillDataDetail(mTicket);

        return  view;
    }

    @NonNull
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder()
                .overlayColor(Color.parseColor(ScrapContant.BLUR_COLOR))  // semi-transparent white color
                .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
                .debug(true)
                .build();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //myContext = context;
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) {
            mListener.onComplete();
        }

    }

    private void initViewDetail(View root) {

        tv_title_of_tour = root.findViewById(R.id.tv_title_of_tour);
        iv_close = root.findViewById(R.id.iv_close);
        tv_amount = root.findViewById(R.id.tv_amount);
        tv_seat_num = root.findViewById(R.id.tv_seat_num);
        tv_date_time = root.findViewById(R.id.tv_date_time);
        tv_hour_start = root.findViewById(R.id.tv_hour_start);
        tv_hour_end = root.findViewById(R.id.tv_hour_end);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onComplete();
                }
                dismiss();
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void fillDataDetail(Ticket ticket) {
        tv_title_of_tour.setText(ticket.getProduct_name());
        tv_amount.setText(String.valueOf(ticket.getAmount()));
        //String str_seat = "<b>" + ticket.getSeat() + "</b>" + "チケット " + "<big><b>" + ticket.getAmount() + "</b></big>" + getString(R.string.sheet);
        //tv_seat_num.setText(Html.fromHtml(str_seat));
        tv_hour_start.setText(CommonFunc.getTime(ticket.getEvent_start()));
        //tv_hour_end.setText(CommonFunc.getTime(ticket.getScanning_start()));
        DateFormat df = new SimpleDateFormat(CommonFunc.DATE_FORMAT);
        try {
            Date date = df.parse(ticket.getDate_time());
            String languageToLoad = Locale.getDefault().getLanguage();
            if (languageToLoad.equals("ja")) {
                tv_date_time.setText(CommonFunc.getDate(date.getTime(), "yyyy年MM月DD日 (E)"));
            } else {
                tv_date_time.setText(CommonFunc.getDate(date.getTime(), "yyyy/MM/dd (E)"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public interface OnCompleteListener {
        void onComplete();
    }
    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }
}
