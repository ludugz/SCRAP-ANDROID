package eplus.scrap.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import eplus.scrap.R;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.model.Ticket;

/**
 * Created by nals-anhdv on 7/24/17.
 */

public class MyTicketAdapter extends ArrayAdapter<Ticket> implements View.OnClickListener{
    private static ClickListener clickListener;
    Context mContext;
    private ArrayList<Ticket> dataSet;
    private int lastPosition = -1;

    public MyTicketAdapter(ArrayList<Ticket> data, Context context) {
        super(context, R.layout.ticket_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        MyTicketAdapter dataModel=(MyTicketAdapter)object;

        switch (v.getId())
        {
            case R.id.image:
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
                break;
        }
    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Ticket dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.ticket_item, parent, false);
            viewHolder.txtEventName = convertView.findViewById(R.id.tv_eventName);
            viewHolder.imageView = convertView.findViewById(R.id.image_ticket);
            viewHolder.txtDate = convertView.findViewById(R.id.tv_date);
            viewHolder.txtTime = convertView.findViewById(R.id.tv_time);
            viewHolder.imgBackground = convertView.findViewById(R.id.img_ticket_bg);
            viewHolder.tvAreaName = convertView.findViewById(R.id.tv_area_name);
            viewHolder.tvStatus = convertView.findViewById(R.id.tv_status_ticket);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    viewHolder.imgBackground.setBackgroundResource(R.drawable.ticket_item_highligh_bg);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            viewHolder.imgBackground.setBackgroundResource(R.drawable.bg_item_my10);
                        }
                    }, 1000);
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    viewHolder.imgBackground.setBackgroundResource(R.drawable.bg_item_my10);
                    clickListener.onItemClick(position);
                }

                return false;
            }
        });

        DateFormat df = new SimpleDateFormat(CommonFunc.DATE_FORMAT);
        SimpleDateFormat formatter_to = new SimpleDateFormat("HH:mm");
        try {
            Date date = df.parse(dataModel.getEvent_start());
            viewHolder.txtDate.setText(CommonFunc.getDate(date.getTime(),"yyyy年MMMdd日(E)"));//yyyy年MMM dd日
            viewHolder.txtTime.setText(formatter_to.format(date)+ "~");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.txtEventName.setText(dataModel.getProduct_name());
        viewHolder.tvAreaName.setText(dataModel.getVenue());


        if(dataModel.getImage_path() != null) {
            Glide.with(mContext)
                    .load(dataModel.getImage_path())
                    .placeholder(R.drawable.my10_thumbnail)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.imageView);
        }
        switch (dataModel.getStatus()) {
            case 1 :
                viewHolder.tvStatus.setText(R.string.processing);
                break;
            case 2:
                viewHolder.tvStatus.setText(R.string.un_used);
                break;
            case 3:
                viewHolder.tvStatus.setText(R.string.used);
                break;
            case 4:
                viewHolder.tvStatus.setText(R.string.ticket_cancel);
                break;
            case 5:
                viewHolder.tvStatus.setText(R.string.invalid);
                break;
        }
        return convertView;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MyTicketAdapter.clickListener = clickListener;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    public interface ClickListener {
        void onItemClick(int position);
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtEventName;
        TextView txtDate;
        TextView txtTime;
        ImageView imageView;
        ImageView imgBackground;
        TextView tvAreaName;
        TextView tvStatus;
    }

}
