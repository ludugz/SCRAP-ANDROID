package noman.weekcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import noman.weekcalendar.decorator.DayDecorator;
import noman.weekcalendar.decorator.DefaultDayDecorator;
import noman.weekcalendar.eventbus.BusProvider;
import noman.weekcalendar.eventbus.Event;
import noman.weekcalendar.listener.OnDateClickListener;
import noman.weekcalendar.listener.OnWeekChangeListener;
import noman.weekcalendar.view.WeekPager;

/**
 * Created by nor on 12/6/2015.
 */
public class WeekCalendar extends LinearLayout {
    private static final String TAG = "WeekCalendarTAG";
    private OnDateClickListener listener;
    private TypedArray typedArray;
    private GridView daysName;
    private DayDecorator dayDecorator;
    private OnWeekChangeListener onWeekChangeListener;


    public WeekCalendar(Context context) {
        super(context);
        init(context,null);
    }

    public WeekCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);

    }

    public WeekCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);

    }

    private void init(Context context,AttributeSet attrs) {
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeekCalendar);
            int selectedDateColor = typedArray.getColor(R.styleable
                    .WeekCalendar_selectedBgColor, ContextCompat.getColor(context, R.color
                    .colorPrimary));
            int todayDateColor = typedArray.getColor(R.styleable
                    .WeekCalendar_todaysDateBgColor, ContextCompat.getColor(context, R.color
                    .day_bg));
            int daysTextColor = typedArray.getColor(R.styleable
                    .WeekCalendar_daysTextColor, Color.WHITE);
            float daysTextSize = typedArray.getDimension(R.styleable
                    .WeekCalendar_daysTextSize, -1);
            int todayDateTextColor = typedArray.getColor(R.styleable
                    .WeekCalendar_todaysDateTextColor, ContextCompat.getColor(context, android.R.color.white));
            setDayDecorator(new DefaultDayDecorator(context,
                    selectedDateColor,
                    todayDateColor,
                    todayDateTextColor,
                    daysTextColor,
                    daysTextSize));
            typedArray.recycle();
        }
        setOrientation(VERTICAL);

//        if (!typedArray.getBoolean(R.styleable.WeekCalendar_hideNames, false)) {
//            daysName = getDaysNames();
//            addView(daysName, 0);
//        }

        WeekPager weekPager = new WeekPager(context, attrs);
        addView(weekPager);

    }

    /***
     * Do not use this method
     * this is for receiving date,
     * use "setOndateClick" instead.
     */
    @Subscribe
    public void onDateClick(Event.OnDateClickEvent event) {
        if (listener != null)
            listener.onDateClick(event.getDateTime());
    }

    @Subscribe
    public void onDayDecorate(Event.OnDayDecorateEvent event) {
        if (dayDecorator != null) {
            dayDecorator.decorate(event.getView(), event.getDayTextView(),event.getDayWeekView(), event.getDateTime(),
                    event.getFirstDay(), event.getSelectedDateTime());
        }
    }

    @Subscribe
    public void onWeekChange(Event.OnWeekChange event) {
        if (onWeekChangeListener != null) {
            onWeekChangeListener.onWeekChange(event.getFirstDayOfTheWeek(), event.isForward());
        }
    }

    public void setOnDateClickListener(OnDateClickListener listener) {
        this.listener = listener;
    }

    public void setDayDecorator(DayDecorator decorator) {
        this.dayDecorator = decorator;
    }
    public DayDecorator getDayDecorator() {
        return this.dayDecorator;
    }

    public void setOnWeekChangeListener(OnWeekChangeListener onWeekChangeListener) {
        this.onWeekChangeListener = onWeekChangeListener;
    }

    /**
     * Renders the days again. If you depend on deferred data which need to update the calendar
     * after it's resolved to decorate the days.
     */
    public void updateUi() {
        BusProvider.getInstance().post(new Event.OnUpdateUi());
    }

    public void moveToPrevious() {
        BusProvider.getInstance().post(new Event.UpdateSelectedDateEvent(-1));
    }

    public void moveToNext() {
        BusProvider.getInstance().post(new Event.UpdateSelectedDateEvent(1));
    }
    public void moveToPreviousMonth() {
        BusProvider.getInstance().post(new Event.UpdateSelectedDateEvent(-30));
    }

    public void moveToNextMonth() {
        BusProvider.getInstance().post(new Event.UpdateSelectedDateEvent(30));
    }
    public void reset() {
        BusProvider.getInstance().post(new Event.ResetEvent());
    }

    public void setSelectedDate(DateTime selectedDate) {
        BusProvider.getInstance().post(new Event.SetSelectedDateEvent(selectedDate));
    }

    public void setStartDate(DateTime startDate) {
        BusProvider.getInstance().post(new Event.SetStartDateEvent(startDate));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BusProvider.getInstance().unregister(this);
        BusProvider.disposeInstance();
    }
}
