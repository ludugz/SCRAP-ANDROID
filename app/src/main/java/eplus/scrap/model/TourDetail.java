package eplus.scrap.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nals-anhdv on 8/7/17.
 */

@SuppressLint("ParcelCreator")
public class TourDetail implements Parcelable {


    /**
     * product_id : 367
     * product_name : 私は緑の芝生の上に黄色の花を見まし0
     * amount : 1
     * timestamp : 2017/08/17T12:28:52
     * updated_timestamp : 2017/08/23T16:47:21
     * product_code :
     * popularity : 139
     * price : 600
     * start_tour_period : 2017/08/17
     * end_tour_period : 2017/09/30
     * limit_number_ticket : 0
     * only_a_few_ticket : 0
     * show_schedule : 1
     * venue_color : #ffff00
     * main_image : https://scrap.nal.vn/images/detailed/1/aa.jpg
     * start_selling_date_time : 2017/08/17T00:00:00
     * end_selling_date_time : 2017/09/30T00:00:00
     * start_scanning_date_time : 2017/08/19T00:00:00
     * end_scanning_date_time : 2017/09/30T00:00:00
     * venue_id : 427
     * venue_name : Vincom
     * date_event : ["2017/08/22T20:00:00","2017/08/22T22:00:00","2017/08/23T20:00:00","2017/08/23T22:00:00","2017/08/24T20:00:00","2017/08/24T22:00:00","2017/08/25T20:00:00","2017/08/25T22:00:00","2017/08/28T20:00:00","2017/08/28T22:00:00","2017/08/29T20:00:00","2017/08/29T22:00:00","2017/08/30T20:00:00","2017/08/30T22:00:00","2017/08/31T20:00:00","2017/08/31T22:00:00","2017/09/01T20:00:00","2017/09/01T22:00:00","2017/09/04T20:00:00","2017/09/04T22:00:00","2017/09/05T20:00:00","2017/09/05T22:00:00","2017/09/06T20:00:00","2017/09/06T22:00:00","2017/09/07T20:00:00","2017/09/07T22:00:00","2017/09/08T20:00:00","2017/09/08T22:00:00","2017/09/11T20:00:00","2017/09/11T22:00:00","2017/09/12T20:00:00","2017/09/12T22:00:00","2017/09/13T20:00:00","2017/09/13T22:00:00","2017/09/14T20:00:00","2017/09/14T22:00:00","2017/09/15T20:00:00","2017/09/15T22:00:00","2017/09/18T20:00:00","2017/09/18T22:00:00","2017/09/19T20:00:00","2017/09/19T22:00:00","2017/09/20T20:00:00","2017/09/20T22:00:00","2017/09/21T20:00:00","2017/09/21T22:00:00","2017/09/22T20:00:00","2017/09/22T22:00:00","2017/09/25T20:00:00","2017/09/25T22:00:00","2017/09/26T20:00:00","2017/09/26T22:00:00","2017/09/27T20:00:00","2017/09/27T22:00:00","2017/09/28T20:00:00","2017/09/28T22:00:00"]
     * schedules : [{"date_id":428,"time_id":456,"date_time":"2017/08/22T20:00:00","stock":"10","status_stock":1},{"date_id":428,"time_id":457,"date_time":"2017/08/22T22:00:00","stock":"10","status_stock":1},{"date_id":429,"time_id":456,"date_time":"2017/08/23T20:00:00","stock":"10","status_stock":1},{"date_id":429,"time_id":457,"date_time":"2017/08/23T22:00:00","stock":"10","status_stock":1},{"date_id":430,"time_id":456,"date_time":"2017/08/24T20:00:00","stock":"10","status_stock":1},{"date_id":430,"time_id":457,"date_time":"2017/08/24T22:00:00","stock":"10","status_stock":1},{"date_id":431,"time_id":456,"date_time":"2017/08/25T20:00:00","stock":"10","status_stock":1},{"date_id":431,"time_id":457,"date_time":"2017/08/25T22:00:00","stock":"10","status_stock":1},{"date_id":432,"time_id":456,"date_time":"2017/08/28T20:00:00","stock":"10","status_stock":1},{"date_id":432,"time_id":457,"date_time":"2017/08/28T22:00:00","stock":"10","status_stock":1},{"date_id":433,"time_id":456,"date_time":"2017/08/29T20:00:00","stock":"10","status_stock":1},{"date_id":433,"time_id":457,"date_time":"2017/08/29T22:00:00","stock":"10","status_stock":1},{"date_id":434,"time_id":456,"date_time":"2017/08/30T20:00:00","stock":"10","status_stock":1},{"date_id":434,"time_id":457,"date_time":"2017/08/30T22:00:00","stock":"10","status_stock":1},{"date_id":435,"time_id":456,"date_time":"2017/08/31T20:00:00","stock":"10","status_stock":1},{"date_id":435,"time_id":457,"date_time":"2017/08/31T22:00:00","stock":"10","status_stock":1},{"date_id":436,"time_id":456,"date_time":"2017/09/01T20:00:00","stock":"10","status_stock":1},{"date_id":436,"time_id":457,"date_time":"2017/09/01T22:00:00","stock":"10","status_stock":1},{"date_id":437,"time_id":456,"date_time":"2017/09/04T20:00:00","stock":"10","status_stock":1},{"date_id":437,"time_id":457,"date_time":"2017/09/04T22:00:00","stock":"10","status_stock":1},{"date_id":438,"time_id":456,"date_time":"2017/09/05T20:00:00","stock":"10","status_stock":1},{"date_id":438,"time_id":457,"date_time":"2017/09/05T22:00:00","stock":"10","status_stock":1},{"date_id":439,"time_id":456,"date_time":"2017/09/06T20:00:00","stock":"10","status_stock":1},{"date_id":439,"time_id":457,"date_time":"2017/09/06T22:00:00","stock":"10","status_stock":1},{"date_id":440,"time_id":456,"date_time":"2017/09/07T20:00:00","stock":"10","status_stock":1},{"date_id":440,"time_id":457,"date_time":"2017/09/07T22:00:00","stock":"10","status_stock":1},{"date_id":441,"time_id":456,"date_time":"2017/09/08T20:00:00","stock":"10","status_stock":1},{"date_id":441,"time_id":457,"date_time":"2017/09/08T22:00:00","stock":"10","status_stock":1},{"date_id":442,"time_id":456,"date_time":"2017/09/11T20:00:00","stock":"10","status_stock":1},{"date_id":442,"time_id":457,"date_time":"2017/09/11T22:00:00","stock":"10","status_stock":1},{"date_id":443,"time_id":456,"date_time":"2017/09/12T20:00:00","stock":"10","status_stock":1},{"date_id":443,"time_id":457,"date_time":"2017/09/12T22:00:00","stock":"10","status_stock":1},{"date_id":444,"time_id":456,"date_time":"2017/09/13T20:00:00","stock":"10","status_stock":1},{"date_id":444,"time_id":457,"date_time":"2017/09/13T22:00:00","stock":"10","status_stock":1},{"date_id":445,"time_id":456,"date_time":"2017/09/14T20:00:00","stock":"10","status_stock":1},{"date_id":445,"time_id":457,"date_time":"2017/09/14T22:00:00","stock":"10","status_stock":1},{"date_id":446,"time_id":456,"date_time":"2017/09/15T20:00:00","stock":"10","status_stock":1},{"date_id":446,"time_id":457,"date_time":"2017/09/15T22:00:00","stock":"10","status_stock":1},{"date_id":447,"time_id":456,"date_time":"2017/09/18T20:00:00","stock":"10","status_stock":1},{"date_id":447,"time_id":457,"date_time":"2017/09/18T22:00:00","stock":"10","status_stock":1},{"date_id":448,"time_id":456,"date_time":"2017/09/19T20:00:00","stock":"10","status_stock":1},{"date_id":448,"time_id":457,"date_time":"2017/09/19T22:00:00","stock":"10","status_stock":1},{"date_id":449,"time_id":456,"date_time":"2017/09/20T20:00:00","stock":"10","status_stock":1},{"date_id":449,"time_id":457,"date_time":"2017/09/20T22:00:00","stock":"10","status_stock":1},{"date_id":450,"time_id":456,"date_time":"2017/09/21T20:00:00","stock":"10","status_stock":1},{"date_id":450,"time_id":457,"date_time":"2017/09/21T22:00:00","stock":"10","status_stock":1},{"date_id":451,"time_id":456,"date_time":"2017/09/22T20:00:00","stock":"10","status_stock":1},{"date_id":451,"time_id":457,"date_time":"2017/09/22T22:00:00","stock":"10","status_stock":1},{"date_id":452,"time_id":456,"date_time":"2017/09/25T20:00:00","stock":"10","status_stock":1},{"date_id":452,"time_id":457,"date_time":"2017/09/25T22:00:00","stock":"10","status_stock":1},{"date_id":453,"time_id":456,"date_time":"2017/09/26T20:00:00","stock":"10","status_stock":1},{"date_id":453,"time_id":457,"date_time":"2017/09/26T22:00:00","stock":"10","status_stock":1},{"date_id":454,"time_id":456,"date_time":"2017/09/27T20:00:00","stock":"10","status_stock":1},{"date_id":454,"time_id":457,"date_time":"2017/09/27T22:00:00","stock":"10","status_stock":1},{"date_id":455,"time_id":456,"date_time":"2017/09/28T20:00:00","stock":"10","status_stock":1},{"date_id":455,"time_id":457,"date_time":"2017/09/28T22:00:00","stock":"10","status_stock":1}]
     * tabs : {"ticket_sales":"<p>Empty<\/p>","price":"<p>前売一般 　2,800円<\/p><span id=\"docs-internal-guid-f8aa1b4d-a6bc-70e0-3df5-ea6d2399ce44\"><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\" rel=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">前売学生 　2,300円<br class=\"kix-line-break\">前売グループチケット　15,900円<br class=\"kix-line-break\">当日一般 　3,300円<br class=\"kix-line-break\">当日学生 　2,800円<\/p><\/span>","priviledge":"<p>empty<\/p>","notes":"<p>\u203b各回開場は開演の15分前。<\/p><span id=\"docs-internal-guid-f8aa1b4d-a6bc-e399-f1f7-35b4ee5b4028\"><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\" rel=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">\u203b必ずゲームスタートの10分前までにお越しください。<br class=\"kix-line-break\">ゲームの性質上、遅れますとご参加いただけない場合がございます。<\/p><p><br><\/p>\r\n<p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">\u203b学生チケット購入の場合、当日学生証必携。<\/p><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">\u203b小学生以下は要保護者同伴。未就学児は保護者同伴に限り入場無料。<\/p><p><br><\/p>\r\n<p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">前売券で完売していない場合、当日券の販売を行います。<\/p><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">\u203b前売チケットの最終的な販売数は当日朝に確定するため、事前のご案内は難しくなっております。ご了承ください。<\/p><\/span>","what_group_ticket":"<p><span id=\"docs-internal-guid-f8aa1b4d-a6bd-1316-2736-5849c3020b3d\"><\/span><\/p><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\" rel=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">グルーブチケットは1～6名様のお好きな人数で、1テーブルを貸し切ってゲームに参加できるチケットです。<\/p><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">もし、あなたの家族や友人達だけで脱出を目指してみたい方は、こちらのグルーブチケットをご利用ください。<\/p><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">1～6名様のお好きな人数で、1つのテーブルを貸し切りにでき、一般チケットを6枚購入するより少しお得なチケットになっています。<\/p><p><br><\/p>\r\n<p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">\u203bグループチケットに各種割引はご利用いただけません。予めご了承ください。<\/p><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">\u203bご購入時に発券されたチケットはすべて会場までお持ちください。<\/p><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">\u203b残席が少なくなった場合には、グループチケットの販売がない場合がございます。予めご了承ください<\/p><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">\u203bグループチケットは公演前日までの前売り券での販売となります。公演当日の販売はございません。<\/p><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">\u203b当日の受付には、グループ皆様が揃ってお越しください。チケットをお持ちでないお連れ様にはスタッフからお声がけさせていただく場合がございます。<\/p><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">また、会場の受付にてお連れ様分のチケットをお預かりすることはできませんので、代表の方がお連れ様分をまとめてお持ちください。<\/p><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">\u203b未就学児は1～6名の人数には含みません。<\/p><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">\u203b小学生以下のお客様のみでのグループ参加はお断りさせていただきます。小学生以下のお客様グループでご参加される場合は、同じグループに必ず保護者の方がご同伴ください。<\/p>","number_of_ppl_per_team":"<p>empty<\/p>","time_limit":"<p>ホール型。<\/p><span id=\"docs-internal-guid-f8aa1e74-a6bd-a525-85b9-3903a069c924\"><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\" rel=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">1チーム6人のゲームです。各<\/p><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">回24人ほどが同時に参加しますが、ゲームはチームごとに挑戦していただきます。<\/p><\/span>","show_how_to_play":"Y","how_to_play":"<p>ホール型。<\/p><span id=\"docs-internal-guid-f8aa1e74-a6bd-8378-4053-22ba997fb8da\"><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\" rel=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">1チーム6人のゲームです。<\/p><p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:0pt;\">各回24人ほどが同時に参加しますが、ゲームはチームごとに挑戦していただきます。<\/p><\/span>","content":"<h1 style=\"padding-top: 15%;text-align: center;width: 100%;height: 284px;background:#34495e; color: #fff\">{__(coming_soon)}<\/h1>\r\n<iframe id=\"frame\" src=\"\" width=\"100%\" style=\"border: none\">\r\n<\/iframe>\r\n"}
     */

