package eplus.scrap.model;

/**
 * Created by nals-anhdv on 8/24/17.
 */

public class SchedulesBean {


    /**
     * time_id : 51917
     * date_time : 2017/10/14T13:00:00
     * stock : 50
     * status : 1
     * holiday : false
     */

    private int time_id;
    private String date_time;
    private int stock;
    private int status;
    private boolean holiday;

    public int getTime_id() {
        return time_id;
    }

    public void setTime_id(int time_id) {
        this.time_id = time_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }
}