package eplus.scrap.view.tourdetail;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import eplus.scrap.EventBus.GetTourDetailEvent;
import eplus.scrap.R;
import eplus.scrap.adapter.VenueAdapter;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.Helper;
import eplus.scrap.common.SharePreferences;
import eplus.scrap.common.WeekCalendar.WeekendAdapter;
import eplus.scrap.model.SchedulesBean;
import eplus.scrap.model.TourDetail;
import eplus.scrap.networking.ApiKey;
import eplus.scrap.networking.BaseRestClient;
import eplus.scrap.networking.DataResponse;
import eplus.scrap.view.WebViewFragment;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static eplus.scrap.networking.ApiKey.URL_GET_SCHEDULE_DATE;

public class BoxOfficeFragment extends Fragment implements View.OnClickListener, Updateable {
    private static final String TOUR_DETAIL = "tour_detail";
    private static final String DATE_SELECT = "date_select";
    VenueAdapter venueAdapter;
    Map<String, Integer> mapCalendar = new TreeMap<>();
    int idxMonth = 0;
    private TourDetail mTourDetail;
    private OnFragmentInteractionListener mListener;
    private Button btNextMonth;
    private Button btPreviosMonth;
    private int dateSelect_pos = 0;
    private HorizontalGridView gridView;
    private WeekendAdapter weekendAdapter;
    private ArrayList<Date> days;
    private ArrayList<SchedulesBean> schedulesBeanArrayList;
    private ListView listViewVenue;
    private TextView tvTicketRelease;
    private LinearLayout mLineContent;
    private ImageButton btLeftDay;
    private ImageButton btRigtDay;
    private WebView tvFullDes;
    private LinearLayoutManager layoutManager;
    private TextView textViewTitle;
    private ImageView img_thumb;
    private LinearLayout lineNodata;
    private LinearLayout firstBar;
    private FragmentActivity myContext;
    private ScrollView mainScrollView;
    private String mDateSelect;

    public BoxOfficeFragment() {
        // Required empty public constructor
    }
    public static BoxOfficeFragment newInstance(TourDetail tourDetail, String date_select) {
        BoxOfficeFragment fragment = new BoxOfficeFragment();
        Bundle args = new Bundle();
        if(tourDetail != null){
            args.putParcelable(TOUR_DETAIL, tourDetail);
        }
        args.putString(DATE_SELECT,date_select);
        fragment.setArguments(args);
        return fragment;
    }

    private static ArrayList<Date> getDates(String min_schedule, String max_schedule) {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat(CommonFunc.DATE_FORMAT_NONE_HOUR);

        Date maxDate = null;
        Date minDate = null;

        try {
            maxDate = df1.parse(max_schedule);
            minDate = df1.parse(min_schedule);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(minDate);
        dates.add(cal.getTime());
        if(minDate != null){
            while (minDate.before(maxDate)) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
                minDate = cal.getTime();
                dates.add(cal.getTime());
            }
        }
        if(dates.size() < 4) {
            for (int i = 0; i < 4; i++) {
                cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
                minDate = cal.getTime();
                dates.add(minDate);
            }
        }
        return dates;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if(getArguments().getParcelable(TOUR_DETAIL) != null){
                mTourDetail = getArguments().getParcelable(TOUR_DETAIL);
            }
            mDateSelect = getArguments().getString(DATE_SELECT);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_box_office, container, false);
        mainScrollView = view.findViewById(R.id.mainScrollView);
        firstBar = view.findViewById(R.id.channelsProgress);
        lineNodata = view.findViewById(R.id.line_nodata);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        textViewTitle = view.findViewById(R.id.tv_title);
        if(mTourDetail != null && !mTourDetail.getProduct_name().isEmpty())
            textViewTitle.setText(mTourDetail.getProduct_name());

