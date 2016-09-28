package spring.batch.boot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokst on 3/5/15.
 */
public class OfferFeed {
    private List<Offer> offers = new ArrayList<Offer>();

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public void addOffer(Offer offer) {
        offers.add(offer);
    }
}
