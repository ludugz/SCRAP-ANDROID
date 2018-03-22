package eplus.scrap.model;

import java.util.List;

/**
 * Created by nals-anhdv on 9/23/17.
 */

public class Calendar {

    /**
     * month : 9
     * year : 2017
     * event_dates : [{"date":"2017/09/23","count_event":47},{"date":"2017/09/24","count_event":48},{"date":"2017/09/25","count_event":49},{"date":"2017/09/26","count_event":49},{"date":"2017/09/27","count_event":48},{"date":"2017/09/28","count_event":53},{"date":"2017/09/29","count_event":47},{"date":"2017/09/30","count_event":54}]
     */

    private int month;
    private int year;
    private List<EventDatesBean> event_dates;

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

    public List<EventDatesBean> getEvent_dates() {
        return event_dates;
    }

    public void setEvent_dates(List<EventDatesBean> event_dates) {
        this.event_dates = event_dates;
    }

    public static class EventDatesBean {
        /**
         * date : 2017/09/23
         * count_event : 47
         */

        private String date;
        private int count_event;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getCount_event() {
            return count_event;
        }

        public void setCount_event(int count_event) {
            this.count_event = count_event;
        }
    }
}
