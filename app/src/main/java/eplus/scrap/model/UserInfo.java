package eplus.scrap.model;

/**
 * Created by nals-anhdv on 10/3/17.
 */

public class UserInfo {


    /**
     * url_download_app : https://mysterycircus.jp/app
     * url_invite_friend : https://mysterycircus.jp/app
     * title_share : The real-life room escape [Tokyo Mystery Circus] is open now at Kabuki-cho, ShinjukuGet coupons from app and let's gohttps://mysterycircus.jp/app
     * title_invite : The real-life room escape [Tokyo Mystery Circus] is open now at Kabuki-cho, ShinjukuGet coupons from app and let's goInvitation code: 266222 https://mysterycircus.jp/app
     * invitation_code : 266222
     * already_enter_invitation_code : true
     * already_share_app : true
     * status_share : 1
     * status_invite : 3
     */

    private String url_download_app;
    private String url_invite_friend;
    private String title_share;
    private String title_invite;
    private String invitation_code;
    private boolean already_enter_invitation_code;
    private boolean already_share_app;
    private int status_share;
    private int status_invite;

    public String getUrl_download_app() {
        return url_download_app;
    }

    public void setUrl_download_app(String url_download_app) {
        this.url_download_app = url_download_app;
    }

    public String getUrl_invite_friend() {
        return url_invite_friend;
    }

    public void setUrl_invite_friend(String url_invite_friend) {
        this.url_invite_friend = url_invite_friend;
    }

    public String getTitle_share() {
        return title_share;
    }

    public void setTitle_share(String title_share) {
        this.title_share = title_share;
    }

    public String getTitle_invite() {
        return title_invite;
    }

    public void setTitle_invite(String title_invite) {
        this.title_invite = title_invite;
    }

    public String getInvitation_code() {
        return invitation_code;
    }

    public void setInvitation_code(String invitation_code) {
        this.invitation_code = invitation_code;
    }

    public boolean isAlready_enter_invitation_code() {
        return already_enter_invitation_code;
    }

    public void setAlready_enter_invitation_code(boolean already_enter_invitation_code) {
        this.already_enter_invitation_code = already_enter_invitation_code;
    }

    public boolean isAlready_share_app() {
        return already_share_app;
    }

    public void setAlready_share_app(boolean already_share_app) {
        this.already_share_app = already_share_app;
    }

    public int getStatus_share() {
        return status_share;
    }

    public void setStatus_share(int status_share) {
        this.status_share = status_share;
    }

    public int getStatus_invite() {
        return status_invite;
    }

    public void setStatus_invite(int status_invite) {
        this.status_invite = status_invite;
    }
}
