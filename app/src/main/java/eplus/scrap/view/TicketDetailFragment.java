package eplus.scrap.view;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurDialogFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import eplus.scrap.R;
import eplus.scrap.SmartAsyncPolicyHolder;
import eplus.scrap.adapter.SeatAdapter;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.Helper;
import eplus.scrap.common.ScrapContant;
import eplus.scrap.model.Ticket;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketDetailFragment extends BlurDialogFragment {
    private static final String ARG_PARAM1 = "param1";
    protected OnCompleteListener mListener;
    private Ticket mTicket;
    private Context myContext;
    //private RelativeLayout mRootDetail;
    //private ImageView iv_detail;
    private ImageView iv_close;
    private ImageView iv_qr_code;
    private TextView tv_amount;
    private TextView tv_seat_num;
    private TextView tv_date_time;
    private TextView tv_hour_start;
    private TextView tv_hour_end;
    private TextView tv_title_of_tour;
    private TextView tv_area_name;
    private TextView tv_ticket_oder_id;
    private MultiFormatWriter multiFormat = new MultiFormatWriter();
    private BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

    private TextView tv_ticket_status;
    private ListView lvSeat;

    public TicketDetailFragment() {
        // Required empty public constructor
    }
    public static TicketDetailFragment newInstance(OnCompleteListener listener, Ticket ticket) {
        TicketDetailFragment fragment = new TicketDetailFragment();
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
        if (dialog != null && dialog.getWindow()!= null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        if(getDialog().getWindow()!= null){
            getDialog().getWindow()
                    .getAttributes().windowAnimations = R.style.DialogAnimation;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getParcelable(ARG_PARAM1) != null) {
            mTicket = getArguments().getParcelable(ARG_PARAM1);
        }
        myContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ticket_detail, container, false);
        if(getDialog().getWindow()!= null){
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
                .debug(false)
                .build();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
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
        //mRootDetail = (RelativeLayout) root.findViewById(R.id.rl_view_detail);
        //iv_detail = (ImageView) root.findViewById(R.id.iv_detail);
        iv_close = root.findViewById(R.id.iv_close);
        iv_qr_code = root.findViewById(R.id.iv_qr_code);
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
        tv_title_of_tour = root.findViewById(R.id.tv_title_of_tour);
        tv_area_name = root.findViewById(R.id.tv_area_name);
        tv_ticket_oder_id =  root.findViewById(R.id.tv_ticket_oder_id);
        tv_ticket_status = root.findViewById(R.id.tv_ticket_status);
        lvSeat = root.findViewById(R.id.listview_seat);
    }
    @SuppressLint("SimpleDateFormat")
    private void fillDataDetail(Ticket ticket) {
        tv_title_of_tour.setText(ticket.getProduct_name());
        if (!ticket.getQr_code().isEmpty()) {
            tv_ticket_status.setVisibility(View.GONE);
            try {
                BitMatrix bitMatrix = multiFormat.encode(ticket.getQr_code(), BarcodeFormat.QR_CODE,
                        1000, 1000);
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                iv_qr_code.setImageBitmap(bitmap);
                iv_qr_code.setPadding(0,0,0,0);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            iv_qr_code.setImageResource(R.drawable.ic_qr_code);
        }
        tv_amount.setText(String.valueOf(ticket.getAmount()));
        tv_hour_start.setText(CommonFunc.getTime(ticket.getEvent_start())+ "~");
        List<Ticket.SeatsBean> listSeat = ticket.getSeats();
        SeatAdapter seatAdapter = new SeatAdapter(getCurrentContext(),listSeat);

        lvSeat.setAdapter(seatAdapter);
        Helper.getListViewSize_none(lvSeat,0);
        DateFormat df = new SimpleDateFormat(CommonFunc.DATE_FORMAT);
        try {
            Date date = df.parse(ticket.getEvent_start());
            String languageToLoad = Locale.getDefault().getLanguage();
            if (languageToLoad.equals("ja")) {
                tv_date_time.setText(CommonFunc.getDate(date.getTime(), "yyyy年MMMdd日(E)"));
            } else {
                tv_date_time.setText(CommonFunc.getDate(date.getTime(), "yyyy/MM/dd (E)"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_area_name.setText(ticket.getVenue());
        tv_ticket_oder_id.setText("" + ticket.getOrder_id());
        switch (ticket.getStatus()) {
            case 1 :
                tv_ticket_status.setText(R.string.processing);
                break;
            case 2:
                tv_ticket_status.setText(R.string.un_used);
                break;
            case 3:
                tv_ticket_status.setText(R.string.used);
                break;
            case 4:
                tv_ticket_status.setText(R.string.ticket_cancel);
                break;
            case 5:
                tv_ticket_status.setText(R.string.invalid);
                break;
            default:
                tv_ticket_status.setText("");
                break;
        }
    }

    public interface OnCompleteListener {
        void onComplete();
    }
    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }
}
