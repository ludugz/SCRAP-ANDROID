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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eplus.scrap.R;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.model.SchedulesBean;

/**
 * Created by nals-anhdv on 7/25/17.
 */

public class TimeAdapter extends ArrayAdapter<SchedulesBean> {
    public TimeAdapter(Context context, List<SchedulesBean> routesArrayList) {
        super(context, 0, routesArrayList);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SchedulesBean timeLineBean = getItem(position);
        //timeLineBean.setStatus(-1);
        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.time_item, parent, false);


            ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
            layoutParams.width = layoutParams.height = (int) (size.x * 0.1466);

            int top_pading = (int) (size.x * 0.0226);
            LinearLayout lineMain = convertView.findViewById(R.id.line_time_main);
            lineMain.setPadding(top_pading,top_pading,top_pading,top_pading);

        }

        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.tv_time);
        ImageView img_status = convertView.findViewById(R.id.tv_status_time);
        img_status.getLayoutParams().width = img_status.getLayoutParams().height = (int) (size.x * 0.048);
        ViewGroup.MarginLayoutParams img_status_params = (ViewGroup.MarginLayoutParams) img_status.getLayoutParams();
        int spacing = (int) (size.x * 0.01233);
        img_status_params.topMargin = spacing;
        TextView tvDaining = convertView.findViewById(R.id.tv_daining);
        ViewGroup.MarginLayoutParams tvDaining_params = (ViewGroup.MarginLayoutParams) tvDaining.getLayoutParams();
        tvDaining_params.topMargin = spacing;
        // Populate the data into the template view using the data object
        SimpleDateFormat sdf = new SimpleDateFormat(CommonFunc.DATE_FORMAT);

        Date dt;
        SimpleDateFormat formatter_to = new SimpleDateFormat(CommonFunc.HOUR_FORMAT);
        try {
            dt = sdf.parse(timeLineBean.getDate_time()) ;

            tvName.setText(formatter_to.format(dt));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Drawable bg5 = getContext().getResources().getDrawable( R.drawable.backround_rect_status5 );
        Drawable bg12 = getContext().getResources().getDrawable( R.drawable.backround_rect_status1_2 );
        Drawable bg3 = getContext().getResources().getDrawable( R.drawable.backround_rect_status3 );
        Calendar c = Calendar.getInstance();
        tvDaining.setVisibility(View.GONE);


            if(timeLineBean.getStatus() < 0) {
                int pading = (int) (size.x * 0.01233);
                img_status.getLayoutParams().width = img_status.getLayoutParams().height = (int) (size.x * 0.032);                int top_pading = (int) (size.x * 0.00633);
                img_status_params.topMargin = pading/8;

            }

                convertView.setEnabled(true);
                switch (timeLineBean.getStatus()) {
                    case 1:
                        img_status.setBackgroundResource(R.drawable.ic_circle_red);
                        convertView.setBackgroundDrawable(bg12);
                        break;
                    case 2:
                        img_status.setBackgroundResource(R.drawable.ic_triangle);
                        convertView.setBackgroundDrawable(bg12);
                        break;
                    case 3:
                        img_status.setBackgroundResource(R.drawable.x);
                        convertView.setBackgroundDrawable(bg3);
                        break;
                    case 4:
                        tvName.setTextColor(Color.parseColor("#666666"));
                        img_status.setVisibility(View.GONE);
                        tvDaining.setText(R.string.members_in_advance);
                        tvDaining.setVisibility(View.VISIBLE);
                        convertView.setBackgroundDrawable(bg5);
                        break;
                    case 5:
                        tvName.setTextColor(Color.parseColor("#666666"));
                        img_status.setBackgroundResource(R.drawable.ic_dash);
                        convertView.setBackgroundDrawable(bg5);
                        break;
                    case 6:
                        tvName.setTextColor(Color.parseColor("#666666"));
                        img_status.setVisibility(View.GONE);
                        tvDaining.setText(R.string.in_preparation);
                        tvDaining.setVisibility(View.VISIBLE);
                        convertView.setBackgroundDrawable(bg5);
                        break;



                }
        // Return the completed view to render on screen
        return convertView;
    }
}
