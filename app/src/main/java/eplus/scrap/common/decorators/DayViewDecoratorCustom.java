package eplus.scrap.common.decorators;


import eplus.scrap.calendarview2.CalendarDay;
import eplus.scrap.calendarview2.DayViewFacade;

/**
 * Created by nals-anhdv on 8/3/17.
 */

public interface DayViewDecoratorCustom {
    boolean shouldDecorate(CalendarDay day);

    /**
     * Set decoration options onto a facade to be applied to all relevant days
     *
     * @param view View to decorate
     */
    void decorate(DayViewFacade view, CalendarDay day);

}
