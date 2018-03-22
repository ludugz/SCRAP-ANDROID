package eplus.scrap.common.decorators;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import eplus.scrap.R;
import eplus.scrap.calendarview2.CalendarDay;
import eplus.scrap.calendarview2.DayViewDecorator;
import eplus.scrap.calendarview2.DayViewFacade;
import eplus.scrap.view.SearchFragment;

/**
 * Highlight Saturdays and Sundays with a background
 */
public class HighlightWeekendsDecorator extends DayViewDecorator {

    private final Calendar calendar = Calendar.getInstance();
    private final Drawable highlightDrawable;
    private HashSet<CalendarDay> dates;
    private static final int color = Color.parseColor("#178f5f");
    public HighlightWeekendsDecorator(SearchFragment context) {
        highlightDrawable = context.getResources().getDrawable(R.drawable.date_range_circle);
        //highlightDrawable = new ColorDrawable(color);
        this.dates = new HashSet<>();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(highlightDrawable);
    }
    public void setDate(Collection<CalendarDay> dates) {
        this.dates = new HashSet<>(dates);
    }
}
