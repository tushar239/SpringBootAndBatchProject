package spring.batch.boot;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by chokst on 3/5/15.
 */
@Component
public class IOMFeedProcessor implements ItemProcessor<List<IOMIncentive>, OfferFeed> {

    @Override
    public OfferFeed process(List<IOMIncentive> incentivesList) throws Exception {
        OfferFeed offerFeed = new OfferFeed();

        for(IOMIncentive iomIncentive : incentivesList) {
            Offer offer = new Offer();
            offer.setId(iomIncentive.getId());
            offerFeed.addOffer(offer);
        }

        return offerFeed;
    }

}
