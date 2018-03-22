package eplus.scrap.EventBus;

import eplus.scrap.model.TourDetail;

/**
 * Created by nals-anhdv on 12/6/17.
 */

public class GetTourDetailEvent {
    public final TourDetail tourDetail;

    public GetTourDetailEvent(TourDetail tourDetail) {
        this.tourDetail = tourDetail;
    }
}
