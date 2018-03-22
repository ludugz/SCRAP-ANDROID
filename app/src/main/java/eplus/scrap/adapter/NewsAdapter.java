package eplus.scrap.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import eplus.scrap.R;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.rss.MediaEnclosure;
import eplus.scrap.common.rss.RSSItem;

/**
 * Created by nals-anhdv on 7/24/17.
 */

public class NewsAdapter extends ArrayAdapter<RSSItem> implements View.OnClickListener{
    Context mContext;
    private ArrayList<RSSItem> dataSet;
    private int lastPosition = -1;

    public NewsAdapter(ArrayList<RSSItem> data, Context context) {
        super(context, R.layout.ticket_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
//        Object object= getItem(position);
//        NewsAdapter dataModel=(NewsAdapter)object;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RSSItem dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.new_item, parent, false);
            viewHolder.txtTitle = convertView.findViewById(R.id.tv_title);
            viewHolder.imageView = convertView.findViewById(R.id.img_thumbnail);
            viewHolder.txtDate = convertView.findViewById(R.id.tv_date);
            viewHolder.tvIsAttention = convertView.findViewById(R.id.tv_is_attention);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Date date = dataModel.getPubDate();
        viewHolder.txtDate.setText(CommonFunc.getDate(date.getTime(),"yyyy.MM.dd"));
        viewHolder.txtTitle.setText(dataModel.getTitle());
        if(dataModel.getIsAttention() == 1) {
            viewHolder.tvIsAttention.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvIsAttention.setVisibility(View.GONE);
        }
        if(dataModel.getEnclosure() != null) {
            MediaEnclosure mediaEnclosure = dataModel.getEnclosure();
//            Log.d("New image:",mediaEnclosure.getUrl().toString());
            String username = "tmc";
            String password = "tmc2017";
            String credentials = username + ":" + password;
            String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            LazyHeaders.Builder builder = new LazyHeaders.Builder()
                    .addHeader("Authorization", basic);

            GlideUrl glideUrl = new GlideUrl(mediaEnclosure.getUrl().toString(), builder.build());
            Glide.with(mContext)
                    .load(glideUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.imageView);
        } else {
            Random random = new Random();
            int number = random.nextInt(3);
            if(number == 0) viewHolder.imageView.setImageResource(R.drawable.rss_1);
            else if(number == 1)  viewHolder.imageView.setImageResource(R.drawable.rss_2);
            else viewHolder.imageView.setImageResource(R.drawable.rss_3);
        }
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtTitle;
        TextView txtDate;
        ImageView imageView;
        TextView tvIsAttention;
    }

}
