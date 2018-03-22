package eplus.scrap.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nals-anhdv on 8/22/17.
 */

public class Coupon implements Parcelable {


    /**
     * promotion_id : 41
     * company_id : 1
     * coupon_code : C598E955
     * number_of_usages : 1
     * order_discount : 300
     * to_date : 2017/10/15
     * from_date : 2017/10/05
     * status : true
     * name : C598E955
     * detailed_description :
     * short_description :
     * condition : _coupon_condition_invite
     * user_coupon_type : I
     * user_id : 44
     * coupon_image_url : https://is-ticket.mysterycircus.jp/index.php?dispatch=coupon.show&code=gptciYrRWJMlJy2UqM6GMaZSekA65gvYgkZuTYpUjVo%3D
     * how_to_use : _coupon_how_to_use
     * notes : _coupon_notes
     */

    private int promotion_id;
    private int company_id;
    private String coupon_code;
    private int number_of_usages;
    private String order_discount;
    private String to_date;
    private String from_date;
    private boolean status;
    private String name;
    private String detailed_description;
    private String short_description;
    private String condition;
    private String user_coupon_type;
    private int user_id;
    private String coupon_image_url;
    private String how_to_use;
    private String notes;

    public int getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(int promotion_id) {
        this.promotion_id = promotion_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public int getNumber_of_usages() {
        return number_of_usages;
    }

    public void setNumber_of_usages(int number_of_usages) {
        this.number_of_usages = number_of_usages;
    }

    public String getOrder_discount() {
        return order_discount;
    }

    public void setOrder_discount(String order_discount) {
        this.order_discount = order_discount;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailed_description() {
        return detailed_description;
    }

    public void setDetailed_description(String detailed_description) {
        this.detailed_description = detailed_description;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getUser_coupon_type() {
        return user_coupon_type;
    }

    public void setUser_coupon_type(String user_coupon_type) {
        this.user_coupon_type = user_coupon_type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCoupon_image_url() {
        return coupon_image_url;
    }

    public void setCoupon_image_url(String coupon_image_url) {
        this.coupon_image_url = coupon_image_url;
    }

    public String getHow_to_use() {
        return how_to_use;
    }

    public void setHow_to_use(String how_to_use) {
        this.how_to_use = how_to_use;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
