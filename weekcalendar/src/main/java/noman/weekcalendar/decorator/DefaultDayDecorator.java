package noman.weekcalendar.decorator;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;

import noman.weekcalendar.R;
import noman.weekcalendar.fragment.WeekFragment;

/**
 * Created by gokhan on 7/27/16.
 */
public class DefaultDayDecorator implements DayDecorator {

    private Context context;
    private final int selectedDateColor;
    private final int todayDateColor;
    private int todayDateTextColor;
    private int textColor;
    private float textSize;
    public DateTime mDateTime;

    public DefaultDayDecorator(Context context,
                               @ColorInt int selectedDateColor,
                               @ColorInt int todayDateColor,
                               @ColorInt int todayDateTextColor,
                               @ColorInt int textColor,
                               float textSize) {
        this.context = context;
        this.selectedDateColor = selectedDateColor;
        this.todayDateColor = todayDateColor;
        this.todayDateTextColor = todayDateTextColor;
        this.textColor = textColor;
        this.textSize = textSize;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void decorate(View view, TextView dayTextView,TextView dayweekView,
                         DateTime dateTime, DateTime firstDayOfTheWeek, DateTime selectedDateTime) {
        //DateTime dt = new DateTime();

        Drawable holoCircle = ContextCompat.getDrawable(context, R.drawable.holo_circle);
        Drawable solidCircle = ContextCompat.getDrawable(context, R.drawable.solid_circle);
        Drawable visible_bg = ContextCompat.getDrawable(context, R.drawable.visible_bg);

        holoCircle.setColorFilter(selectedDateColor, PorterDuff.Mode.SRC_ATOP);
        solidCircle.setColorFilter(todayDateColor, PorterDuff.Mode.SRC_ATOP);

        if (firstDayOfTheWeek.getMonthOfYear() < dateTime.getMonthOfYear()
                || firstDayOfTheWeek.getYear() < dateTime.getYear())
            view.setBackground(solidCircle);

        DateTime calendarStartDate = WeekFragment.CalendarStartDate;
        this.mDateTime = selectedDateTime;

        if (selectedDateTime != null) {
                if (selectedDateTime.toLocalDate().equals(dateTime.toLocalDate())) {
                    view.setBackground(holoCircle);
                } else {
                    view.setBackground(solidCircle);
                }

        }

        if (calendarStartDate.toLocalDate().isAfter(dateTime.toLocalDate())) {
            view.setBackground(visible_bg);
        }
    }

    @Override
    public DateTime getDateSelect() {
        return this.mDateTime;
    }

    DateTime getDateTime() {
        return this.mDateTime;
    }
}