        btLeftDay = view.findViewById(R.id.bt_left_day);
        btLeftDay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    btLeftDay.getDrawable().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            btLeftDay.getDrawable().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    btLeftDay.getDrawable().clearColorFilter();
                }
                return false;
            }
        });
        btLeftDay.setOnClickListener(this);
        btRigtDay = view.findViewById(R.id.bt_right_day);
        btRigtDay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    btRigtDay.getDrawable().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            btRigtDay.getDrawable().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    btRigtDay.getDrawable().clearColorFilter();
                }
                return false;
            }
        });
        btRigtDay.setOnClickListener(this);

        img_thumb = view.findViewById(R.id.image_thumbnail);

        if(mTourDetail != null && !mTourDetail.getMain_image().isEmpty()) {
            Glide.with(getActivity())
                    .load(mTourDetail.getMain_image())
                    .dontAnimate().dontTransform()
                    .placeholder(R.drawable.place_holder_ct10)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_thumb);
        }
        tvFullDes = view.findViewById(R.id.tv_full_des);
        tvFullDes.getSettings().setJavaScriptEnabled(true);

        mLineContent = view.findViewById(R.id.line_content);
        tvTicketRelease = view.findViewById(R.id.tv__ticket_release);
        listViewVenue = view.findViewById(R.id.list_venue);

        layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        gridView = view.findViewById(R.id.gridView);
        gridView.setHasFixedSize(true);
        gridView.setLayoutManager(layoutManager);
        gridView.addOnScrollListener(new HorizontalGridView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                LinearLayoutManager llm = (LinearLayoutManager) gridView.getLayoutManager();
                dateSelect_pos = layoutManager.findFirstVisibleItemPosition() ;
                Date date = days.get(dateSelect_pos);
                DateFormat df2 = new SimpleDateFormat("yyyyMM");


                String monthyear = df2.format(date);
                int index = 0;

                for (Object key : mapCalendar.keySet()) {
                    if(key.equals(monthyear)) break;
                    ++index;
                }
                idxMonth = index;
                setstatus();

                if(dateSelect_pos >= days.size() -5) dateSelect_pos = days.size() -1;
                llm.scrollToPosition(dateSelect_pos);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                setstatus();
            }

        });
        btPreviosMonth = view.findViewById(R.id.bt_privios_month);
        btPreviosMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayoutManager llm = (LinearLayoutManager) gridView.getLayoutManager();
                if(idxMonth - 1 >= 0) {
                    String key = (new ArrayList<>(mapCalendar.keySet())).get(idxMonth - 1);
                    if (mapCalendar.get(key) != null) {
                        int pos = mapCalendar.get(key)  ;
                        llm.scrollToPosition(pos);
                        idxMonth -= 1;
                        dateSelect_pos = pos;
                        weekendAdapter = new WeekendAdapter(getActivity(), days,dateSelect_pos);
                        gridView.setAdapter(weekendAdapter);
                        if (days.size() > 0) {
                            Date date = days.get(dateSelect_pos);

                            getSchedule(CommonFunc.getDate(date.getTime(),"yyyy/MM/dd"));
                        }
                    }
                }
                setstatus();


            }
        });
        RelativeLayout re_calendar = view.findViewById(R.id.re_calendar);
        re_calendar.getLayoutParams().height = (int) (width * 0.26);
        ViewGroup.MarginLayoutParams btLeftDay_params = (ViewGroup.MarginLayoutParams) btLeftDay.getLayoutParams();

        ViewGroup.MarginLayoutParams btRightDay_params = (ViewGroup.MarginLayoutParams) btRigtDay.getLayoutParams();
        btRightDay_params.rightMargin = btLeftDay_params.leftMargin = (int) (width * 0.0267);
        int width_schedule_bt = (int) (width * .188);
        int spacing_schedule_bt = (int) (width * 0.014);
        spacing_schedule_bt = (spacing_schedule_bt / 2);
        spacing_schedule_bt = spacing_schedule_bt * 2;
        gridView.getLayoutParams().width = width_schedule_bt*4 + spacing_schedule_bt*4;
        gridView.getLayoutParams().height = (int) (width * 0.1466);

        btLeftDay.getLayoutParams().width = btLeftDay.getLayoutParams().height = (int) (width * 0.06) ;
        btRigtDay.getLayoutParams().width = btRigtDay.getLayoutParams().height = (int) (width * 0.06) ;


        btNextMonth = view.findViewById(R.id.bt_next_month);
        btNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager llm = (LinearLayoutManager) gridView.getLayoutManager();
                String key  = (new ArrayList<>(mapCalendar.keySet())).get(idxMonth+1);
                if(mapCalendar.get(key) != null) {
                    int pos = mapCalendar.get(key);
                    if(pos + 3 < days.size() -1)
                    llm.scrollToPosition(pos);
                    else llm.scrollToPosition(days.size() -1);
                    idxMonth += 1;
                    dateSelect_pos = pos;
                    weekendAdapter = new WeekendAdapter(getActivity(), days,dateSelect_pos);
                    gridView.setAdapter(weekendAdapter);
                    if (days.size() > 0) {
                        Date date = days.get(dateSelect_pos);

                        getSchedule(CommonFunc.getDate(date.getTime(),"yyyy/MM/dd"));
                    }
                }
                setstatus();


            }
        });

        fillData(mTourDetail);

        return view;
    }
    public void fillData(TourDetail tourDetail) {
        mTourDetail = tourDetail;
        if(mTourDetail != null) {
            if(mTourDetail.getFull_description() != null) {

                StringBuilder sb = new StringBuilder();
                sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"styles1.css\" /></HEAD><body>");
                sb.append(mTourDetail.getFull_description());
                sb.append("</body></HTML>");
                Log.d("HTML",sb.toString());
                tvFullDes.loadDataWithBaseURL("file:///android_asset/", sb.toString(), "text/html", "UTF-8", null);
                tvFullDes.setBackgroundColor(Color.TRANSPARENT);

            }
        }
        init();
        setstatus();
        mainScrollView.scrollTo(0,0);
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void setstatus() {
        int tintGrayColor = ContextCompat.getColor(getContext(), android.R.color.darker_gray);
        int tintBlackColor = ContextCompat.getColor(getContext(), android.R.color.black);


        Drawable drawable_left = ContextCompat.getDrawable(getContext(), R.drawable.arrow_left_white);
        drawable_left.setBounds( 0, 0, drawable_left.getIntrinsicWidth(), drawable_left.getIntrinsicHeight());
        Drawable drawable_right = ContextCompat.getDrawable(getContext(), R.drawable.arrow_right_white);
        drawable_right.setBounds( 0, 0, drawable_left.getIntrinsicWidth(), drawable_left.getIntrinsicHeight());



        if(days == null || days.size() <= 0 || mapCalendar.size() <= 0) {
            btPreviosMonth.setEnabled(false);
            btPreviosMonth.setTextColor(Color.GRAY);
            btNextMonth.setEnabled(false);
            btNextMonth.setTextColor(Color.GRAY);
            btLeftDay.setEnabled(false);
            btLeftDay.setAlpha((float) 0.5);
            btRigtDay.setEnabled(false);
            btRigtDay.setAlpha((float) 0.5);
        } else {
            if (mapCalendar.size() == 1) {
                btPreviosMonth.setEnabled(false);
                btPreviosMonth.setTextColor(Color.GRAY);
                btNextMonth.setEnabled(false);
                btNextMonth.setTextColor(Color.GRAY);
                drawable_left = DrawableCompat.wrap(drawable_left);
                DrawableCompat.setTint(drawable_left.mutate(), tintGrayColor);
                btPreviosMonth.setCompoundDrawables(drawable_left, null, null, null);
                drawable_right = DrawableCompat.wrap(drawable_right);
                DrawableCompat.setTint(drawable_right.mutate(), tintGrayColor);
                btNextMonth.setCompoundDrawables(null, null, drawable_right, null);

            } else {
                if (idxMonth == 0) {
                    btPreviosMonth.setEnabled(false);
                    btPreviosMonth.setTextColor(Color.GRAY);
                    drawable_left = DrawableCompat.wrap(drawable_left);
                    DrawableCompat.setTint(drawable_left.mutate(), tintGrayColor);
                    btPreviosMonth.setCompoundDrawables(drawable_left, null, null, null);
                } else {
                    btPreviosMonth.setEnabled(true);
                    btPreviosMonth.setTextColor(Color.BLACK);
                    drawable_left = DrawableCompat.wrap(drawable_left);
                    DrawableCompat.setTint(drawable_left.mutate(), tintBlackColor);
                    btPreviosMonth.setCompoundDrawables(drawable_left, null, null, null);
                }
                if (idxMonth + 1 == mapCalendar.size()) {
                    btNextMonth.setEnabled(false);
                    btNextMonth.setTextColor(Color.GRAY);
                    drawable_right = DrawableCompat.wrap(drawable_right);
                    DrawableCompat.setTint(drawable_right.mutate(), tintGrayColor);
                    btNextMonth.setCompoundDrawables(null, null, drawable_right, null);
                } else {
                    btNextMonth.setEnabled(true);
                    btNextMonth.setTextColor(Color.BLACK);
                    drawable_right = DrawableCompat.wrap(drawable_right);
                    DrawableCompat.setTint(drawable_right.mutate(), tintBlackColor);
                    btNextMonth.setCompoundDrawables(null, null, drawable_right, null);
                }
            }
            if(days.size() <= 4) {
                btLeftDay.setEnabled(false);
                btLeftDay.setAlpha((float) 0.5);
                btRigtDay.setEnabled(false);
                btRigtDay.setAlpha((float) 0.5);
                return;
            }

            int first = layoutManager.findFirstVisibleItemPosition();
            if (first == 0) {
                btLeftDay.setEnabled(false);
                btLeftDay.setAlpha((float) 0.5);
            } else {
                btLeftDay.setEnabled(true);
                btLeftDay.setAlpha((float) 1.0);
            }
            int last = layoutManager.findLastVisibleItemPosition();
            if (last == days.size() - 1) {
                btRigtDay.setEnabled(false);
                btRigtDay.setAlpha((float) 0.5);
            } else {
                btRigtDay.setEnabled(true);
                btRigtDay.setAlpha((float) 1.0);
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        myContext=(FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.bt_left_day:
                previosDay();
                break;

            case R.id.bt_right_day:
                nextDay();
                break;
            default:
                break;



        }
    }

    private void previosDay() {
        LinearLayoutManager llm = (LinearLayoutManager) gridView.getLayoutManager();
        if(dateSelect_pos == days.size() -1) dateSelect_pos = layoutManager.findFirstVisibleItemPosition() -3;
        else if(layoutManager.findFirstVisibleItemPosition() - dateSelect_pos < 4)
            dateSelect_pos = layoutManager.findFirstVisibleItemPosition() - 4;
        else dateSelect_pos = layoutManager.findFirstVisibleItemPosition() -3;
        if(dateSelect_pos < 0) dateSelect_pos = 0;
        llm.scrollToPosition(dateSelect_pos);

        Date date = days.get(dateSelect_pos);
        DateFormat df2 = new SimpleDateFormat("yyyyMM");


        String monthyear = df2.format(date);
        int index = 0;

        for (Object key : mapCalendar.keySet()) {
            if(key.equals(monthyear)) break;
            ++index;
        }
        idxMonth = index;
        setstatus();
    }

    private void nextDay() {
        if(layoutManager.findLastVisibleItemPosition() - dateSelect_pos < 4)
            dateSelect_pos = layoutManager.findLastVisibleItemPosition() + 4;
        else dateSelect_pos = layoutManager.findLastVisibleItemPosition() +3;
        LinearLayoutManager llm = (LinearLayoutManager) gridView.getLayoutManager();
        if(dateSelect_pos > days.size() -1) dateSelect_pos = days.size() -1;
        llm.scrollToPosition(dateSelect_pos);
        Date date = days.get(dateSelect_pos);
        DateFormat df2 = new SimpleDateFormat("yyyyMM");
        String monthyear = df2.format(date);
        int index = 0;

        for (Object key : mapCalendar.keySet()) {
            if(key.equals(monthyear)) break;
            ++index;
        }
        idxMonth = index;
        setstatus();
    }

    private void updateSchedule() {
        if(schedulesBeanArrayList.size() >0 && myContext != null) {
            venueAdapter = new VenueAdapter( myContext, schedulesBeanArrayList);
            listViewVenue.setAdapter(venueAdapter);
            Helper.getListViewSize500(listViewVenue);

            listViewVenue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SchedulesBean schedulesBean = schedulesBeanArrayList.get(position);
                    if (schedulesBean.getStatus() != 4 && schedulesBean.getStatus() != 5 && schedulesBean.getStatus() != 6) {
                        String url = ApiKey.DOMAIN + "/index.php?dispatch=products.view&" +
                                "product_id=" + mTourDetail.getProduct_id() +
                                "&venue_id=" + mTourDetail.getVenue_id() +
                                "&scroll_panel=1" +
                                "&time_id=" + schedulesBean.getTime_id();
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);
                        WebViewFragment fragment =  WebViewFragment.newInstance(new WebViewFragment.OnCompleteListener() {
                            @Override
                            public void onComplete() {

                            }
                        }, url, true,false);
                        fragment.show(ft, "webview");
                    }
                }
            });
            lineNodata.setVisibility(View.GONE);
            listViewVenue.setVisibility(View.VISIBLE);
        } else {
            lineNodata.setVisibility(View.VISIBLE);
            listViewVenue.setVisibility(View.GONE);
        }
    }

    private void init() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        Date today = cal.getTime();
        if(mTourDetail != null && mTourDetail.getMin_schedule() != null && mTourDetail.getMax_schedule() != null && !mTourDetail.getMin_schedule().isEmpty() && ! mTourDetail.getMax_schedule().isEmpty()) {
            DateFormat df2 = new SimpleDateFormat("yyyyMM");
            days = getDates(mTourDetail.getMin_schedule(),mTourDetail.getMax_schedule());
            for (int key = 0; key < days.size(); key++) {

                Date newdate = days.get(key);
                String monthyear = df2.format(newdate);
                if (!mapCalendar.containsKey(monthyear)) {
                    mapCalendar.put(monthyear, key);
                }

            }

            //setstatus();
            if(mDateSelect.isEmpty()){
                mDateSelect = CommonFunc.getDate(today.getTime(),"yyyy/MM/dd");
            }
            for(int i = 0; i < days.size(); i ++){
                String date = CommonFunc.getDate(days.get(i).getTime(),"yyyy/MM/dd");
                if(mDateSelect.equals(date)) {
                    dateSelect_pos = i;
                    break;
                }
            }
            Date date = days.get(dateSelect_pos);
            getSchedule(CommonFunc.getDate(date.getTime(),"yyyy/MM/dd"));
            weekendAdapter = new WeekendAdapter(getActivity(), days,dateSelect_pos);
            gridView.setAdapter(weekendAdapter);
            weekendAdapter.notifyDataSetChanged();
            LinearLayoutManager llm = (LinearLayoutManager) gridView.getLayoutManager();
            llm.scrollToPosition(dateSelect_pos);
            weekendAdapter.setOnItemClickListener(new WeekendAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    dateSelect_pos = position;
                    weekendAdapter.notifyDataSetChanged();
                    if (days.size() > 0) {
                        Date date = days.get(position);
                        getSchedule(CommonFunc.getDate(date.getTime(),"yyyy/MM/dd"));
                    }
                }

                @Override
                public void onItemLongClick(int position, View v) {
                }
            });
            String monthyear = df2.format(date);
            int index = 0;
            for (Object key : mapCalendar.keySet()) {
                if(key.equals(monthyear)) break;
                ++index;
            }
            idxMonth = index;
            setstatus();
        } else {

            days = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
                today = cal.getTime();
                days.add(today);
            }
            weekendAdapter = new WeekendAdapter(getActivity(), days,dateSelect_pos);
            gridView.setAdapter(weekendAdapter);
            weekendAdapter.setOnItemClickListener(new WeekendAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    dateSelect_pos = position;
                    weekendAdapter.notifyDataSetChanged();
                    if (days.size() > 0) {
                        Date date = days.get(dateSelect_pos);
                        getSchedule(CommonFunc.getDate(date.getTime(),"yyyy/MM/dd"));
                    }


                }

                @Override
                public void onItemLongClick(int position, View v) {
                }
            });
            setstatus();
        }


    }

    private void getSchedule( String specify_date) {
        firstBar.setVisibility(View.VISIBLE);
        lineNodata.setVisibility(View.GONE);
        if(schedulesBeanArrayList != null && !schedulesBeanArrayList.isEmpty()) {
            schedulesBeanArrayList.clear();
            venueAdapter.notifyDataSetChanged();
        }

        HashMap<String, String> param = new HashMap<>();

        if(!specify_date.isEmpty()){
            param.put("specify_date", specify_date);
        }
        String token = SharePreferences.getStringPreference(myContext, "token");

        HashMap<String, String> hearder = new HashMap<>();
        if (!TextUtils.isEmpty(token)) {
            hearder.put("token", token);
        }
        DataResponse responseVeryfy = new DataResponse(getContext()) {
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getString("status").equals("200")) {
                        Boolean success = jObject.getBoolean("success");
                        if (success) {
                            Gson gson = new Gson();
                            JSONArray arrayEvent =  jObject.getJSONArray("data");
                            schedulesBeanArrayList = new ArrayList<>();
                            for(int i=0; i<arrayEvent.length(); i++){
                                JSONObject json_data = arrayEvent.getJSONObject(i);
                                SchedulesBean schedulesBean = gson.fromJson(json_data.toString(), SchedulesBean.class);
                                schedulesBeanArrayList.add(schedulesBean);
                            }
                            firstBar.setVisibility(View.GONE);
                            if(schedulesBeanArrayList.isEmpty()){
                                lineNodata.setVisibility(View.VISIBLE);
                                listViewVenue.setVisibility(View.GONE);
                            }
                            else {
                                updateSchedule();
                                lineNodata.setVisibility(View.GONE);
                                listViewVenue.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    super.onResponse(call, response);
                } else {
                    super.onFailure(call, new IOException());
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        };

        BaseRestClient.get(getActivity(), URL_GET_SCHEDULE_DATE + mTourDetail.getProduct_id(), param, hearder, responseVeryfy, false);
    }

    @Override
    public void update(TourDetail tourDetail) {
        fillData(tourDetail);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetTourDetailEvent event) {
        if(event.tourDetail != null) {
            fillData(event.tourDetail);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        //Đăng ký lắng nghe
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        //Hủy lắng ngh]\
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
