package spring.batch.boot;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by chokst on 3/5/15.
 */
@Component
public class DummyWriter implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> strs) throws Exception {
        System.out.println(strs);
    }
}
