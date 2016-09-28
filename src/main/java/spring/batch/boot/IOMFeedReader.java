package spring.batch.boot;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokst on 3/5/15.
 */
@Component
public class IOMFeedReader implements ItemReader<List<IOMIncentive>> {
    @Override
    public List<IOMIncentive> read() throws Exception {
        List<IOMIncentive> iomIncentives = new ArrayList<IOMIncentive>();
        IOMIncentive iomIncentive1=new IOMIncentive();
        iomIncentive1.setId(1);
        iomIncentives.add(iomIncentive1);

        IOMIncentive iomIncentive2=new IOMIncentive();
        iomIncentive2.setId(2);
        iomIncentives.add(iomIncentive2);

        IOMIncentive iomIncentive3=new IOMIncentive();
        iomIncentive3.setId(3);
        iomIncentives.add(iomIncentive3);

        return iomIncentives;
    }
}