    private int product_id;
    private String product_name;
    private int amount;
    private String timestamp;
    private String updated_timestamp;
    private String product_code;
    private String popularity;
    private int price;
    private String start_tour_period;
    private String end_tour_period;
    private int limit_number_ticket;
    private String only_a_few_ticket;
    private String show_schedule;
    private String venue_color;
    private String main_image;
    private String start_selling_date_time;
    private String end_selling_date_time;
    private String start_scanning_date_time;
    private String end_scanning_date_time;
    private int venue_id;
    private String venue_name;
    private String full_description;
    private List<TabsBean> tabs;
    private List<String> date_event;
    private List<SchedulesBean> schedules;
    private List<String> event_time;
    private List<String> event_time_holiday;
    private List<String> event_day;
    private List<String> event_day_holiday;
    private String min_schedule;
    private String max_schedule;

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUpdated_timestamp() {
        return updated_timestamp;
    }

    public void setUpdated_timestamp(String updated_timestamp) {
        this.updated_timestamp = updated_timestamp;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public int getLimit_number_ticket() {
        return limit_number_ticket;
    }

    public void setLimit_number_ticket(int limit_number_ticket) {
        this.limit_number_ticket = limit_number_ticket;
    }

    public String getOnly_a_few_ticket() {
        return only_a_few_ticket;
    }

    public void setOnly_a_few_ticket(String only_a_few_ticket) {
        this.only_a_few_ticket = only_a_few_ticket;
    }

    public String getShow_schedule() {
        return show_schedule;
    }

    public void setShow_schedule(String show_schedule) {
        this.show_schedule = show_schedule;
    }

    public String getVenue_color() {
        return venue_color;
    }

    public void setVenue_color(String venue_color) {
        this.venue_color = venue_color;
    }

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    public String getStart_selling_date_time() {
        return start_selling_date_time;
    }

    public void setStart_selling_date_time(String start_selling_date_time) {
        this.start_selling_date_time = start_selling_date_time;
    }

    public String getEnd_selling_date_time() {
        return end_selling_date_time;
    }

    public void setEnd_selling_date_time(String end_selling_date_time) {
        this.end_selling_date_time = end_selling_date_time;
    }

    public String getStart_scanning_date_time() {
        return start_scanning_date_time;
    }

    public void setStart_scanning_date_time(String start_scanning_date_time) {
        this.start_scanning_date_time = start_scanning_date_time;
    }

    public String getEnd_scanning_date_time() {
        return end_scanning_date_time;
    }

    public void setEnd_scanning_date_time(String end_scanning_date_time) {
        this.end_scanning_date_time = end_scanning_date_time;
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

    public List<String> getDate_event() {
        return date_event;
    }

    public void setDate_event(List<String> date_event) {
        this.date_event = date_event;
    }

    public List<SchedulesBean> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<SchedulesBean> schedules) {
        this.schedules = schedules;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public List<TabsBean> getTabs() {
        return tabs;
    }

    public void setTabsBean(List<TabsBean> tabs) {
        this.tabs = tabs;
    }



    public List<String> getEvent_time() {
        return event_time;
    }

    public void setEvent_time(List<String> event_time) {
        this.event_time = event_time;
    }

    public List<String> getEvent_time_holiday() {
        return event_time_holiday;
    }

    public void setEvent_time_holiday(List<String> event_time_holiday) {
        this.event_time_holiday = event_time_holiday;
    }

    public String getFull_description() {
        return full_description;
    }

    public void setFull_description(String full_description) {
        this.full_description = full_description;
    }

    public List<String> getEvent_day() {
        return event_day;
    }

    public void setEvent_day(List<String> event_day) {
        this.event_day = event_day;
    }

    public List<String> getEvent_day_holiday() {
        return event_day_holiday;
    }

    public void setEvent_day_holiday(List<String> event_day_holiday) {
        this.event_day_holiday = event_day_holiday;
    }

    public String getMin_schedule() {
        return min_schedule;
    }

    public void setMin_schedule(String min_schedule) {
        this.min_schedule = min_schedule;
    }

    public String getMax_schedule() {
        return max_schedule;
    }

    public void setMax_schedule(String max_schedule) {
        this.max_schedule = max_schedule;
    }

    public static class TabsBean implements Parcelable {


        /**
         * name : Overview
         * items : [{"title":"名称","content":"<label for=\"67_0_overview_content_0\" class=\"control-label \" style=\"font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif;\">テキスト<\/label><div class=\"controls cm-no-hide-input\" style=\"font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif; font-size: 13px; background-color: rgb(255, 255, 255);\"><div class=\"redactor-box\" role=\"application\" style=\"margin-bottom: 24px; background-image: initial; background-color: rgb(255, 255, 255);\"><ul class=\"redactor-toolbar\" id=\"redactor-toolbar-0\" role=\"toolbar\"><li><\/li><\/ul><\/div><\/div>"}]
         */

        private String name;
        private List<ItemsBean> items;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }

        public static class ItemsBean {
            /**
             * title : 名称
             * content : <label for="67_0_overview_content_0" class="control-label " style="font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif;">テキスト</label><div class="controls cm-no-hide-input" style="font-family: &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif; font-size: 13px; background-color: rgb(255, 255, 255);"><div class="redactor-box" role="application" style="margin-bottom: 24px; background-image: initial; background-color: rgb(255, 255, 255);"><ul class="redactor-toolbar" id="redactor-toolbar-0" role="toolbar"><li></li></ul></div></div>
             */

            private String title;
            private String content;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }

}
