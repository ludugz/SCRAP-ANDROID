package eplus.scrap.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import eplus.scrap.model.TourDetail;

/**
 * Created by nals-anhdv on 1/9/18.
 */

public class NewTimeAdapter extends RecyclerView.Adapter<NewTimeAdapter.ViewHolder> {
    private List<SchedulesBean> values;
    private Context myContext;
    OnBottomReachedListener onBottomReachedListener;
    private Point size = new Point();
    public interface OnBottomReachedListener {

        void onBottomReached(Boolean bottom);

    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private final OnItemClickListener listener;
    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView tvName;
        public TextView tvDaining;
        public ImageView img_status;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            tvName = v.findViewById(R.id.tv_time);
            tvDaining = v.findViewById(R.id.tv_daining);
            img_status = v.findViewById(R.id.tv_status_time);
            Display display = ((Activity)myContext).getWindowManager().getDefaultDisplay();

            display.getSize(size);
            ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
            layoutParams.width = layoutParams.height = (int) (size.x * 0.1466);

            int top_pading = (int) (size.x * 0.0226);
            LinearLayout lineMain = v.findViewById(R.id.line_time_main);
            lineMain.setPadding(top_pading,top_pading,top_pading,top_pading);

            img_status.getLayoutParams().width = img_status.getLayoutParams().height = (int) (size.x * 0.048);
            ViewGroup.MarginLayoutParams img_status_params = (ViewGroup.MarginLayoutParams) img_status.getLayoutParams();
            int spacing = (int) (size.x * 0.01233);
            img_status_params.topMargin = spacing;
            ViewGroup.MarginLayoutParams tvDaining_params = (ViewGroup.MarginLayoutParams) tvDaining.getLayoutParams();
            tvDaining_params.topMargin = spacing;
        }
        public void bind(final SchedulesBean item, final OnItemClickListener listener) {

            SimpleDateFormat sdf = new SimpleDateFormat(CommonFunc.DATE_FORMAT);

            Date dt;
            SimpleDateFormat formatter_to = new SimpleDateFormat(CommonFunc.HOUR_FORMAT);
            try {
                dt = sdf.parse(item.getDate_time()) ;

                tvName.setText(formatter_to.format(dt));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Drawable bg5 = myContext.getResources().getDrawable( R.drawable.backround_rect_status5 );
            Drawable bg12 = myContext.getResources().getDrawable( R.drawable.backround_rect_status1_2 );
            Drawable bg3 = myContext.getResources().getDrawable( R.drawable.backround_rect_status3 );
            Calendar c = Calendar.getInstance();
            tvDaining.setVisibility(View.GONE);


            if(item.getStatus() < 0) {
                ViewGroup.MarginLayoutParams img_status_params = (ViewGroup.MarginLayoutParams) img_status.getLayoutParams();

                int pading = (int) (size.x * 0.01233);
                img_status.getLayoutParams().width = img_status.getLayoutParams().height = (int) (size.x * 0.032);                int top_pading = (int) (size.x * 0.00633);
                img_status_params.topMargin = pading/8;

            }

            layout.setEnabled(true);
            switch (item.getStatus()) {
                case 1:
                    tvName.setTextColor(Color.WHITE);
                    img_status.setBackgroundResource(R.drawable.ic_circle_red);
                    itemView.setBackgroundDrawable(bg12);
                    break;
                case 2:
                    tvName.setTextColor(Color.WHITE);
                    img_status.setBackgroundResource(R.drawable.ic_triangle);
                    itemView.setBackgroundDrawable(bg12);
                    break;
                case 3:
                    tvName.setTextColor(Color.WHITE);
                    img_status.setBackgroundResource(R.drawable.x);
                    itemView.setBackgroundDrawable(bg3);
                    break;
                case 4:
                    tvName.setTextColor(Color.parseColor("#666666"));
                    img_status.setVisibility(View.GONE);
                    tvDaining.setText(R.string.members_in_advance);
                    tvDaining.setVisibility(View.VISIBLE);
                    itemView.setBackgroundDrawable(bg5);
                    break;
                case 5:
                    tvName.setTextColor(Color.parseColor("#666666"));
                    img_status.setBackgroundResource(R.drawable.ic_dash);
                    itemView.setBackgroundDrawable(bg5);
                    break;
                case 6:
                    tvName.setTextColor(Color.parseColor("#666666"));
                    img_status.setVisibility(View.GONE);
                    tvDaining.setText(R.string.in_preparation);
                    tvDaining.setVisibility(View.VISIBLE);
                    itemView.setBackgroundDrawable(bg5);
                    break;



            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(getAdapterPosition());
                }
            });

        }

    }
    public void add(int position, TourDetail item) {
//        values.add(position, item);
//        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewTimeAdapter(List<SchedulesBean> myDataset, Context context, OnItemClickListener listener) {
        values = myDataset;
        myContext = context;
        this.listener = listener;
    }
    @Override
    public NewTimeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.time_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(values.get(position), listener);
        if (position == values.size() - 1){

            onBottomReachedListener.onBottomReached(true);
            //Log.d("TimeLine","scroll bottom");

        } else {
            onBottomReachedListener.onBottomReached(false);
        }


        //holder.txtFooter.setText("Footer: " + name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }
}
