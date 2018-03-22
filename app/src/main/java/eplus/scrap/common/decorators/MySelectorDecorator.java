package eplus.scrap.common.decorators;

import android.graphics.drawable.Drawable;

import eplus.scrap.R;
import eplus.scrap.calendarview2.CalendarDay;
import eplus.scrap.calendarview2.DayViewDecorator;
import eplus.scrap.calendarview2.DayViewFacade;
import eplus.scrap.view.SearchFragment;

/**
 * Use a custom selector
 */
public class MySelectorDecorator extends DayViewDecorator {
    private final Drawable drawable;

    public MySelectorDecorator(SearchFragment context) {
        drawable = context.getResources().getDrawable(R.drawable.day_selector);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
