package eplus.scrap.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import eplus.scrap.R;
import eplus.scrap.model.TourDetail;

/**
 * Created by nals-anhdv on 1/9/18.
 */

public class NewSearchAdapter extends RecyclerView.Adapter<NewSearchAdapter.ViewHolder> {
    private List<TourDetail> values;
    private Context myContext;
    OnBottomReachedListener onBottomReachedListener;
    public interface OnBottomReachedListener {

        void onBottomReached();

    }
    public interface OnItemClickListener {
        void onItemClick(TourDetail item);
    }
    private final OnItemClickListener listener;
    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView txtHeader;
        public ImageView imgThumb;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = v.findViewById(R.id.tv_eventName);
            imgThumb = v.findViewById(R.id.image);
        }
        public void bind(final TourDetail item, final OnItemClickListener listener) {
            txtHeader.setText(item.getProduct_name());

            if(!item.getMain_image().isEmpty()) {
                Glide.with(myContext)
                        .load(item.getMain_image())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.place_holder_ct10)
                        .into(imgThumb);
            } else {
                imgThumb.setImageResource(R.drawable.place_holder_ct10);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
            itemView.setOnTouchListener(new View.OnTouchListener() {
                private Rect rect;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        imgThumb.setColorFilter(Color.argb(50, 0, 0, 0));
                        rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                // yourMethod();
                                imgThumb.setColorFilter(Color.argb(0, 0, 0, 0));
                            }
                        }, 1000);
                    }
                    else if(event.getAction() == MotionEvent.ACTION_UP){
                        imgThumb.setColorFilter(Color.argb(0, 0, 0, 0));
                    }
                    else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                        if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                            imgThumb.setColorFilter(Color.argb(0, 0, 0, 0));
                        }
                    }
                    else if(event.getAction() == MotionEvent.ACTION_MOVE){
                        if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                            imgThumb.setColorFilter(Color.argb(0, 0, 0, 0));
                        }
                    }
                    return false;
                }
            });
        }

    }
    public void add(int position, TourDetail item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewSearchAdapter(List<TourDetail> myDataset, Context context,OnItemClickListener listener) {
        values = myDataset;
        myContext = context;
        this.listener = listener;
    }
    @Override
    public NewSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.search_item, parent, false);
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

            onBottomReachedListener.onBottomReached();

        }


        //holder.txtFooter.setText("Footer: " + name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }
}
