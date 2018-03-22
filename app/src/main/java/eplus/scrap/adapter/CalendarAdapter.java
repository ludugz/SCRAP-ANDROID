package eplus.scrap.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import eplus.scrap.R;

/**
 * Created by nals-anhdv on 7/25/17.
 */

public class CalendarAdapter extends ArrayAdapter<eplus.scrap.model.Calendar.EventDatesBean> {
    public CalendarAdapter(Context context, List<eplus.scrap.model.Calendar.EventDatesBean> routesArrayList) {
        super(context, 0, routesArrayList);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        eplus.scrap.model.Calendar.EventDatesBean eventDatesBean = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.day_item, parent, false);
        }
        // Lookup view for data population
        TextView dayofweek = convertView.findViewById(R.id.daytextofweek);
        TextView day = convertView.findViewById(R.id.daytext);
        // Populate the data into the template view using the data object
        //SimpleDateFormat sdf = new SimpleDateFormat(CommonFunc.DATE_FORMAT);
        // Return the completed view to render on screen
        dayofweek.setText("thu");
        return convertView;
    }
}
