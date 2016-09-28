package spring.batch.boot;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by chokst on 3/5/15.
 */
@Component
public class IOMFeedWriter implements ItemWriter<OfferFeed> {

    /*
     After reader and processor reads and processes some records(chunk number), writer can write all those records (one chunk) together.
     So, writer accepts a list of processed objects.
    */
    @Override
    public void write(List<? extends OfferFeed> feeds) throws Exception {
        for(OfferFeed feed : feeds) {
            List<Offer> offers = feed.getOffers();
            for(Offer offer : offers) {
                System.out.println(offer.getId());
            }

        }
    }
}