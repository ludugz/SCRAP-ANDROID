package eplus.scrap.common.decorators;

/**
 * Created by nals-anhdv on 8/3/17.
 */

import java.util.Collection;
import java.util.HashSet;

import eplus.scrap.calendarview2.CalendarDay;
import eplus.scrap.calendarview2.DayViewDecorator;
import eplus.scrap.calendarview2.DayViewFacade;







/**



 * Decorate several days with two dots



 */



public class DoubleEventDecorator extends DayViewDecorator {







    private int colorLeft;



    private int colorRight;



    private HashSet<CalendarDay> dates;







    public DoubleEventDecorator(int colorLeft, int colorRight,



                                Collection<CalendarDay> dates) {



        this.colorLeft = colorLeft;



        this.colorRight = colorRight;



        this.dates = new HashSet<>(dates);



    }







    @Override



    public boolean shouldDecorate(CalendarDay day) {



        return dates.contains(day);



    }







    @Override



    public void decorate(DayViewFacade view) {



        view.addSpan(new DoubleDotSpan(5, colorLeft, colorRight));



    }



}
