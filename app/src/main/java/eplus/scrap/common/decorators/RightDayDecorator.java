package eplus.scrap.common.decorators;

import android.graphics.drawable.Drawable;
import android.text.style.RelativeSizeSpan;

import java.util.Date;

import eplus.scrap.R;
import eplus.scrap.calendarview2.CalendarDay;
import eplus.scrap.calendarview2.DayViewDecorator;
import eplus.scrap.calendarview2.DayViewFacade;
import eplus.scrap.view.SearchFragment;

/**
 * Decorate a day by making the text big and bold
 */
public class RightDayDecorator extends DayViewDecorator {

    private CalendarDay date;
    private final Drawable drawable;



    public RightDayDecorator(SearchFragment context) {
        date = CalendarDay.today();
        drawable = context.getResources().getDrawable(R.drawable.right_date_range_circle);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.4f));
        view.setBackgroundDrawable(drawable);
    }


    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}
