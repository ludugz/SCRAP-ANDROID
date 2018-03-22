package eplus.scrap.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import eplus.scrap.R;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.model.SchedulesBean;

/**
 * Created by nals-anhdv on 7/25/17.
 */

public class VenueAdapter extends ArrayAdapter<SchedulesBean> {
    public VenueAdapter(Context context, List<SchedulesBean> routesArrayList) {
        super(context, 0, routesArrayList);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SchedulesBean timeLineBean = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.boxoffice_item, parent, false);
        }
        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        convertView.getLayoutParams().height = (int) (size.x * 0.166);
        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.tv_time);
        TextView tvHome = convertView.findViewById(R.id.tv_status_time);
        ImageView icStatus = convertView.findViewById(R.id.ic_status);
        // Populate the data into the template view using the data object
        SimpleDateFormat sdf = new SimpleDateFormat(CommonFunc.DATE_FORMAT);

        Date dt= null;
        SimpleDateFormat formatter_to = new SimpleDateFormat("HH:mm");
        try {
            dt = sdf.parse(timeLineBean.getDate_time()) ;

            tvName.setText(formatter_to.format(dt) + "~");

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Drawable bg5 = getContext().getResources().getDrawable( R.drawable.backround_rect_status5 );
        Drawable bg12 = getContext().getResources().getDrawable( R.drawable.backround_rect_status1_2 );
        Drawable bg3 = getContext().getResources().getDrawable( R.drawable.backround_rect_status3 );

//        if (dt.before(new Date())) {
//            tvHome.setText(R.string.sales_end);
//            tvHome.setTextColor(Color.parseColor("#666666"));
//            icStatus.setImageResource(R.drawable.ic_dash);
//            tvName.setText(formatter_to.format(dt) + "~");
//            tvName.setTextColor(Color.parseColor("#666666"));
//            convertView.setBackgroundDrawable(bg5);
//        } else {

            switch (timeLineBean.getStatus()) {
                case 1:
                    tvHome.setText(R.string.available);
                    icStatus.setImageResource(R.drawable.ic_circle_red);
                    convertView.setBackgroundDrawable(bg12);
                    break;
                case 2:
                    tvHome.setText(R.string.only_a_few_left);
                    icStatus.setImageResource(R.drawable.ic_triangle);
                    convertView.setBackgroundDrawable(bg12);
                    break;
                case 3:
                    tvHome.setText(R.string.sold_out);
                    icStatus.setImageResource(R.drawable.x);
                    convertView.setBackgroundDrawable(bg3);
                    break;
                case 0:
                    tvHome.setText(R.string.sales_end);
                    icStatus.setImageResource(R.drawable.ic_dash);
                    convertView.setBackgroundDrawable(bg5);
                    break;
                case 4:
                    tvHome.setTextColor(Color.parseColor("#666666"));
                    tvName.setTextColor(Color.parseColor("#666666"));
                    tvHome.setText(R.string.members_in_advance);
                    icStatus.setVisibility(View.GONE);
                    convertView.setBackgroundDrawable(bg5);
                    break;
                case 5:
                    tvHome.setText(R.string.sales_end);
                    icStatus.setImageResource(R.drawable.ic_dash);
                    tvName.setText(formatter_to.format(dt) + "~");
                    tvHome.setTextColor(Color.parseColor("#666666"));
                    tvName.setTextColor(Color.parseColor("#666666"));
                    convertView.setBackgroundDrawable(bg5);
                    tvName.setTextColor(Color.parseColor("#666666"));
                    break;
                case 6:
                    tvHome.setTextColor(Color.parseColor("#666666"));
                    tvName.setTextColor(Color.parseColor("#666666"));
                    tvHome.setText(R.string.in_preparation);
                    icStatus.setVisibility(View.GONE);
                    convertView.setBackgroundDrawable(bg5);
                    break;

            }
        //}

        // Return the completed view to render on screen
        return convertView;
    }
}
