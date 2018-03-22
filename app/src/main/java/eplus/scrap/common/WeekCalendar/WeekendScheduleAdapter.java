package eplus.scrap.common.WeekCalendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import eplus.scrap.R;

/**
 * Created by nals-anhdv on 8/8/17.
 */

public class WeekendScheduleAdapter extends RecyclerView.Adapter<WeekendScheduleAdapter.SimpleViewHolder> {
    private ArrayList<Date> days;
    private Context context;
    private static ClickListener clickListener;
    int selectedPosition=0;

    public WeekendScheduleAdapter(Context context, ArrayList<Date> days, int pos){
        this.context = context;
        this.days = days;
        this.selectedPosition = pos;
    }



    public static class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView dayofweek;
        public TextView dayTextView;

        public SimpleViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            dayofweek = view.findViewById(R.id.daytextofweek);
            dayTextView = view.findViewById(R.id.daytext);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);

        }
        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }

    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final View view = LayoutInflater.from(this.context).inflate(R.layout.day_item, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        //double itemWidth = (size.x * 0.71)/4 ;
        layoutParams.width = (int) (size.x * .188); //(int)itemWidth;
        layoutParams.height = (int) (size.x * 0.1466);
        ViewGroup.MarginLayoutParams params_margin = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int spacing = (int) (size.x * 0.014);
        spacing = spacing / 2;
        params_margin.leftMargin = params_margin.rightMargin = spacing;
        int pading_top = (int) (size.x * 0.016);
        int pading_bottom = (int) (size.x * 0.022);
        view.setPadding(0,pading_top,0,pading_bottom);
        //params_margin.topMargin = spacing;
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder,  int position) {
        final Drawable holoCircle = ContextCompat.getDrawable(context, R.drawable.holo_circle);
        final Drawable solidCircle = ContextCompat.getDrawable(context, R.drawable.solid_circle);
        if(selectedPosition==position)
            holder.itemView.setBackground(solidCircle);
        else
            holder.itemView.setBackground(holoCircle);
        Date dateTime = days.get(position);
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(dateTime);
        String[] names = DateFormatSymbols.getInstance(Locale.getDefault()).getShortWeekdays();
        String languageToLoad = Locale.getDefault().getLanguage();
        String day;
        if(languageToLoad.equals("ja")) day = names[calendar.get(Calendar.DAY_OF_WEEK)];
        else day = names[calendar.get(Calendar.DAY_OF_WEEK)] ;

        holder.dayofweek.setText(day);
        String monthNumber  = (String) DateFormat.format("MM",   dateTime); // 06
        holder.dayTextView.setText(String.valueOf("" + monthNumber + "/" + calendar.get(Calendar.DAY_OF_MONTH) ));
        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=pos;
                if(clickListener != null){
                    clickListener.onItemClick(pos, v);
                }
            }
        });
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        WeekendScheduleAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.days.size();
    }



}


