package eplus.scrap.view;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import eplus.scrap.R;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.model.Coupon;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CouponUsedDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CouponUsedDetailFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private Coupon mCoupon;
    private FragmentActivity myContext;
    private TextView tvEventName;
    private ImageView imgThumbnail;
    private TextView tvEventName2;
    private TextView tvUseCondition;
    private TextView tvExpireDate;
    private TextView tvNote;
    private TextView tvNote1;


    public CouponUsedDetailFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CouponUsedDetailFragment newInstance(Coupon coupon) {
        CouponUsedDetailFragment fragment = new CouponUsedDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, coupon);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCoupon = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coupon_used_detail, container, false);
        tvEventName = view.findViewById(R.id.tv_eventName);
        tvEventName.setText(mCoupon.getName());
        imgThumbnail = view.findViewById(R.id.image);
        tvExpireDate = view.findViewById(R.id.tv_expire_date);
        tvUseCondition = view.findViewById(R.id.tv_use_condition);
//        tvUseCondition.setText(Html.fromHtml(mCoupon.getUsed_condition()));
//        tvNote = (TextView) view.findViewById(R.id.tv_note) ;
//        tvNote1 = (TextView) view.findViewById(R.id.tv_note1) ;
//        tvNote.setText(Html.fromHtml(mCoupon.getNote()));
//        tvNote1.setText(Html.fromHtml(mCoupon.getNote()));

        DateFormat df = new SimpleDateFormat(CommonFunc.DATE_FORMAT);
        try {
            Date time_start = df.parse(mCoupon.getFrom_date());
            Date time_end = df.parse(mCoupon.getTo_date());
            tvExpireDate.setText(CommonFunc.getDate(time_start.getTime(),"yyyy年MMMdd日(E)") + "~" + CommonFunc.getDate(time_end.getTime(),"yyyy年MMMdd日(E)"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
//        if(mCoupon.getImage() != null) {
//            Glide.with(getCurrentContext())
//                    .load(mCoupon.getImage())
//                    .into(imgThumbnail);
//        }

        tvEventName2 = view.findViewById(R.id.tv_title);
        tvEventName2.setText(mCoupon.getName());


        return  view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity) context;
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

    }

}
