package eplus.scrap.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
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
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.crashlytics.android.Crashlytics;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import eplus.scrap.EventBus.GetLinkPickupEvent;
import eplus.scrap.R;
import eplus.scrap.adapter.EventAdapter;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.Helper;
import eplus.scrap.common.InteractiveScrollView;
import eplus.scrap.common.SharePreferences;
import eplus.scrap.common.WeekCalendar.WeekendScheduleAdapter;
import eplus.scrap.model.Calendar;
import eplus.scrap.model.Pagination;
import eplus.scrap.model.Pickup;
import eplus.scrap.model.SchedulesBean;
import eplus.scrap.model.TourDetail;
import eplus.scrap.networking.ApiKey;
import eplus.scrap.networking.BaseRestClient;
import eplus.scrap.networking.DataResponse;
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollUpdateListener;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ScrollViewOverScrollDecorAdapter;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static eplus.scrap.networking.ApiKey.GET_LIMIT_DATE;
import static eplus.scrap.networking.ApiKey.ITEM_PER_PAGE;
import static eplus.scrap.networking.ApiKey.SEARCH_TOUR_URL;
import static eplus.scrap.networking.ApiKey.URL_UPDATE_STOCK;


public class ScheduleFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private static final String ARG_PARAM1 = "event_id";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<TourDetail> eventArrayLists = new ArrayList<>();
    ListView listView;
    private  EventAdapter adapter;
    private Button btNextMonth;
    private Button btPreviosMonth;
    private ImageView btNEWS;
    private FragmentActivity myContext;
    private LinearLayout lineNodata;
    private ImageView pickupLogo;
    private int dateSelect_pos = 0;
    private HorizontalGridView gridView;
    private WeekendScheduleAdapter weekendAdapter;
    private ArrayList<Date> days;
    private ImageButton btLeftDay;
    private ImageButton btRigtDay;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    private float move = 0;
    int idxMonth = 0;
    private  LinearLayoutManager layoutManager;
    Map<String, Integer> mapCalendar = new TreeMap();
    private LinearLayout firstBar;
    private InteractiveScrollView scrollView;
    private Pagination pagination;
    private boolean isLoadMore;
    private int height;
    private View mProgressBarFooter;
    private final DateFormat DATE_FORMAT_NONE_HOUR = new SimpleDateFormat(CommonFunc.DATE_FORMAT_NONE_HOUR);
    private RelativeLayout reNointernet;
    private LinearLayout lineBtRetry;
    private String event_id = "";
    private String link_pickup;
    SharedPreferences prefs = null;
    private VerticalOverScrollBounceEffectDecorator mVertOverScrollEffect;
    private ImageView smallBallon,midleBallon,bigBallon,cloud,star,moon,wind;
    private ImageView smallBallon2;
    private Button btToday;
    private ImageView viewBG;
    private RelativeLayout re_calendar;


    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance(String event_id, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, event_id);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void GetLinkPickupEvent(GetLinkPickupEvent event) {
        String link = event.link_image;
        link_pickup = event.url_topic;
        showPickup(link);

    }
    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            SharePreferences.saveStringPreference(myContext,"pickup_image",CommonFunc.bitmapEncodeTobase64(bitmap));
            pickupLogo.setImageBitmap(bitmap);
        }
    };
    private void showPickup(String link) {
        String username = "tmc";
        String pass = "tmc2017";
        String credentials = username + ":" + pass;
        String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        LazyHeaders.Builder builder = new LazyHeaders.Builder()
                .addHeader("Authorization", basic);

        GlideUrl glideUrl = new GlideUrl(link, builder.build());
        Glide.with(myContext)
                .load(glideUrl)
                .asBitmap()
                .skipMemoryCache( true )
                .placeholder(R.drawable.my10_thumbnail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_pickup)

                .into(target);
    }

    @Override
    public void onStart() {
        super.onStart();
        //Đăng ký lắng nghe
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        //Hủy lắng nghe
        stoptimertask();
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event_id = getArguments().getString(ARG_PARAM1);
        }
        myContext = getActivity();
        prefs = myContext.getSharedPreferences(myContext.getPackageName(), Context.MODE_PRIVATE);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        scrollView = view.findViewById(R.id.scrollView);
        viewBG = view.findViewById(R.id.view_bg);
        initVerticalScrollView(scrollView);
        scrollView.setOnBottomReachedListener(new InteractiveScrollView.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                if (pagination != null) {
                    if (isLoadMore) {
                        Date date = days.get(dateSelect_pos);
                        searchtourmore( CommonFunc.getDate(date.getTime(),"yyyy/MM/dd"),"","","","",true);
                    }
                }
            }
        });
        reNointernet = view.findViewById(R.id.re_no_internet_layout);
        lineBtRetry = view.findViewById(R.id.line_bt_retry);
        lineBtRetry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    lineBtRetry.getBackground().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            lineBtRetry.getBackground().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    lineBtRetry.getBackground().clearColorFilter();
                    if(CommonFunc.isNetworkConnected(myContext)) {

                            fetchPickup();
                            Date date_start = null;
                            Date date_end = null;
                            try {
                                date_start = DATE_FORMAT_NONE_HOUR.parse(SharePreferences.getStringPreference(getContext(),"limit_startDate"));
                                date_end = DATE_FORMAT_NONE_HOUR.parse(SharePreferences.getStringPreference(getContext(),"limit_endDate"));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(date_end != null) {
                                initDays(date_start,date_end);
                            } else {
                                getStartEndTime();
                            }
                        reNointernet.setVisibility(View.GONE);
                    }


                }
                return true;
            }
        });

        initView(view);
        if(CommonFunc.isNetworkConnected(myContext)) {
            fetchPickup();
            if (days != null && days.size() > 0) {
                updateCalendar();
                //dateSelect_pos = 0;
                if (days.size() > 0 && eventArrayLists == null) {
                    Date date = days.get(dateSelect_pos);
                    lineNodata.setVisibility(View.INVISIBLE);
                    searchtour( CommonFunc.getDate(date.getTime(), "yyyy/MM/dd"), "", "", "", "");
                }
            } else {
                Date date_start = null;
                Date date_end = null;

                try {
                    date_start = DATE_FORMAT_NONE_HOUR.parse(SharePreferences.getStringPreference(getContext(), "limit_startDate"));
                    date_end = DATE_FORMAT_NONE_HOUR.parse(SharePreferences.getStringPreference(getContext(), "limit_endDate"));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date_end != null) {
                    initDays(date_start, date_end);
                } else {
                    getStartEndTime();
                }
            }
        } else {
            reNointernet.setVisibility(View.VISIBLE);
        }
        setstatus();
        if(!event_id.isEmpty()) {
            showEvent(event_id);
        }

        return view;
    }

    @SuppressLint("RestrictedApi")
    private void initView(View view) {
        btToday = view.findViewById(R.id.bt_today);
        btToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadCalendar_today();
            }
        });
        smallBallon = view.findViewById(R.id.small_ballon);
        smallBallon2 = view.findViewById(R.id.small_ballon2);
        midleBallon = view.findViewById(R.id.midle_ballon);
        bigBallon = view.findViewById(R.id.big_ballon);
        cloud = view.findViewById(R.id.cloud);
        star = view.findViewById(R.id.start);
        moon = view.findViewById(R.id.moon);
        wind = view.findViewById(R.id.wind);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated
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
        btRigtDay.setOnClickListener(this);
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



        pickupLogo = view.findViewById(R.id.picup_logo);
        if (!SharePreferences.getStringPreference(myContext,"pickup_image").isEmpty()){
            String strBitmap = SharePreferences.getStringPreference(myContext,"pickup_image");
            Bitmap bitmap = CommonFunc.bitmapDecodeBase64(strBitmap);
            pickupLogo.setImageBitmap(bitmap);
        }
            pickupLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(CommonFunc.COLOR_HIGHLIGH, PorterDuff.Mode.MULTIPLY);
                    pickupLogo.getDrawable().setColorFilter(greyFilter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            pickupLogo.getDrawable().clearColorFilter();
                        }
                    }, 1000);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    pickupLogo.getDrawable().clearColorFilter();
                }
                return false;
            }
        });
        pickupLogo.getLayoutParams().height = (int) (width * 0.456);
        ViewGroup.MarginLayoutParams pickupLogo_params = (ViewGroup.MarginLayoutParams) pickupLogo.getLayoutParams();
        pickupLogo_params.topMargin = pickupLogo_params.leftMargin = pickupLogo_params.rightMargin = pickupLogo_params.bottomMargin = (int) (width * 0.02133);
        pickupLogo.setOnClickListener(this);

        TextView textView_pickup_logo = view.findViewById(R.id.tv_pickup);
        //textView_pickup_logo.getLayoutParams().width = (int) (width * 0.456);
        ViewGroup.MarginLayoutParams pickuptext_params = (ViewGroup.MarginLayoutParams) textView_pickup_logo.getLayoutParams();
        pickuptext_params.topMargin = 0;
        pickuptext_params.bottomMargin = pickuptext_params.leftMargin = pickuptext_params.rightMargin = (int) (width * 0.02133) * 2;
        //textView_pickup_logo.setTextSize((float) (width * 0.036));

        RelativeLayout relativeLayout_header_calendar = view.findViewById(R.id.re_bt_layout);
        relativeLayout_header_calendar.getLayoutParams().height = (int) (width * 0.1333);


        lineNodata = view.findViewById(R.id.line_nodata);

        btNEWS = view.findViewById(R.id.bt_news);
        btNEWS.setOnClickListener(this);
        int dimensionInPixel = (int) (width * 0.168);
        int margin = (int) (width * 0.0426);

        btNEWS.getLayoutParams().height = dimensionInPixel;
        btNEWS.getLayoutParams().width = dimensionInPixel;
        btNEWS.requestLayout();

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) btNEWS.getLayoutParams();
        params.rightMargin = margin;
        params.topMargin = margin;

        ImageView img_header = view.findViewById(R.id.my_awesome_toolbar);
        //img_header.bringToFront();
        img_header.getLayoutParams().width = width;
        img_header.getLayoutParams().height =  (int) (width * 0.4173);

        ImageView img_background_blackwhite = view.findViewById(R.id.img_bw_bg);
        img_background_blackwhite.getLayoutParams().width = width;
        //img_background_blackwhite.getLayoutParams().height = (int) (width * 1.58);

        LinearLayout line_pickup_background = view.findViewById(R.id.line_pickup_background);
        line_pickup_background.getLayoutParams().width = (int) (width * 0.8933);
        ViewGroup.MarginLayoutParams pickup_params = (ViewGroup.MarginLayoutParams) line_pickup_background.getLayoutParams();
        pickup_params.topMargin = ((int) (width * 0.0266));
        pickup_params.bottomMargin = ((int) (width * 0.0266));

        re_calendar = view.findViewById(R.id.re_calendar);
        re_calendar.getLayoutParams().height = (int) (width * 0.26);
        ViewGroup.MarginLayoutParams btLeftDay_params = (ViewGroup.MarginLayoutParams) btLeftDay.getLayoutParams();

        ViewGroup.MarginLayoutParams btRightDay_params = (ViewGroup.MarginLayoutParams) btRigtDay.getLayoutParams();
        btRightDay_params.rightMargin = btLeftDay_params.leftMargin = (int) (width * 0.0267);

        btLeftDay.getLayoutParams().width = btLeftDay.getLayoutParams().height = (int) (width * 0.06) ;
        btRigtDay.getLayoutParams().width = btRigtDay.getLayoutParams().height = (int) (width * 0.06) ;


        listView = view.findViewById(R.id.listViewEvent);
        listView.setBackgroundColor(Color.WHITE);
        mProgressBarFooter = ((LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.progress_bar, null, false);


        //
        firstBar = view.findViewById(R.id.channelsProgress);
        //listView.addFooterView(bottomBar, null, false);
        // calendar
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        gridView = view.findViewById(R.id.gridView);
        //gridView.getLayoutParams().width = (int) (width * .188);

        LinearLayout re_status = view.findViewById(R.id.re_status);
        re_status.getLayoutParams().height = (int) (width * 0.0853);

        ImageView img_cicrle_red = view.findViewById(R.id.img_circle_red);
        ImageView img_triangle = view.findViewById(R.id.img_triangle);
        ImageView img_x = view.findViewById(R.id.img_x);
        img_cicrle_red.getLayoutParams().height = img_triangle.getLayoutParams().height = img_x.getLayoutParams().height = (int) (re_status.getLayoutParams().height * 0.5);
        img_cicrle_red.getLayoutParams().width = img_triangle.getLayoutParams().width = img_x.getLayoutParams().width = (int) (re_status.getLayoutParams().height * 0.5);



        int width_schedule_bt = (int) (width * .188);
        int spacing_schedule_bt = (int) (width * 0.014);
        spacing_schedule_bt = spacing_schedule_bt / 2;
        spacing_schedule_bt = spacing_schedule_bt * 2;
        gridView.getLayoutParams().width = width_schedule_bt*4 + spacing_schedule_bt*4;
        gridView.getLayoutParams().height = (int) (width * 0.1466);// + spacing_schedule_bt * 2;

        gridView.setHasFixedSize(true);
        gridView.setLayoutManager(layoutManager);
        gridView.setSaveChildrenLimitNumber(4);
        gridView.addOnScrollListener(new HorizontalGridView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (days != null && days.size() > 0) {
                        LinearLayoutManager llm = (LinearLayoutManager) gridView.getLayoutManager();
                        dateSelect_pos = layoutManager.findFirstVisibleItemPosition();
                        Date date = days.get(dateSelect_pos);
                        DateFormat df2 = new SimpleDateFormat("yyyyMM");
                        String monthyear = df2.format(date);
                        int index = 0;
                        for (Object key : mapCalendar.keySet()) {
                            if (key.equals(monthyear)) break;
                            ++index;
                        }
                        idxMonth = index;
                        setstatus();

                        if (dateSelect_pos >= days.size() - 5) dateSelect_pos = days.size() - 1;
                        llm.scrollToPosition(dateSelect_pos);
                        updateCalendar();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int first = layoutManager.findFirstVisibleItemPosition();
                if(first == 0) {
                    btLeftDay.setEnabled(false);
                    btLeftDay.setAlpha((float) 0.5);
                }else {
                    btLeftDay.setEnabled(true);
                    btLeftDay.setAlpha((float) 1.0);
                }
                int last = layoutManager.findLastVisibleItemPosition();
                if(last == days.size() -1) {
                    btRigtDay.setEnabled(false);
                    btRigtDay.setAlpha((float) 0.5);
                }else {
                    btRigtDay.setEnabled(true);
                    btRigtDay.setAlpha((float) 1.0);
                }


            }

        });
        btPreviosMonth = view.findViewById(R.id.bt_privios_month);
        btPreviosMonth.getLayoutParams().height = (int) (relativeLayout_header_calendar.getLayoutParams().height * 0.8);
        ViewGroup.MarginLayoutParams params_btPreviosMonth = (ViewGroup.MarginLayoutParams) btPreviosMonth.getLayoutParams();
        params_btPreviosMonth.leftMargin = (int) (width * 0.0533);
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
                        weekendAdapter = new WeekendScheduleAdapter(myContext, days,dateSelect_pos);
                        gridView.setAdapter(weekendAdapter);
                        weekendAdapter.notifyDataSetChanged();
                        if (days.size() > 0) {
                            Date date = days.get(dateSelect_pos);
                            searchtour( CommonFunc.getDate(date.getTime(),"yyyy/MM/dd"),"","","","");
                        }
                    }
                }
                setstatus();
                updateCalendar();


            }
        });
        btNextMonth = view.findViewById(R.id.bt_next_month);
        btNextMonth.getLayoutParams().height = (int) (relativeLayout_header_calendar.getLayoutParams().height * 0.8);
        ViewGroup.MarginLayoutParams params_btNextMonth = (ViewGroup.MarginLayoutParams) btNextMonth.getLayoutParams();
        params_btNextMonth.rightMargin = (int) (width * 0.0533);
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
                    weekendAdapter = new WeekendScheduleAdapter(myContext, days,dateSelect_pos);
                    gridView.setAdapter(weekendAdapter);
                    weekendAdapter.notifyDataSetChanged();
                    if (days.size() > 0) {
                        Date date = days.get(dateSelect_pos);
                        searchtour( CommonFunc.getDate(date.getTime(),"yyyy/MM/dd"),"","","","");
                    }
                }
                updateCalendar();
                setstatus();


            }
        });
        if(eventArrayLists != null && eventArrayLists.size() > 0 ) updateData();
    }
    private void initVerticalScrollView(ScrollView scrollView) {
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        mVertOverScrollEffect = new VerticalOverScrollBounceEffectDecorator(new ScrollViewOverScrollDecorAdapter(scrollView));
        mVertOverScrollEffect.setOverScrollUpdateListener(new IOverScrollUpdateListener() {
            float offsetOld = 0;
            @Override
            public void onOverScrollUpdate(IOverScrollDecor decor, int state, float offset) {
                if(offset != offsetOld )
                    offsetOld = offset;
                if (offset > 0) {
                    moveItem(viewBG, offsetOld );
                    moveItem(smallBallon, offsetOld / 6);
                    moveItem(smallBallon2, offsetOld / 6);
                    moveItem(midleBallon, -offsetOld / 10);
                    moveItem(bigBallon, -offsetOld * 2 / 3);
                    moveItem(cloud, offsetOld / 3);
                    moveItem(star, offsetOld / 4);
                    moveItem(wind, offsetOld / 4);
                    moveItem(moon, offsetOld / 4);
                }

                }
        });
    }
    private void moveItem(final View v, final float moveY) {
        v.setTranslationY(moveY);

    }
    private void getStartEndTime() {
        HashMap<String, String> param = new HashMap<>();
        param.put("mode","start_end_calendar");

        DataResponse responseVeryfy = new DataResponse(myContext) {
            @Override
            public void onRealFail() {
                reNointernet.setVisibility(View.VISIBLE);
            }
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getString("status").equals("200")) {
                        Boolean success = jObject.getBoolean("success");
                        if (success) {
                            JSONObject data =  jObject.getJSONObject("data");
                            SharePreferences.saveStringPreference(myContext,"limit_startDate", data.getString("start"));
                            SharePreferences.saveStringPreference(myContext,"limit_endDate",data.getString("end"));
                            Date date_start = null;
                            Date date_end = null;


                            try {
                                date_start = DATE_FORMAT_NONE_HOUR.parse(data.getString("start"));
                                date_end = DATE_FORMAT_NONE_HOUR.parse(data.getString("end"));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            initDays(date_start,date_end);

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

        BaseRestClient.get(getContext(), GET_LIMIT_DATE, param, null, responseVeryfy, false);
    }
    private void initDays(Date date_start, Date date_end) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        Date today = cal.getTime();
        days = new ArrayList<>();
        if(date_end != null) {

            cal.setTime(date_start);
            days.add(cal.getTime());

            while (today.before(date_end))
            {
                cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
                today = cal.getTime();
                days.add(today);
            }
        } else {
            for (int i = 0; i < 4; i++) {
                cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
                today = cal.getTime();
                days.add(today);
            }
        }
        if(days.size() > 0)
        loadCalendar_today();

    }

    private void setstatus() {
        int tintGrayColor = ContextCompat.getColor(myContext, android.R.color.darker_gray);
        int tintWhiteColor = ContextCompat.getColor(myContext, android.R.color.white);


        Drawable drawable_left = ContextCompat.getDrawable(myContext, R.drawable.arrow_left_white);
        drawable_left.setBounds( 0, 0, drawable_left.getIntrinsicWidth(), drawable_left.getIntrinsicHeight());
        Drawable drawable_right = ContextCompat.getDrawable(myContext, R.drawable.arrow_right_white);
        drawable_right.setBounds( 0, 0, drawable_left.getIntrinsicWidth(), drawable_left.getIntrinsicHeight());



        if(days == null || days.size() <= 0) {
            btPreviosMonth.setEnabled(false);
            btPreviosMonth.setTextColor(Color.GRAY);
            btNextMonth.setEnabled(false);
            btNextMonth.setTextColor(Color.GRAY);
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
                    btPreviosMonth.setTextColor(Color.WHITE);
                    drawable_left = DrawableCompat.wrap(drawable_left);
                    DrawableCompat.setTint(drawable_left.mutate(), tintWhiteColor);
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
                    btNextMonth.setTextColor(Color.WHITE);
                    drawable_right = DrawableCompat.wrap(drawable_right);
                    DrawableCompat.setTint(drawable_right.mutate(), tintWhiteColor);
                    btNextMonth.setCompoundDrawables(null, null, drawable_right, null);
                }
            }
            LinearLayoutManager llm = (LinearLayoutManager) gridView.getLayoutManager();
            int first = llm.findFirstVisibleItemPosition();
            if (first == 0) {
                btLeftDay.setEnabled(false);
                btLeftDay.setAlpha((float) 0.5);
            } else {
                btLeftDay.setEnabled(true);
                btLeftDay.setAlpha((float) 1.0);
            }
            int last = llm.findLastVisibleItemPosition();
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
    public void onPause() {
        super.onPause();
//        Log.e("view:","onPause");
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 3000ms the TimerTask will run every 3000ms
        timer.schedule(timerTask, 300000, 300000); //
    }

    private void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        updateStock();
                    }
                });
            }
        };
    }

    private void updateStock() {
        //Toast.makeText(getContext(), "UPDATE STOCK", Toast.LENGTH_SHORT).show();
        //Log.d("UPDATE STOCK",timer.toString());
        if(days != null && days.size() > 0 && eventArrayLists != null && eventArrayLists.size() > 0) {
            HashMap<String, String> param = new HashMap<>();
            Date date = days.get(dateSelect_pos);
            param.put("specify_date", CommonFunc.getDate(date.getTime(), "yyyy/MM/dd"));
            for (int i = 0; i < eventArrayLists.size(); i++) {
                TourDetail product = eventArrayLists.get(i);
                param.put("product_ids[" + i + "]", "" + product.getProduct_id());
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
                                JSONArray arrayEvent = jObject.getJSONArray("data");
                                Map<String, List<SchedulesBean>> map = new HashMap<String, List<SchedulesBean>>();
                                for (int i = 0; i < arrayEvent.length(); i++) {
                                    JSONObject json_data = arrayEvent.getJSONObject(i);
                                    String key = json_data.getString("product_id");
                                    JSONArray arraySchedule = json_data.getJSONArray("schedules");
                                    ArrayList<SchedulesBean> schedulesBeanArrayList = new ArrayList<>();
                                    for (int j = 0; j < arraySchedule.length(); j++) {
                                        JSONObject jsonObject = arraySchedule.getJSONObject(j);
                                        Gson gson = new Gson();
                                        SchedulesBean schedulesBean = gson.fromJson(jsonObject.toString(), SchedulesBean.class);
                                        schedulesBeanArrayList.add(schedulesBean);
                                    }
                                    map.put(key, schedulesBeanArrayList);
                                }
                                for (TourDetail product : eventArrayLists) {
                                    List<SchedulesBean> schedulesBeans = map.get("" + product.getProduct_id());
                                    if (schedulesBeans != null)
                                        product.setSchedules(schedulesBeans);

                                }
                                lineNodata.setVisibility(View.INVISIBLE);
                                updateData();

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

            BaseRestClient.get(getActivity(), URL_UPDATE_STOCK, param, null, responseVeryfy, false);
        }
    }

    private void updateData() {
        adapter= new EventAdapter(eventArrayLists,getContext());
        listView.setAdapter(adapter);
        Helper.getListViewSize(listView,height);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ImageView img = (ImageView) view.findViewById(R.id.imageView) ;
                //Bitmap bitmap = img.getDrawingCache();
                //App appState = ((App)myContext.getApplicationContext());
                //appState.setMyBitmap(bitmap);
                TourDetail event= eventArrayLists.get(position);
                showTourDetail(event);
                //getTourDetail(String.valueOf(event.getProduct_id()));


            }
        });
        adapter.setOnItemTimeClickListener(new EventAdapter.ClickListener() {
            @Override
            public void onItemTimeClick(String url) {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    Fragment prev = myContext.getSupportFragmentManager().findFragmentByTag("webview");
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

            @Override
            public void onItemClick(int position) {
                TourDetail event= eventArrayLists.get(position);
                showTourDetail(event);
            }
        });


    }


    private void searchtourmore( String specify_date, String date_from, String date_to, String key_word, String show_type, boolean loadMore ) {
        String token = SharePreferences.getStringPreference(myContext, "token");

        HashMap<String, String> hearder = new HashMap<>();
        if (!token.isEmpty()) {
            hearder.put("token", token);
        }
        HashMap<String, String> param = new HashMap<>();
        listView.addFooterView(mProgressBarFooter);
        if(!specify_date.isEmpty()){
            param.put("specify_date", specify_date);
        }
        if(!date_from.isEmpty()){
            param.put("date_from", date_from);
        }
        if(!date_to.isEmpty()){
            param.put("date_to", date_to);
        }
        if(!key_word.isEmpty()){
            param.put("keyword", key_word );
        }
        if(!show_type.isEmpty()){
            param.put("show_type",show_type);
        }
        int page = pagination.getPage() + 1;
        param.put("page", "" + page);


        param.put("items_per_page",ITEM_PER_PAGE);

        DataResponse responseVeryfy = new DataResponse(getContext()) {
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
                try {

                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getString("status").equals("200")) {
                        Gson gson = new Gson();
                        Boolean success = jObject.getBoolean("success");
                        if (success) {
                            JSONObject pagine = jObject.getJSONObject("params");
                            pagination = gson.fromJson(pagine.toString(), Pagination.class);
                            JSONArray arrayEvent =  jObject.getJSONArray("data");
                            ArrayList<TourDetail> arrayList = new ArrayList<>();
                            for(int i=0; i<arrayEvent.length(); i++){
                                JSONObject json_data = arrayEvent.getJSONObject(i);

                                TourDetail event = gson.fromJson(json_data.toString(), TourDetail.class);
                                arrayList.add(event);
                            }
                            listView.removeFooterView(mProgressBarFooter);
                            if(arrayList.isEmpty()) {
                                isLoadMore = false;
                            }
                            else {
                                isLoadMore = true;
                                adapter.addAll(arrayList);
                                adapter.notifyDataSetChanged();
                                Helper.getListViewSize(listView,height);

                            }
                        }
                    }else{
                        isLoadMore = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
            }
            @Override
            public void onRealFail() {
                listView.removeFooterView(mProgressBarFooter);
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
                //bottomBar .setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        };
        BaseRestClient.get(getActivity(), SEARCH_TOUR_URL, param, hearder, responseVeryfy, false);
    }
    private void searchtour(String specify_date, String date_from, String date_to, String key_word, String show_type) {
        firstBar.setVisibility(View.VISIBLE);
        isLoadMore = false;
        if(eventArrayLists != null && !eventArrayLists.isEmpty()) {
            eventArrayLists.clear();
            listView.setVisibility(View.GONE);
//            final int scroll_pos = scrollView.getScrollY();
//            scrollView.post(new Runnable() {
//                @Override
//                public void run() {
//                    scrollView.scrollTo(0, scroll_pos);
//                }
//            });
            //adapter.notifyDataSetChanged();
            //scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
        String token = SharePreferences.getStringPreference(myContext, "token");

        HashMap<String, String> hearder = new HashMap<>();
        if (!token.isEmpty()) {
            hearder.put("token", token);
        }
//        Log.d(TAG,"load schedule");
        HashMap<String, String> param = new HashMap<>();

        if(!specify_date.isEmpty()){
            param.put("specify_date", specify_date);
        }
        if(!date_from.isEmpty()){
            param.put("date_from", date_from);
        }
        if(!date_to.isEmpty()){
            param.put("date_to", date_to);
        }
        if(!key_word.isEmpty()){
            param.put("key_word", key_word );
        }
        if(!show_type.isEmpty()){
            param.put("show_type",show_type);
        }
        param.put("items_per_page",ITEM_PER_PAGE);
        DataResponse responseVeryfy = new DataResponse(getContext()) {
            @Override
            public void onRealFail() {
                reNointernet.setVisibility(View.VISIBLE);
            }
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
                            eventArrayLists = new ArrayList<>();
                            JSONObject pagine = jObject.getJSONObject("params");
                            pagination = gson.fromJson(pagine.toString(), Pagination.class);
                            for(int i=0; i<arrayEvent.length(); i++){
                                JSONObject json_data = arrayEvent.getJSONObject(i);

                                TourDetail event = gson.fromJson(json_data.toString(), TourDetail.class);
                                eventArrayLists.add(event);
                            }
                            firstBar.setVisibility(View.GONE);
                            if(eventArrayLists.isEmpty()){
                                lineNodata.setVisibility(View.VISIBLE);
                                isLoadMore = false;
                                listView.setVisibility(View.GONE);
                            }
                            else {
                                lineNodata.setVisibility(View.INVISIBLE);
                                updateData();
                                isLoadMore = eventArrayLists.size() >= 10;
                                listView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } catch (JSONException e) {e.printStackTrace();

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

        BaseRestClient.get(getActivity(), SEARCH_TOUR_URL, param, hearder, responseVeryfy, false);
    }
    private void fetchPickup() {
        DataResponse responseVeryfy = new DataResponse(getContext()) {
            @Override
            public void onRealFail() {
                reNointernet.setVisibility(View.VISIBLE);
            }
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
                try {
                    JSONArray jArray = new JSONArray (response);
                    JSONObject jsonObj = jArray.getJSONObject(0);
                    Gson gson = new Gson();
                    Pickup pickup = gson.fromJson(jsonObj.toString(), Pickup.class);
                    link_pickup = pickup.getLink();
                    showPickup(pickup.getCustomfields().getPickup_banner().getUrl());

                } catch (Exception e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
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

        BaseRestClient.get(getActivity(), ApiKey.PICKUP_URL, null, null, responseVeryfy);

    }

    private void fetchCalendar() {
        DataResponse responseVeryfy = new DataResponse(getContext()) {
            @Override
            public void onRealFail() {
                reNointernet.setVisibility(View.VISIBLE);
            }
            @Override
            public void onRealSuccess(String response) {
                super.onRealSuccess(response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getString("status").equals("200")) {
                        Boolean success = jObject.getBoolean("success");
                        if (success) {
                            Gson gson = new Gson();
                            DateFormat df = new SimpleDateFormat(CommonFunc.DATE_FORMAT_NONE_HOUR);
                            JSONArray arrayEvent =  jObject.getJSONArray("data");
                            days = new ArrayList<>();
                            JSONObject json_data = arrayEvent.getJSONObject(0);
                            Calendar calendar = gson.fromJson(json_data.toString(), Calendar.class);
                            Calendar.EventDatesBean eventDatesBean = calendar.getEvent_dates().get(calendar.getEvent_dates().size()-1);
                            java.util.Calendar cal = java.util.Calendar.getInstance();
                            Date today = cal.getTime();
                            Date lastDate = df.parse(eventDatesBean.getDate());
                            while (today.before(lastDate))
                            {
                                cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
                                today = cal.getTime();
                                days.add(today);
                            }
                            loadCalendar_today();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

        BaseRestClient.get(getActivity(), ApiKey.SEARCH_CALENDAR, null, null, responseVeryfy, false);

    }
    private void loadCalendar_today() {
        if(days != null && days.size() > 0) {
            Date today = new Date();
            DateFormat df2 = new SimpleDateFormat("yyyyMM");
            for (int key = 0; key < days.size(); key++) {

                Date newdate = days.get(key);
                String monthyear = df2.format(newdate);
                if (!mapCalendar.containsKey(monthyear)) {
                    mapCalendar.put(monthyear, key);
                }

            }
            dateSelect_pos = 0;
            for (int i = 0; i < days.size(); i++) {
                if (CommonFunc.getDate(today.getTime(), "yyyy/MM/dd").equals(CommonFunc.getDate(days.get(i).getTime(), "yyyy/MM/dd"))) {
                    dateSelect_pos = i;
                    break;
                }
            }
            int index = 0;


            String monthyear = df2.format(today);
            for (Object key : mapCalendar.keySet()) {
                if (key.equals(monthyear)) break;
                ++index;
            }
            idxMonth = index;
            Date date = days.get(dateSelect_pos);
            searchtour( CommonFunc.getDate(date.getTime(), "yyyy/MM/dd"), "", "", "", "");


            updateCalendar();
            setstatus();
        }
    }

    private void updateCalendar() {
        weekendAdapter = new WeekendScheduleAdapter(myContext, days,dateSelect_pos);
        gridView.setAdapter(weekendAdapter);
        LinearLayoutManager llm = (LinearLayoutManager) gridView.getLayoutManager();
        llm.scrollToPosition(dateSelect_pos);
        weekendAdapter.setOnItemClickListener(new WeekendScheduleAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                dateSelect_pos = position;
                weekendAdapter.notifyDataSetChanged();
                if (days.size() > 0) {
                    Date date = days.get(position);
                    if(DateUtils.isToday(date.getTime())) {
                        btToday.setAlpha(0.5f);
                        btToday.setEnabled(false);
                    } else {
                        btToday.setAlpha(1.0f);
                        btToday.setEnabled(true);
                    }
                    lineNodata.setVisibility(View.INVISIBLE);
                    searchtour( CommonFunc.getDate(date.getTime(),"yyyy/MM/dd"),"","","","");
                }

            }

            @Override
            public void onItemLongClick(int position, View v) {
            }
        });
        setstatus();
        Date date = days.get(dateSelect_pos);
        if(DateUtils.isToday(date.getTime())) {
            btToday.setAlpha(0.5f);
            btToday.setEnabled(false);
        } else {
            btToday.setAlpha(1.0f);
            btToday.setEnabled(true);
        }

        searchtour( CommonFunc.getDate(date.getTime(), "yyyy/MM/dd"), "", "", "", "");
    }

    private void previosDay() {
//        Log.d("last:",""+dateSelect_pos+"/" +layoutManager.findFirstVisibleItemPosition() );
        LinearLayoutManager llm = (LinearLayoutManager) gridView.getLayoutManager();
        int pos = llm.findFirstVisibleItemPosition() - 4;
//        if(dateSelect_pos == days.size() -1) dateSelect_pos = layoutManager.findFirstVisibleItemPosition() -3;
//        else if(layoutManager.findFirstVisibleItemPosition() - dateSelect_pos < 4)
//            dateSelect_pos = layoutManager.findFirstVisibleItemPosition() - 4;
//        else dateSelect_pos = layoutManager.findFirstVisibleItemPosition() -3;
        if(pos < 0) pos = 0;
        //llm.scrollToPosition(pos);
        dateSelect_pos = pos;
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
        updateCalendar();
    }

    private void nextDay() {

        LinearLayoutManager llm = (LinearLayoutManager) gridView.getLayoutManager();
        int pos = llm.findLastVisibleItemPosition() + 1;
        if(pos > 0) {
//            Log.d("","vao day");
            if(pos >= days.size() -1) pos = days.size() -1;

            //llm.scrollToPosition(pos);
            dateSelect_pos = pos;
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
            updateCalendar();
        }

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_news:
                loadwebview();
                break;
            case R.id.bt_left_day:
                previosDay();
                break;
            case R.id.bt_right_day:
                nextDay();
                break;
            case R.id.picup_logo:
                loadwebview();
                break;
            default:
                break;

        }
    }

    private void loadwebview() {
        //!link_pickup.isEmpty()
        if(!TextUtils.isEmpty(link_pickup)) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("webview");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            WebViewNEWSFragment fragment = WebViewNEWSFragment.newInstance(new WebViewNEWSFragment.OnCompleteListener() {
                @Override
                public void onComplete() {

                }
            }, link_pickup,false);
            fragment.show(ft, "webview");
        }
    }
    private void showTourDetail(TourDetail tourDetail) {
        Date date_event = days.get(dateSelect_pos);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        TourDetailFragment fragment =  TourDetailFragment.newInstance(tourDetail,"",CommonFunc.getDate(date_event.getTime(),"yyyy/MM/dd"));
        ft.replace(R.id.fragment_schedule, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack("Tour_Detail");
        ft.commit();
    }
    private void showEvent(String event_id) {
        Date date_event = days.get(dateSelect_pos);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        TourDetailFragment fragment =  TourDetailFragment.newInstance(null,event_id,CommonFunc.getDate(date_event.getTime(),"yyyy/MM/dd"));
        ft.replace(R.id.fragment_schedule, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack("Tour_Detail");
        ft.commit();
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
                switch(action) {
                    case (MotionEvent.ACTION_DOWN) :
                        return true;
                    case (MotionEvent.ACTION_MOVE) :
                        float horizontalOffset = event.getY();

                        move += horizontalOffset;
                        return true;
                    case (MotionEvent.ACTION_UP) :
                        if(move > 0) pickupLogo.setVisibility(View.VISIBLE);
                        else if(move < 0) pickupLogo.setVisibility(View.GONE);
                        move = 0;
                        return true;
                    case (MotionEvent.ACTION_CANCEL) :
                        return true;
                    case (MotionEvent.ACTION_OUTSIDE) :
                        return true;
                    default :
                        return true;
                }

    }
    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }


}
