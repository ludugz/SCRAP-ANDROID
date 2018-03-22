package eplus.scrap.model;

import java.util.List;

/**
 * Created by nals-anhdv on 8/24/17.
 */

public class Product {

    /**
     * product_id : 352
     * product_name : 私は緑の芝生の上に黄色の花を見まし8
     * start_tour_period : 2017/08/21
     * end_tour_period : 2017/09/30
     * main_image : https://scrap.nal.vn/images/detailed/1/aa.jpg
     * venue_id : 210
     * venue_name : Dn
     * venue_color : #000000
     * schedules : [{"date_id":212,"time_id":220,"date_time":"2017/08/22T15:00:00","time":"15:00","stock":10,"status_stock":1}]
     */

    private int product_id;
    private String product_name;
    private String start_tour_period;
    private String end_tour_period;
    private String main_image;
    private int venue_id;
    private String venue_name;
    private String venue_color;
    private List<SchedulesBean> schedules;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getStart_tour_period() {
        return start_tour_period;
    }

    public void setStart_tour_period(String start_tour_period) {
        this.start_tour_period = start_tour_period;
    }

    public String getEnd_tour_period() {
        return end_tour_period;
    }

    public void setEnd_tour_period(String end_tour_period) {
        this.end_tour_period = end_tour_period;
    }

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    public int getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(int venue_id) {
        this.venue_id = venue_id;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public void setVenue_name(String venue_name) {
        this.venue_name = venue_name;
    }

    public String getVenue_color() {
        return venue_color;
    }

    public void setVenue_color(String venue_color) {
        this.venue_color = venue_color;
    }

    public List<SchedulesBean> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<SchedulesBean> schedules) {
        this.schedules = schedules;
    }


}
