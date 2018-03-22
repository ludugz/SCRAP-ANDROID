package eplus.scrap.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import eplus.scrap.R;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.SimpleHeaderDecoration;
import eplus.scrap.model.SchedulesBean;
import eplus.scrap.model.TourDetail;
import eplus.scrap.networking.ApiKey;

/**
 * Created by nals-anhdv on 7/24/17.
 */

public class EventAdapter extends ArrayAdapter<TourDetail> implements View.OnClickListener{
    Context mContext;
    private static ClickListener clickListener;
    private LinearLayoutManager layoutManager;

    // View lookup cache
    private static class ViewHolder {
        TextView txtEventName;
        TextView txtTime;
        TextView txtVenue;
        ImageView imageView;
        RecyclerView listTimeline;
        ImageButton btNextTime;
        RelativeLayout nexttime_layout;
    }

    public EventAdapter(ArrayList<TourDetail> data, Context context) {
        super(context, R.layout.event_item, data);
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final TourDetail dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.event_item, parent, false);
            viewHolder.nexttime_layout = convertView.findViewById(R.id.re_bt_next_time);
            viewHolder.txtEventName = convertView.findViewById(R.id.tv_eventName);
            viewHolder.txtTime = convertView.findViewById(R.id.tv_time);
            viewHolder.txtVenue = convertView.findViewById(R.id.tv_venue);
            viewHolder.imageView = convertView.findViewById(R.id.image);
            viewHolder.listTimeline = convertView.findViewById(R.id.listTimeline);
            layoutManager
                    = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

