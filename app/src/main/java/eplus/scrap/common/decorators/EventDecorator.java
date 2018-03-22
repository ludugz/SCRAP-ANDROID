package eplus.scrap.common.decorators;


import java.util.Collection;
import java.util.HashSet;

import eplus.scrap.calendarview2.CalendarDay;
import eplus.scrap.calendarview2.DayViewDecorator;
import eplus.scrap.calendarview2.DayViewFacade;

/**
 * Decorate several days with a dot
 */
public class EventDecorator extends DayViewDecorator {

    private int color;
    private HashSet<CalendarDay> dates;
    private int numberEvent;

    public EventDecorator(int color, Collection<CalendarDay> dates, int numberEvent) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        this.numberEvent = numberEvent;
    }



    @Override
    public boolean shouldDecorate(CalendarDay day) {


        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, color,numberEvent));


    }

}
