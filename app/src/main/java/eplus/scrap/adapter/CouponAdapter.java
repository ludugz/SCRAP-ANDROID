package eplus.scrap.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import eplus.scrap.R;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.model.Coupon;

/**
 * Created by nals-anhdv on 7/24/17.
 */

public class CouponAdapter extends ArrayAdapter<Coupon> implements View.OnClickListener{
    private ArrayList<Coupon> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtEventName;
        //TextView tvEvenName2;
        //TextView tvStatus;
        TextView txtDate;
        //TextView txtTime;
        ImageView imageView;
        //RelativeLayout relativeLayoutUnuse;
        //LinearLayout linearLayoutUsed;
    }

    public CouponAdapter(ArrayList<Coupon> data, Context context) {
        super(context, R.layout.ticket_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {


    }

    private int lastPosition = -1;

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Coupon dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag


        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.coupon_item, parent, false);
            viewHolder.txtEventName = convertView.findViewById(R.id.tv_eventName);
            viewHolder.imageView = convertView.findViewById(R.id.img_coupon_item_bg);
            viewHolder.txtDate = convertView.findViewById(R.id.tv_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DateFormat df = new SimpleDateFormat(CommonFunc.DATE_FORMAT_NONE_HOUR);
        if(dataModel.getTo_date() != null) {
            try {
                Date date = df.parse(dataModel.getTo_date());
                Date datenow = new Date();
                String languageToLoad = Locale.getDefault().getLanguage();
                if (languageToLoad.equals("ja")) {
                    viewHolder.txtDate.setText(CommonFunc.getDate(date.getTime(), "yyyy年MMMdd日(E)") + " " + mContext.getString(R.string.until));
                } else {
                    viewHolder.txtDate.setText(mContext.getString(R.string.until)+ " " + CommonFunc.getDate(date.getTime(), "yyyy.MM.dd(E)")  );
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        viewHolder.txtEventName.setText(dataModel.getName());
        if(dataModel.isStatus()) {
            viewHolder.imageView.setImageResource(R.drawable.coupon_item_1_bg);

        } else {
            viewHolder.imageView.setImageResource(R.drawable.coupon_item_2_bg);
        }
        convertView.setOnTouchListener(new View.OnTouchListener() {
            private Rect rect;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    viewHolder.imageView.setColorFilter(Color.argb(50, 0, 0, 0));
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            viewHolder.imageView.setColorFilter(Color.argb(0, 0, 0, 0));
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    viewHolder.imageView.setColorFilter(Color.argb(0, 0, 0, 0));
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                        viewHolder.imageView.setColorFilter(Color.argb(0, 0, 0, 0));
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                        viewHolder.imageView.setColorFilter(Color.argb(0, 0, 0, 0));
                    }
                }
                return false;
            }
        });

        return convertView;
    }
}
