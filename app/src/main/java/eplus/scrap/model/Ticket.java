package eplus.scrap.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nals-anhdv on 8/9/17.
 */

@SuppressLint("ParcelCreator")
public class Ticket  implements Parcelable {

    /**
     * product_name : Trang3 multiple tickets e+member
     * product_id : 285
     * order_id : 798
     * order_status : O
     * scanapp_status : N
     * image_path : https://d2vxc2k9wlisbr.cloudfront.net/images/detailed/1/ngây-thơ.jpg?t=1510822110
     * relative_path : detailed/1/ngây-thơ.jpg
     * venue : Vincom1
     * date_time : 2017/11/16T20:00:00
     * event_start : 2017/11/16T20:00:00
     * event_end : 2017/11/16T21:00:00
     * scanning_end : 2017/12/31T00:00:00
     * seats : [{"item_id":2600779873,"price":110,"amount":1,"seat":"Student11"},{"item_id":3959681271,"price":55,"amount":1,"seat":"Student12"}]
     * amount : 2
     * status : 1
     * qr_code :
     */

    private String product_name;
    private int product_id;
    private int order_id;
    private String order_status;
    private String scanapp_status;
    private String image_path;
    private String relative_path;
    private String venue;
    private String date_time;
    private String event_start;
    private String event_end;
    private String scanning_end;
    private int amount;
    private int status;
    private String qr_code;
    private List<SeatsBean> seats;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getScanapp_status() {
        return scanapp_status;
    }

    public void setScanapp_status(String scanapp_status) {
        this.scanapp_status = scanapp_status;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getRelative_path() {
        return relative_path;
    }

    public void setRelative_path(String relative_path) {
        this.relative_path = relative_path;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getEvent_start() {
        return event_start;
    }

    public void setEvent_start(String event_start) {
        this.event_start = event_start;
    }

    public String getEvent_end() {
        return event_end;
    }

    public void setEvent_end(String event_end) {
        this.event_end = event_end;
    }

    public String getScanning_end() {
        return scanning_end;
    }

    public void setScanning_end(String scanning_end) {
        this.scanning_end = scanning_end;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public List<SeatsBean> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatsBean> seats) {
        this.seats = seats;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public static class SeatsBean {
        /**
         * item_id : 2600779873
         * price : 110
         * amount : 1
         * seat : Student11
         */

        private long item_id;
        private int price;
        private int amount;
        private String seat;

        public long getItem_id() {
            return item_id;
        }

        public void setItem_id(long item_id) {
            this.item_id = item_id;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getSeat() {
            return seat;
        }

        public void setSeat(String seat) {
            this.seat = seat;
        }
    }
}
