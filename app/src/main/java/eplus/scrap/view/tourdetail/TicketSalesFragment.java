package eplus.scrap.view.tourdetail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import eplus.scrap.R;
import eplus.scrap.adapter.VenueAdapter;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.WeekCalendar.WeekendAdapter;
import eplus.scrap.model.SchedulesBean;
import eplus.scrap.model.TourDetail;
import eplus.scrap.view.WebViewFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicketSalesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TicketSalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketSalesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String DATE_KEY = "date_key_abc";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btNextMonth;
    private Button btPreviosMonth;
    private int dateSelect_pos = 0;
    private HorizontalGridView gridView;

    private WeekendAdapter weekendAdapter;
    private TourDetail mTourDetail;
    private  ArrayList<Date> days;
    private ArrayList<SchedulesBean> schedulesBeanArrayList;
    private ListView listViewVenue;
    VenueAdapter venueAdapter;
    private TextView tvTicketRelease;
    private LinearLayout mLineContent;
    private FragmentActivity myContext;



    private OnFragmentInteractionListener mListener;

    public TicketSalesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TicketSalesFragment newInstance(TourDetail tourDetail) {
        TicketSalesFragment fragment = new TicketSalesFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, tourDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTourDetail = getArguments().getParcelable(ARG_PARAM1);
        }
        myContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ticket_sales, container, false);
        mLineContent = view.findViewById(R.id.line_content);
        tvTicketRelease = view.findViewById(R.id.tv__ticket_release);
        listViewVenue = view.findViewById(R.id.list_venue);
        gridView = view.findViewById(R.id.gridView);
        gridView.setHasFixedSize(true);

        init();
        btPreviosMonth = view.findViewById(R.id.bt_privios_month);
        btPreviosMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateSelect_pos - 30 >=0) {
                    dateSelect_pos = dateSelect_pos - 30;
                    weekendAdapter.notifyDataSetChanged();
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    gridView.getLayoutManager().scrollToPosition(llm.findLastVisibleItemPosition() + dateSelect_pos+1);
                }

            }
        });
        btNextMonth = view.findViewById(R.id.bt_next_month);
        btNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateSelect_pos + 30 < days.size()) {
                    dateSelect_pos = dateSelect_pos + 30;
                    weekendAdapter.notifyDataSetChanged();
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    gridView.getLayoutManager().scrollToPosition(llm.findLastVisibleItemPosition() + dateSelect_pos+1);
                }


            }
        });
        if(!ticketRelease()) {
            mLineContent.setVisibility(View.GONE);

            String text = "一般発売\n" + mTourDetail.getStart_selling_date_time()
                    + "～\n" +
                    "※お一人様最大6枚まで購入可能。ご希望の回が売り切れの場合はご容赦ください。\n";
            tvTicketRelease.setText(text);
            tvTicketRelease.setVisibility(View.VISIBLE);
        }else {
            mLineContent.setVisibility(View.VISIBLE);
            tvTicketRelease.setVisibility(View.GONE);
            if(mTourDetail.getSchedules() != null) {
                loadSchedule(mTourDetail.getStart_tour_period());
            }
        }



        return view;
    }
    private boolean ticketRelease() {
        DateFormat df1 = new SimpleDateFormat(CommonFunc.DATE_FORMAT);

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(mTourDetail.getStart_selling_date_time());
            date2 = new Date();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1.before(date2);

    }
    private void loadSchedule(String datestr){
        schedulesBeanArrayList = new ArrayList<SchedulesBean>();
        for(SchedulesBean schedulesBean : mTourDetail.getSchedules()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

            Date date_schedue = null;
            Date date = null;

            try {
                date_schedue = dateFormat .parse(schedulesBean.getDate_time());
                date = dateFormat.parse(datestr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(date.equals(date_schedue)) {
                schedulesBeanArrayList.add(schedulesBean);
            }

        }
        venueAdapter = new VenueAdapter(getCurrentContext(),schedulesBeanArrayList);
        listViewVenue.setAdapter(venueAdapter);
        listViewVenue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SchedulesBean schedulesBean= schedulesBeanArrayList.get(position);
                String url =    "https://scrap.nal.vn/index.php?dispatch=products.view&" +
                                "product_id=" + mTourDetail.getProduct_id() +
                                "&venue_id=" + mTourDetail.getVenue_id() +
                                //"&date_id=" + schedulesBean.getDate_id() +
                                "&time_id="+ schedulesBean.getTime_id() ;
                        //+ "&seat_type_id=" + schedulesBean.getSeat_type_id();
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                WebViewFragment fragment = new WebViewFragment().newInstance(new WebViewFragment.OnCompleteListener() {
                    @Override
                    public void onComplete() {

                    }
                }, url,true,false);
                fragment.show(ft, "webview");



            }
        });


    }
    private static ArrayList<Date> getDates(String dateString1, String dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat(CommonFunc.DATE_FORMAT_NONE_HOUR);

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(dateString1);
            date2 = df1 .parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        dates.add(cal.getTime());
        while (cal.getTime().before(date2)) {
            cal.add(Calendar.DATE, 1);
            dates.add(cal.getTime());
        }
        return dates;
    }
    private void init() {
        days = getDates(mTourDetail.getStart_tour_period(),mTourDetail.getEnd_tour_period());
//        DateTime midDate = new DateTime();
//        if (midDate != null) {
//            midDate = midDate.withDayOfWeek(DateTimeConstants.THURSDAY);
//        }
//        //Getting all seven days
//
//        for (int i = -3; i <= 30; i++)
//            days.add(midDate != null ? midDate.plusDays(i) : null);
//
//        startDate = days.get(0);
//        endDate = days.get(days.size() - 1);

        weekendAdapter = new WeekendAdapter(getActivity(), days,0);
        gridView.setAdapter(weekendAdapter);
        weekendAdapter.setOnItemClickListener(new WeekendAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
//                Toast.makeText(getActivity(), "Single Click on position :"+position,
//                        Toast.LENGTH_SHORT).show();
                dateSelect_pos = position;
                weekendAdapter.notifyDataSetChanged();
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                gridView.getLayoutManager().scrollToPosition(llm.findLastVisibleItemPosition() + position+1);
                if(days.size() > 0) {
                    Date date = days.get(position);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    loadSchedule(dateFormat.format(date));
                }


            }
            @Override
            public void onItemLongClick(int position, View v) {
            }
        });





//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                BusProvider.getInstance().post(new Event.OnDateClickEvent(weekendAdapter.getItem
//                        (position)));
//                selectedDateTime = weekendAdapter.getItem(position);
//                BusProvider.getInstance().post(new Event.InvalidateEvent());
//            }
//        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }
}
