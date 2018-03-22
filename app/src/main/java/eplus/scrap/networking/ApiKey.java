package eplus.scrap.networking;


import eplus.scrap.BuildConfig;

public class ApiKey {

    public static final int INTENT_RELOAD_HOME = 111;
    public static final String MESSAGE = "detail";
    public static final String RSS_LINK = "https://mysterycircus.jp/feed";

    public static final String ITEM_PER_PAGE = "20";
    public static final String SECRET_KEY = BuildConfig.BASE_SECRET_KEY;

    public static final String SECRET_TAG = "secret_key";
    public static final String URL_ACCESS = "https://mysterycircus.jp/access";
    public static final String URL_FAQ = "https://mysterycircus.jp/faq";
    public static String DOMAIN = BuildConfig.BASE_URL_API;
    public static final String PICKUP_URL = "https://mysterycircus.jp/wp-json/wp/v2/topics?meta_key=is_pickup&meta_value=1&_embed";
    public static final String SEARCH_TOUR_URL = DOMAIN + "api/product";
    public static final String SEARCH_CALENDAR = DOMAIN + "api/search_calendar?mode=count_event_calendar_all_month";
    public static final String GET_LIMIT_DATE = DOMAIN + "api/search_calendar";
    public static final String GET_TOUR_DETAIL = DOMAIN + "api/product/";
    public static final String GET_MY_TICKET = DOMAIN + "api/my_ticket";
    public static final String GET_COUPON = DOMAIN + "api/my_coupon";
    public static final String URL_LOGIN = DOMAIN + "index.php?dispatch=member.login";
    public static final String URL_LOGOUT = DOMAIN + "index.php?dispatch=member.logout";
    public static final String URL_UPDATE_STOCK = DOMAIN + "api/stocks";
    public static final String URL_GETMEMBER_INFO = DOMAIN + "index.php?dispatch=member.info";
    public static final String URL_GET_USER_INFO = DOMAIN + "api/get_user_info";
    public static final String URL_ENTER_INVITATION_CODE = DOMAIN + "api/get_coupon/invite";
    public static final String URL_GET_COUPON_ON_SHARE_SNS = DOMAIN + "api/get_coupon/share";
    public static final String URL_GET_SCHEDULE_DATE = DOMAIN + "api/schedule/";
    public static final String URL_NOTIFICATION_LOGIN = DOMAIN + "api/notification_token";
    public static final String URL_NEWS_TOPIC ="https://mysterycircus.jp/topics/";
    public static final String URL_FOOD = " https://mysterycircus.jp/food";
    public static final String URL_GOODS = "http://www.scrapgoods.jp/fs/scrap/c/tmc/";
    public static final String URL_INQUIRY = "https://mysterycircus.jp/contact";
    public static final String URL_CHECK_INVITATION_STATUS = DOMAIN + "api/check_invitation";
    public static final String URL_CHECK_MENU_AVAILALBE = DOMAIN + "api/check_available_status";
}