package eplus.scrap.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eplus.scrap.R;
import eplus.scrap.model.TourDetail;

/**
 * Created by nals-anhdv on 7/24/17.
 */

public class ProductTabAdapter extends ArrayAdapter<TourDetail.TabsBean.ItemsBean> implements View.OnClickListener{
    Context mContext;
    private List<TourDetail.TabsBean.ItemsBean> dataSet;
    private int mPosition;

    public ProductTabAdapter(List<TourDetail.TabsBean.ItemsBean> data, Context context,int position) {
        super(context, R.layout.ticket_item, data);
        this.dataSet = data;
        this.mContext=context;
        mPosition = position;

    }
    @Override
    public void onClick(View v) {

    }

    @SuppressLint("NewApi")
    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TourDetail.TabsBean.ItemsBean dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag


        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.product_item, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.tv_tab_name);
            viewHolder.webView = convertView.findViewById(R.id.wv_tab);
            viewHolder.webView.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.lineView = convertView.findViewById(R.id.line_view);
            viewHolder.header = convertView.findViewById(R.id.img_header);
            viewHolder.footer = convertView.findViewById(R.id.img_footer);
            //viewHolder.webView.getSettings().setLoadWithOverviewMode(true);
            //viewHolder.webView.getSettings().setUseWideViewPort(true);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtName.setText(dataModel.getTitle());

        viewHolder.webView.getSettings().setJavaScriptEnabled(true);
        int pos = 1 ;
        if(mPosition <= 4) pos = mPosition;
        else pos = mPosition%4;

        String content = dataModel.getContent().trim().replaceAll("(\\r\\n|\\n|\\t)","");
        String regex = "<iframe.*?\\/iframe>";
        String replace = content.replaceAll(regex," ");
        content = replace.replaceAll("<p[^>]*>[\\s|&nbsp;]*<\\/p>","");
        StringBuilder sb = new StringBuilder();

        if(pos == 1) {
            if (position % 2 == 1) {
                viewHolder.header.setImageResource(R.drawable.product_tab_header);
                viewHolder.footer.setImageResource(R.drawable.product_footer_bg_new);
                convertView.setBackgroundColor(Color.parseColor("#c72931"));
                viewHolder.txtName.setTextColor(Color.WHITE);
                viewHolder.lineView.setBackgroundColor(Color.WHITE);
                sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"styles1.css\" /></HEAD><body>");
                viewHolder.header.setVisibility(View.VISIBLE);
                viewHolder.footer.setVisibility(View.VISIBLE);
            } else {
                viewHolder.txtName.setTextColor(Color.BLACK);
                viewHolder.header.setVisibility(View.GONE);
                viewHolder.footer.setVisibility(View.GONE);
                convertView.setBackgroundColor(Color.WHITE);
                viewHolder.lineView.setBackgroundColor(Color.BLACK);
                sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" /></HEAD><body>");
            }
        }else if (pos == 2) {
            if (position == 1) {
                viewHolder.header.setImageResource(R.drawable.ev30_item_header);
                viewHolder.footer.setImageResource(R.drawable.product_footer_bg_new);
                convertView.setBackgroundColor(Color.parseColor("#fff45c"));
                viewHolder.txtName.setTextColor(Color.BLACK);
                viewHolder.lineView.setBackgroundColor(Color.BLACK);
                sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" /></HEAD><body>");
                viewHolder.header.setVisibility(View.VISIBLE);
                viewHolder.footer.setVisibility(View.GONE);
            } else if (position > 1) {
                viewHolder.header.setImageResource(R.drawable.ev30_item_header);
                viewHolder.footer.setImageResource(R.drawable.product_footer_bg_new);
                convertView.setBackgroundColor(Color.parseColor("#fff45c"));
                viewHolder.txtName.setTextColor(Color.BLACK);
                viewHolder.lineView.setBackgroundColor(Color.BLACK);
                sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" /></HEAD><body>");
                viewHolder.header.setVisibility(View.GONE);
                viewHolder.footer.setVisibility(View.GONE);

            }
            else {
                sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" /></HEAD><body>");
            }
        } else if (pos == 3) {
            if (position % 2 == 1) {
                viewHolder.header.setImageResource(R.drawable.ev40_item_header);
                viewHolder.footer.setImageResource(R.drawable.product_footer_bg_new);
                convertView.setBackgroundColor(Color.parseColor("#c72931"));
                viewHolder.txtName.setTextColor(Color.WHITE);
                viewHolder.lineView.setBackgroundColor(Color.WHITE);
                sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"styles1.css\" /></HEAD><body>");
                viewHolder.header.setVisibility(View.VISIBLE);
                viewHolder.footer.setVisibility(View.VISIBLE);
            } else {
                sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" /></HEAD><body>");
            }
        } else if (pos == 4|| pos == 0) {
            sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" /></HEAD><body>");
        }
        if(position == dataSet.size() - 1 ) {
            if(pos == 1) {
                if (position % 2 == 0) {
                    ViewGroup.MarginLayoutParams params_title = (ViewGroup.MarginLayoutParams) viewHolder.webView.getLayoutParams();
                    params_title.bottomMargin = 100;
                }
            }else {
                ViewGroup.MarginLayoutParams params_title = (ViewGroup.MarginLayoutParams) viewHolder.webView.getLayoutParams();
                params_title.bottomMargin = 100;
            }
        }
        sb.append(content);
        sb.append("</body></HTML>");
        viewHolder.webView.loadDataWithBaseURL("file:///android_asset/", sb.toString(), "text/html", "UTF-8", null);

        if (position == 0) {
            Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            ViewGroup.MarginLayoutParams params_title = (ViewGroup.MarginLayoutParams) viewHolder.txtName.getLayoutParams();
            params_title.topMargin = (int) (size.x * 0.08667);

        }
        if(dataModel.getTitle().isEmpty()) {
            viewHolder.txtName.setVisibility(View.GONE);
            viewHolder.lineView.setVisibility(View.GONE);

        }
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        WebView webView;
        View lineView;
        ImageView header;
        ImageView footer;
    }
}
