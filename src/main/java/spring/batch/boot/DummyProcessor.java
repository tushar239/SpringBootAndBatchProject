package spring.batch.boot;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by chokst on 3/5/15.
 */
@Component
public class DummyProcessor implements ItemProcessor<String, String>{

    @Override
    public String process(String s) throws Exception {
        return s;
    }
}