            viewHolder.listTimeline.setLayoutManager(layoutManager);
            SimpleHeaderDecoration simpleHeaderDecoration = new SimpleHeaderDecoration(0,0,0, SimpleHeaderDecoration.HORIZONTAL);
            viewHolder.listTimeline.addItemDecoration(simpleHeaderDecoration);
            viewHolder.btNextTime = convertView.findViewById(R.id.bt_nexttime);
            viewHolder.btNextTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayoutManager llm = (LinearLayoutManager) viewHolder.listTimeline.getLayoutManager();
                    int dateSelect_pos = llm.findLastVisibleItemPosition() +4;
                    if(dateSelect_pos > dataModel.getSchedules().size() -1) {
                        dateSelect_pos = dataModel.getSchedules().size() - 1;
                        viewHolder.nexttime_layout.setVisibility(View.GONE);
                    }
                    llm.scrollToPosition(dateSelect_pos);
                    ViewGroup.MarginLayoutParams listTimeline_params_margin = (ViewGroup.MarginLayoutParams) viewHolder.listTimeline.getLayoutParams();
                    listTimeline_params_margin.leftMargin = 0;
                    viewHolder.listTimeline.requestLayout();
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        final LinearLayout line_top = convertView.findViewById(R.id.line_top_event);
        line_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickListener != null){
                    clickListener.onItemClick(position);
                }
            }
        });
        final View finalConvertView = convertView;
        line_top.setOnTouchListener(new View.OnTouchListener() {
            private Rect rect;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    finalConvertView.setBackgroundColor(Color.argb(50, 0, 0, 0));
                    viewHolder.nexttime_layout.setBackgroundColor(Color.argb(0, 0, 0, 0));

                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            finalConvertView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                            viewHolder.nexttime_layout.setBackgroundColor(Color.argb(0, 0, 0, 0));
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    finalConvertView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                    viewHolder.nexttime_layout.setBackgroundColor(Color.argb(0, 0, 0, 0));

                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    if(rect != null && !rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                        finalConvertView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                        viewHolder.nexttime_layout.setBackgroundColor(Color.argb(0, 0, 0, 0));

                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    if(rect != null && !rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                        finalConvertView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                        viewHolder.nexttime_layout.setBackgroundColor(Color.argb(0, 0, 0, 0));

                    }
                }
                return false;
            }
        });
        ViewGroup.MarginLayoutParams params_margin = (ViewGroup.MarginLayoutParams) line_top.getLayoutParams();
        params_margin.leftMargin = params_margin.rightMargin = params_margin.topMargin = params_margin.bottomMargin = (int) (size.x * 0.0266);

        viewHolder.imageView.getLayoutParams().width = (int) (size.x * 0.172);
        viewHolder.imageView.getLayoutParams().height = (int) (size.x * 0.244);
        final int padding = (int) (size.x * 0.0266);
        ViewGroup.MarginLayoutParams img_params_margin = (ViewGroup.MarginLayoutParams) viewHolder.imageView.getLayoutParams();
        img_params_margin.rightMargin = padding;

        final ViewGroup.MarginLayoutParams listTimeline_params_margin = (ViewGroup.MarginLayoutParams) viewHolder.listTimeline.getLayoutParams();
        listTimeline_params_margin.leftMargin = listTimeline_params_margin.bottomMargin = padding;
        final int list_element_width = (int) (size.x * 0.1466);
        viewHolder.listTimeline.getLayoutParams().height = list_element_width;// + list_element_spacing*2;
        ViewGroup.MarginLayoutParams btNextTime_margin = (ViewGroup.MarginLayoutParams) viewHolder.btNextTime.getLayoutParams();
        btNextTime_margin.height = (int) (size.x * 0.15) ;
        viewHolder.txtEventName.setText(dataModel.getProduct_name());
        Date startdate ;
        Date endDate ;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd(E)");
        startdate = new Date(dataModel.getStart_tour_period());
        endDate = new Date(dataModel.getEnd_tour_period());
        viewHolder.txtTime.setText(mContext.getString(R.string.event_item) + "：" + fmt.format(startdate) + " ~ " + fmt.format(endDate));
        viewHolder.txtVenue.setText(mContext.getString(R.string.booth) + "："+ dataModel.getVenue_name());
        if(dataModel.getSchedules() != null ) {
            viewHolder.listTimeline.setVisibility(View.VISIBLE);
            NewTimeAdapter timeAdapter = new NewTimeAdapter(dataModel.getSchedules(), mContext, new NewTimeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int item_position) {
                    SchedulesBean schedulesBean = dataModel.getSchedules().get(item_position);
                    if (schedulesBean.getStatus() != 4 && schedulesBean.getStatus() != 5 ) {
                        String url = ApiKey.DOMAIN + "/index.php?dispatch=products.view&" +
                                "scroll_panel=1" +
                                "&product_id=" + dataModel.getProduct_id() +
                                "&venue_id=" + dataModel.getVenue_id() +
                                //"&date_id=" + schedulesBean.getDate_id() +
                                "&time_id=" + schedulesBean.getTime_id();// +
                        //"&seat_type_id=" + schedulesBean.getSeat_type_id();
                        clickListener.onItemTimeClick(url);
                    }


                }
            });
            viewHolder.listTimeline.setAdapter(timeAdapter);
            timeAdapter.setOnBottomReachedListener(new NewTimeAdapter.OnBottomReachedListener() {
                @Override
                public void onBottomReached(Boolean bottom) {
                    if(bottom) {
                        viewHolder.nexttime_layout.setVisibility(View.GONE);
                        listTimeline_params_margin.leftMargin = padding;
                        listTimeline_params_margin.rightMargin = 0;
                        viewHolder.listTimeline.requestLayout();
                    }
                }
            });
            viewHolder.listTimeline.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if(newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        viewHolder.nexttime_layout.setVisibility(View.GONE);
                        //viewHolder.listTimeline.getLayoutParams().width = size.x;
                        ViewGroup.MarginLayoutParams listTimeline_params_margin = (ViewGroup.MarginLayoutParams) viewHolder.listTimeline.getLayoutParams();
                        listTimeline_params_margin.leftMargin = 0;
                    }
                    if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                        LinearLayoutManager llm = (LinearLayoutManager) viewHolder.listTimeline.getLayoutManager();
                        int lastVisibleItemPosition = llm.findLastVisibleItemPosition();
                        int firtVisibleItem = llm.findFirstVisibleItemPosition();

                        if(lastVisibleItemPosition == dataModel.getSchedules().size() - 1) {

                            if(dataModel.getSchedules().size() > 6)
                            {
                                llm.scrollToPosition(lastVisibleItemPosition);
                                listTimeline_params_margin.rightMargin = padding;
                            }
                            viewHolder.nexttime_layout.setVisibility(View.GONE);
                        } else {
                            viewHolder.nexttime_layout.setVisibility(View.VISIBLE);
                            listTimeline_params_margin.rightMargin = 0;
                        }
                        if(firtVisibleItem == 0)
                            listTimeline_params_margin.leftMargin = padding;
                        else  listTimeline_params_margin.leftMargin = 0;
                        viewHolder.listTimeline.requestLayout();
                    }
                }
            });


        } else {
            viewHolder.listTimeline.setVisibility(View.GONE);
        }

        if(dataModel.getMain_image() != null) {
            Glide.with(mContext)
                    .load(dataModel.getMain_image())
                    .placeholder(R.drawable.tp_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(viewHolder.imageView);
        }
        // Return the completed view to render on screen
        if(dataModel.getSchedules().size() > 6) viewHolder.nexttime_layout.setVisibility(View.VISIBLE);
        return convertView;
    }
    public void setOnItemTimeClickListener(ClickListener clickListener) {
        EventAdapter.clickListener = clickListener;
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        EventAdapter.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemTimeClick(String url);
        void onItemClick(int position);

    }
}
