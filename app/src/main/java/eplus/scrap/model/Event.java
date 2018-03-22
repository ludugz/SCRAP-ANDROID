package eplus.scrap.model;

import java.util.List;

/**
 * Created by nals-anhdv on 8/3/17.
 */

public class Event {


    /**
     * month : 8
     * year : 2017
     * event_counts : [{"day":1,"count_event":0},{"day":2,"count_event":0},{"day":3,"count_event":2},{"day":4,"count_event":0},{"day":5,"count_event":1},{"day":6,"count_event":0},{"day":7,"count_event":0},{"day":8,"count_event":0},{"day":9,"count_event":0},{"day":10,"count_event":0},{"day":11,"count_event":0},{"day":12,"count_event":0},{"day":13,"count_event":0},{"day":14,"count_event":0},{"day":15,"count_event":0},{"day":16,"count_event":0},{"day":17,"count_event":0},{"day":18,"count_event":0},{"day":19,"count_event":0},{"day":20,"count_event":1},{"day":21,"count_event":1},{"day":22,"count_event":0},{"day":23,"count_event":0},{"day":24,"count_event":0},{"day":25,"count_event":0},{"day":26,"count_event":0},{"day":27,"count_event":0},{"day":28,"count_event":0},{"day":29,"count_event":0},{"day":30,"count_event":0},{"day":31,"count_event":0}]
     */

    private int month;
    private int year;
    private List<EventCountsBean> event_counts;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<EventCountsBean> getEvent_counts() {
        return event_counts;
    }

    public void setEvent_counts(List<EventCountsBean> event_counts) {
        this.event_counts = event_counts;
    }

    public static class EventCountsBean {
        /**
         * day : 1
         * count_event : 0
         */

        private int day;
        private int count_event;

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getCount_event() {
            return count_event;
        }

        public void setCount_event(int count_event) {
            this.count_event = count_event;
        }
    }
}
