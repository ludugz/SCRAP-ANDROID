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
import eplus.scrap.model.Ticket;

/**
 * Created by nals-anhdv on 7/25/17.
 */

public class SeatAdapter extends ArrayAdapter<Ticket.SeatsBean> {
    private Context mContext;
    public SeatAdapter(Context context, List<Ticket.SeatsBean> routesArrayList) {
        super(context, 0, routesArrayList);
        mContext = context;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Ticket.SeatsBean seatsBean = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.seat_item, parent, false);
        }

        // Lookup view for data population
        TextView tv_seat_num = convertView.findViewById(R.id.tv_seat_num);
        tv_seat_num.setText(seatsBean.getSeat() + " " + seatsBean.getAmount());
        // Return the completed view to render on screen
        return convertView;
    }
}
