package eplus.scrap.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimus.edittextfield.EditTextField;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eplus.scrap.MainActivity;
import eplus.scrap.R;
import eplus.scrap.adapter.SesrchPagerAdapter;
import eplus.scrap.calendarview2.CalendarDay;
import eplus.scrap.calendarview2.CalendarView2;
import eplus.scrap.calendarview2.OnDateSelectedListener;
import eplus.scrap.calendarview2.OnMonthChangedListener;
import eplus.scrap.calendarview2.OnRangeSelectedListener;
import eplus.scrap.calendarview2.format.DayFormatter;
import eplus.scrap.common.CommonFunc;
import eplus.scrap.common.decorators.OneDayDecorator;
import eplus.scrap.model.TourDetail;

import static eplus.scrap.calendarview2.CalendarView2.SELECTION_MODE_RANGE;
import static eplus.scrap.calendarview2.CalendarView2.SHOW_ALL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements View.OnClickListener, OnDateSelectedListener, OnRangeSelectedListener,  OnMonthChangedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private  OneDayDecorator oneDayDecorator;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SEARCH TOUR";





    // TODO: Rename and change types of parameters
    private int mValue;
    private String mParam2;
    private EditTextField txtSerchKeyword;
    private RelativeLayout btSearchPeriod;
    private TextView mTvPeriod;
    private FragmentActivity myContext;


    private String mKeyWord = "";
    private long mFromdate = 0;
    private long mToDate = 0;


    private ImageView modalView;
    private RelativeLayout rePeriod;
    private boolean showPeriod;
    private ImageView bgPeriodModalView;

    // search period
    private Button btFromdate;
    private Button btToDate;
    private int statusDateSelect = 0;
    private long fromDate;
    private long toDate;
    TabLayout tabLayout;
    int tabindex = 0;
    private DayFormatter formatter = DayFormatter.DEFAULT;

    private CalendarView2 widget;
    private TextView tvToday;
    private ViewPager vpPager;
    private SesrchPagerAdapter adapterViewPager;

    private  int width;
    private  int height;
    private RelativeLayout reNointernet;
    private LinearLayout lineBtRetry;
    private ImageView icDelete;
    int[] icon_unselect = { R.drawable.comming_soon_unselect, R.drawable.open_now_unselect
    };
    int[] icon_select = { R.drawable.comming_soon_selected, R.drawable.open_now_selected
    };


    private Context getCurrentContext(){
        return myContext == null ? getContext() : myContext;
    }
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(int value, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, value);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mKeyWord = "";
        mFromdate = 0;
        mToDate = 0;

    }

    @Override
    public void onPause() {
        super.onPause();
    }
    boolean _areLecturesLoaded = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areLecturesLoaded ) {
            _areLecturesLoaded = true;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mValue = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        myContext = getActivity();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        icDelete = view.findViewById(R.id.ic_delete);
        icDelete.setVisibility(View.GONE);
        icDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFromdate = 0;
                mToDate = 0;
                icDelete.setVisibility(View.GONE);
                mTvPeriod.setText(getString(R.string.period));
                updateData();
                widget.setSelectedDate(CalendarDay.today());
            }
        });
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated
        LinearLayout lineKeyword = view.findViewById(R.id.line_keyword);
        lineKeyword.getLayoutParams().height = (int) (width * 0.109);
        initBackgroundTop(view);
        intCalendarView(view);
        initMainView(view);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                view.getWindowVisibleDisplayFrame(r);
                if(!showPeriod) {
                    if (view.getRootView().getHeight() - (r.bottom - r.top) > 500) { // if more than 100 pixels, its probably a keyboard...
                        btSearchPeriod.setVisibility(View.GONE);
                        if(getActivity() instanceof  MainActivity){
                            ((MainActivity)getActivity()).hideTabLayout();
                        }
                        modalView.setVisibility(View.VISIBLE);

                    } else {
                        btSearchPeriod.setVisibility(View.VISIBLE);
                        if(getActivity() instanceof  MainActivity){
                            ((MainActivity)getActivity()).showTabLayout();
                        }
                        modalView.setVisibility(View.GONE);
                    }
                }



            }
        });
        reNointernet = view.findViewById(R.id.re_no_internet_layout);
        reNointernet.setVisibility(View.GONE);
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
                    if(CommonFunc.isNetworkConnected(getCurrentContext())) {
                        adapterViewPager = new SesrchPagerAdapter(new SesrchPagerAdapter.OnCompleteListener() {
                            @Override
                            public void onShowTour(TourDetail tourDetail) {
                                showTourDetail(tourDetail);
                            }

                            @Override
                            public void onNointernet() {
                                reNointernet.setVisibility(View.VISIBLE);
                            }
                        }, getChildFragmentManager(), 2, getCurrentContext(), mKeyWord, mFromdate, mToDate);
                        vpPager.setAdapter(adapterViewPager);
                        adapterViewPager.notifyDataSetChanged();
                        vpPager.setCurrentItem(tabindex);

                        reNointernet.setVisibility(View.GONE);
                    }
                }
                return true;
            }
        });



        return view;
    }

    private void initMainView(View view) {
        RelativeLayout re_top_ct = view.findViewById(R.id.re_top_ct);
        re_top_ct.bringToFront();
        mTvPeriod = view.findViewById(R.id.tv_period);


        btSearchPeriod = view.findViewById(R.id.bt_searchperiod);
        ViewGroup.MarginLayoutParams btSearchPeriod_params = (ViewGroup.MarginLayoutParams) btSearchPeriod.getLayoutParams();
        btSearchPeriod_params.leftMargin = (int) (width * 0.01);
        btSearchPeriod.setOnClickListener(this);
        txtSerchKeyword = view.findViewById(R.id.txt_search);
        ViewGroup.MarginLayoutParams txtSerchKeyword_params = (ViewGroup.MarginLayoutParams) txtSerchKeyword.getLayoutParams();
        txtSerchKeyword_params.rightMargin = (int) (width * 0.01);
        txtSerchKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||actionId == KeyEvent.KEYCODE_BACK) {
                    CommonFunc.hideSoftKeyboard((Activity) getCurrentContext());
                    mKeyWord = String.valueOf(v.getText()).trim();
                    txtSerchKeyword.setText(mKeyWord);
                    updateData();
                    return false;
                }
                return false;
            }
        });



        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpPager.setCurrentItem(tab.getPosition());
                tabindex = tab.getPosition();
                tabselect(tabindex);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vpPager = view.findViewById(R.id.viewpager);
        ViewGroup.MarginLayoutParams vpPager_params = (ViewGroup.MarginLayoutParams) vpPager.getLayoutParams();
        vpPager_params.topMargin = (int) (width * 0.285);
        //TODO nho bo ra
        adapterViewPager = new SesrchPagerAdapter(new SesrchPagerAdapter.OnCompleteListener() {
            @Override
            public void onShowTour(TourDetail tourDetail) {
                showTourDetail(tourDetail);
            }

            @Override
            public void onNointernet() {
                reNointernet.setVisibility(View.VISIBLE);
            }

        }, getChildFragmentManager(), tabLayout.getTabCount(), getCurrentContext(), mKeyWord, mFromdate, mToDate);
        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        if(mValue == 4) {
            initTabbar(1);
            vpPager.setCurrentItem(1);
        }
        else  {
            initTabbar(0);
            vpPager.setCurrentItem(0);
        }

    }
    private void initTabbar (int position) {
        RelativeLayout tabOne = (RelativeLayout) LayoutInflater.from(getCurrentContext()).inflate(R.layout.search_tab, null);
        ImageView icon_tab_tabOne = tabOne.findViewById(R.id.img_background);
        if (position == 0) {
            icon_tab_tabOne.setImageDrawable(getResources().getDrawable(R.drawable.comming_soon_selected));
        } else {
            icon_tab_tabOne.setImageDrawable(getResources().getDrawable(R.drawable.comming_soon_unselect));
        }


        RelativeLayout tabTwo = (RelativeLayout) LayoutInflater.from(getCurrentContext()).inflate(R.layout.search_tab, null);
        ImageView icon_tab_tabTwo = tabTwo.findViewById(R.id.img_background);
        if(position == 1) {
            icon_tab_tabTwo.setImageDrawable(getResources().getDrawable(R.drawable.open_now_selected));
        } else {
            icon_tab_tabTwo.setImageDrawable(getResources().getDrawable(R.drawable.open_now_unselect));
        }


        tabLayout.getTabAt(0).setCustomView(tabOne);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

    }

    private void tabselect (int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            View view=tab.getCustomView();
            ImageView icon_tab = view.findViewById(R.id.img_background);
            if(i == position) {
                icon_tab.setImageResource(icon_select[i]);
            } else {
                icon_tab.setImageResource(icon_unselect[i]);
            }


        }
    }
    private void intCalendarView(View view) {
        tvToday = view.findViewById(R.id.tv_today);
        tvToday.setText(CommonFunc.getDate(new Date().getTime(),"yyyy年MM月"));
        modalView = view.findViewById(R.id.view_modal);
        modalView.setOnClickListener(this);
        bgPeriodModalView = view.findViewById(R.id.view_modal_period);
        bgPeriodModalView.setOnClickListener(this);
        rePeriod = view.findViewById(R.id.re_period);
        rePeriod.setVisibility(View.GONE);
        // search period
        btFromdate = view.findViewById(R.id.bt_fromdate);
        btToDate = view.findViewById(R.id.bt_todate);
        Button btOK = view.findViewById(R.id.bt_ok);
        btOK.getLayoutParams().height = (int) (height * 0.07);

        btOK.setOnClickListener(this);



        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        widget = view.findViewById(R.id.calendarView);
        widget.getLayoutParams().height = (int) (width * 0.74) ;
        int spacing = (int) (width * 0.0246);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) widget.getLayoutParams();
        LinearLayout line_bt_start_end = view.findViewById(R.id.line_bt_start_end);
        line_bt_start_end.getLayoutParams().height = (int) (width * 0.106);
        ViewGroup.MarginLayoutParams line_bt_start_end_params = (ViewGroup.MarginLayoutParams) line_bt_start_end.getLayoutParams();
        line_bt_start_end_params.topMargin = line_bt_start_end_params.bottomMargin = line_bt_start_end_params.leftMargin = line_bt_start_end_params.rightMargin =  spacing;
        RelativeLayout re_period_top = view.findViewById(R.id.re_period_top);
        re_period_top.getLayoutParams().height = (int) (width * 0.3733);

        LinearLayout line_content1 = view.findViewById(R.id.line_content1);
        line_content1.getLayoutParams().height = (int) (width * 0.106);
        ViewGroup.MarginLayoutParams line_content1_params = (ViewGroup.MarginLayoutParams) line_content1.getLayoutParams();
        line_content1_params.leftMargin = line_content1_params.rightMargin =  spacing;
        line_content1_params.bottomMargin = (int) (width * 0.06);

        ViewGroup.MarginLayoutParams tvToday_params = (ViewGroup.MarginLayoutParams) tvToday.getLayoutParams();
        tvToday_params.bottomMargin = (int) (width * 0.04);

        widget.setSelectionColor(Color.parseColor("#006f4e"));
        widget.setSelectionMode(SELECTION_MODE_RANGE);
        widget.setTopbarVisible(false);
        widget.setOnRangeSelectedListener(this);
        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);
        widget.setShowOtherDates(SHOW_ALL);

        widget.setDayFormatter(formatter);



        oneDayDecorator = new OneDayDecorator(this);
        widget.addDecorators(
                oneDayDecorator
        );

        Button btThisweek = view.findViewById(R.id.bt_this_week);
        btThisweek.setOnClickListener(this);
        Button btNextweek = view.findViewById(R.id.bt_next_week);
        btNextweek.setOnClickListener(this);
        Button btThismonth = view.findViewById(R.id.bt_this_month);
        btThismonth.setOnClickListener(this);
    }

    private void initBackgroundTop(View view) {
        ImageView img_bg_red = view.findViewById(R.id.img_top_red);
        img_bg_red.getLayoutParams().height = (int) (width * 0.295);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.bt_searchperiod:
                showSearchPeriod();
                break;
            case R.id.view_modal:
                CommonFunc.hideSoftKeyboard((Activity) getCurrentContext());
                break;
            case R.id.view_modal_period:
                showSearchPeriod();
                break;

            case R.id.bt_this_week:
                getThisWeek();
                onFinishDialog(fromDate,toDate);
                break;
            case R.id.bt_next_week:
                getNextWeek();
                onFinishDialog(fromDate,toDate);
                break;
            case R.id.bt_this_month:
                getThisMoth();
                onFinishDialog(fromDate,toDate);
                break;
            case R.id.bt_ok:
                onFinishDialog(fromDate,toDate);
                break;
            default:
                break;

        }

    }

    private void showSearchPeriod() {
        if(!showPeriod) {
            showPeriod = true;
            rePeriod.setVisibility(View.VISIBLE);
            if(getActivity() instanceof  MainActivity){
                ((MainActivity)getActivity()).hideTabLayout();
            }

        } else {
            showPeriod = false;
            rePeriod.setVisibility(View.GONE);
            if(getActivity() instanceof  MainActivity){
                ((MainActivity)getActivity()).showTabLayout();
            }
        }
    }

    private void updateData() {

        adapterViewPager = new SesrchPagerAdapter(new SesrchPagerAdapter.OnCompleteListener() {
            @Override
            public void onShowTour(TourDetail tourDetail) {
                showTourDetail(tourDetail);
            }

            @Override
            public void onNointernet() {
                reNointernet.setVisibility(View.VISIBLE);
            }
        },getChildFragmentManager(), 2, getCurrentContext(), mKeyWord, mFromdate, mToDate);
        vpPager.setAdapter(adapterViewPager);
        adapterViewPager.notifyDataSetChanged();
        vpPager.setCurrentItem(tabindex);



    }

    public void showTourDetail(TourDetail tourDetail) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        TourDetailFragment fragment = new TourDetailFragment().newInstance(tourDetail,"","");
        ft.replace(R.id.fragment_search, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack("Tour_Detail");
        ft.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
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

    // search period
    @Override
    public void onDateSelected(@NonNull CalendarView2 widget, @NonNull CalendarDay date, boolean selected) {
        if (oneDayDecorator != null && date.equals(CalendarDay.today())) {
            oneDayDecorator.setDate(date.getDate());
            widget.invalidateDecorators();
        }
        if(statusDateSelect == 0 && selected) {
            fromDate = date.getDate().getTime();
            statusDateSelect = 1;
            btFromdate.setText(CommonFunc.getDate(fromDate,"MMMM dd (E)"));
            btToDate.setText(getString(R.string.start_date));
            toDate = 0;

        }else {
            fromDate = 0;
            btFromdate.setText(getString(R.string.start_date));
            fromDate = 0;
            btToDate.setText(getString(R.string.end_date));

        }
    }

    @Override
    public void onRangeSelected(@NonNull CalendarView2 widget, @NonNull final List<CalendarDay> dates) {

        if(dates.size() > 1) {
            fromDate = dates.get(0).getDate().getTime();
            btFromdate.setText(CommonFunc.getDate(fromDate, "MMMM dd (E)"));
            toDate = dates.get(dates.size() - 1).getDate().getTime();
            statusDateSelect = 0;
            btToDate.setText(CommonFunc.getDate(toDate, "MMMM dd (E)"));
        }
    }

    @Override
    public void onMonthChanged(CalendarView2 widget, CalendarDay date) {
        tvToday.setText(CommonFunc.getDate(date.getDate().getTime(),"yyyy年 MM月"));
    }
//
    private void getThisWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        fromDate = cal.getTimeInMillis();
        System.out.println(CommonFunc.getDate(fromDate,"MMMM dd (E)"));
        cal.add(Calendar.DAY_OF_WEEK, 6);
        toDate = cal.getTimeInMillis();
        System.out.println(CommonFunc.getDate(toDate,"MMMM dd (E)"));

    }
    private void getThisMoth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        fromDate = cal.getTimeInMillis();
        System.out.println(CommonFunc.getDate(fromDate,"MMMM dd (E)"));
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        toDate = cal.getTimeInMillis();
        System.out.println(CommonFunc.getDate(toDate,"MMMM dd (E)"));

    }
    private void getNextWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        fromDate = cal.getTimeInMillis();
        System.out.println(CommonFunc.getDate(fromDate,"MMMM dd (E)"));
        cal.add(Calendar.DAY_OF_WEEK, 6);
        toDate = cal.getTimeInMillis();
        System.out.println(CommonFunc.getDate(toDate,"MMMM dd (E)"));

    }
    public void onFinishDialog(long fromDate, long toDate) {
                if(fromDate == 0 && toDate == 0) {
                    mFromdate = 0;
                    mToDate = 0;
                    icDelete.setVisibility(View.GONE);
                }else {
                    if(fromDate == 0) fromDate = toDate;
                    else if (toDate == 0) toDate = fromDate;
                    mTvPeriod.setText(getString(R.string.period));
                    mFromdate = fromDate;
                    mToDate = toDate;
                    icDelete.setVisibility(View.VISIBLE);
                    mTvPeriod.setText(CommonFunc.getDate(fromDate, "MM/dd") + "~" + CommonFunc.getDate(toDate, "MM/dd"));

                }
                updateData();
                showSearchPeriod();
    }

}
