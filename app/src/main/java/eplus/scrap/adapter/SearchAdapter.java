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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import eplus.scrap.R;
import eplus.scrap.model.TourDetail;

/**
 * Created by nals-anhdv on 7/24/17.
 */

public class SearchAdapter extends ArrayAdapter<TourDetail> implements View.OnClickListener{
    private ArrayList<TourDetail> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtEventName;
        ImageView imageView;
    }

    public SearchAdapter(ArrayList<TourDetail> data, Context context) {
        super(context, R.layout.event_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        SearchAdapter dataModel=(SearchAdapter)object;

        switch (v.getId())
        {
            case R.id.image:
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TourDetail dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        final View result;

        if (convertView == null) {

            final ViewHolder viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.search_item, parent, false);
            viewHolder.txtEventName = convertView.findViewById(R.id.tv_eventName);
            viewHolder.imageView = convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);

        }
        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
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

        viewHolder.txtEventName.setText(dataModel.getProduct_name());

        if(!dataModel.getMain_image().isEmpty()) {
            Glide.with(mContext)
                    .load(dataModel.getMain_image())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.place_holder_ct10)
                    .into(viewHolder.imageView);
        } else {
            viewHolder.imageView.setImageResource(R.drawable.place_holder_ct10);
        }

        return convertView;
    }
}
