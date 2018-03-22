package eplus.scrap.EventBus;

/**
 * Created by nals-anhdv on 11/30/17.
 */

public class GetLinkPickupEvent {
    public final String link_image;
    public final String url_topic;

    public GetLinkPickupEvent(String link_image,String url_topic) {
        this.link_image = link_image;
        this.url_topic = url_topic;
    }
}
