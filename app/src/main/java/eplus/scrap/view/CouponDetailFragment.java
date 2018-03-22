package eplus.scrap.view;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurDialogFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import eplus.scrap.R;
import eplus.scrap.SmartAsyncPolicyHolder;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.Contents;
import eplus.scrap.common.QRCodeEncoder;
import eplus.scrap.common.ScrapContant;
import eplus.scrap.model.Coupon;

import static android.content.Context.WINDOW_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CouponDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CouponDetailFragment extends BlurDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final static int WIDTH = 500;
    private static int WHITE = 0xFFFFFFFF;
    private static int BLACK = 0xFF000000;
    protected OnCompleteListener mListener;
    TextView txtDate;
    // TODO: Rename and change types of parameters
    private Coupon mCoupon;
    private Context myContext;
    private TextView tv_full_des;
    private TextView tvUsed;
    private TextView tvEventName2;
    private TextView tvUseCondition;
    private TextView tvExpireDate;
    private TextView tvNote;
    private TextView tvCouponCode;
    private ImageView imgQRCode;
    private ImageView iv_close;
    private LinearLayout line_code_name;
    private LinearLayout line_qr_code;
    private TextView tv_qr_text;

    public CouponDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CouponDetailFragment newInstance(OnCompleteListener listener, Coupon coupon) {
        CouponDetailFragment fragment = new CouponDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, coupon);
        fragment.setArguments(args);
        fragment.mListener = listener;
        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCoupon = getArguments().getParcelable(ARG_PARAM1);
        }
        myContext =  getActivity();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) {
            mListener.onComplete();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coupon_detail, container, false);
        if(getDialog().getWindow() != null){
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(ScrapContant.BLUR_COLOR)));
        iv_close = view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_full_des = view.findViewById(R.id.tv_full_des);
        tv_full_des.setText(mCoupon.getHow_to_use());
        tvUsed = view.findViewById(R.id.tv_used);
        tvExpireDate = view.findViewById(R.id.tv_expire_date);
        tvUseCondition = view.findViewById(R.id.tv_use_condition);
        tvUseCondition.setText(Html.fromHtml(mCoupon.getCondition()));
//        if(mCoupon.getImage() != null) {
//            Glide.with(getCurrentContext())
//                    .load(mCoupon.getImage())
//                    .into(imgThumbnail);
//        }
        txtDate = view.findViewById(R.id.tv_date);
        tvNote = view.findViewById(R.id.tv_note);
        tvNote.setText(Html.fromHtml(mCoupon.getNotes()));
        DateFormat df = new SimpleDateFormat(CommonFunc.DATE_FORMAT_NONE_HOUR);
        if(mCoupon.getFrom_date() != null && !mCoupon.getFrom_date().isEmpty() && mCoupon.getTo_date() != null && !mCoupon.getTo_date().isEmpty()){
            try {
                Date time_start = df.parse(mCoupon.getFrom_date());
                Date time_end = df.parse(mCoupon.getTo_date());
                tvExpireDate.setText(CommonFunc.getDate(time_start.getTime(),"yyyy年MMMdd日(E)") + "~" + CommonFunc.getDate(time_end.getTime(),"MMMdd日(E)"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else
            {
                tvExpireDate.setText("");
        }

        tvEventName2 = view.findViewById(R.id.tv_title);
        tvEventName2.setText(mCoupon.getName());

        tvCouponCode = view.findViewById(R.id.tv_coupon_code);
        tvCouponCode.setText(mCoupon.getCoupon_code());
        line_code_name = view.findViewById(R.id.line_code_name);
        line_code_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFunc.setClipboard(getActivity(), mCoupon.getCoupon_code());
                CommonFunc.showDialogOneButton(getActivity(), "", getString(R.string.copy_coupon_code_to_clipboard));

            }
        });
        line_qr_code = view.findViewById(R.id.line_qr_code);
        WindowManager manager = (WindowManager) getCurrentContext().getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        if (mCoupon.isStatus() ) {
            imgQRCode = view.findViewById(R.id.image_qrcode);

            int smallerDimension = WIDTH < height ? width : height;
            smallerDimension = smallerDimension;
            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(mCoupon.getCoupon_code(),
                    null,
                    Contents.Type.TEXT,
                    BarcodeFormat.QR_CODE.toString(),
                    smallerDimension);
            try {
                Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                imgQRCode.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            tvUsed.setVisibility(View.GONE);
            line_qr_code.setPadding(0, 0, 0, 0);
            imgQRCode.setPadding(0,0,0,0);

        }else {
            //tvCouponCode.setVisibility(View.GONE);
            tvUsed.setVisibility(View.VISIBLE);
            line_code_name.setVisibility(View.GONE);

        }


        return  view;
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
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
        myContext = context;
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

    }

    public interface OnCompleteListener {
        void onComplete();
    }
    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }
}
