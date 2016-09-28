package spring.batch.boot;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chokst on 3/5/15.
 */

/*
Looks like there are two types of BatchConfigurers - DefaultBatchConfigurer and BasicBatchConfigurer
DefaultBatchConfigurer - If you have datasource set, then it uses datasource and JobRepository, otherwise it uses in-memory(MapJobRepositoryFactoryBean).
BasicBatchConfigurer - It requires datasource as JobRepository.

@EnableBatchProcessing imports a configuration BatchConfigurationSelector.
BatchConfigurationSelector sets up SimpleBatchConfiguration or ModularBatchConfiguration and it sets the basic components by extracting them from the bean of type BatchConfigurer in it, defaulting to some sensible values as long as a unique DataSource is available.

Tasklet oriented spring batch example
http://projects.spring.io/spring-batch/

Chunk oriented spring batch example
http://spring.io/guides/gs/batch-processing/

JobExecution
	jobParameters - params passed during execution of the job through command line
	jobInstance
	Collection<StepExecution> - StepExecution is available for every step's reader/processor/writer
	ExecutionContext - you can put any key-value pair during the job inside ExecutionContext which is available from StepExecution also.




*/

@Configuration
@EnableBatchProcessing
@ComponentScan
public class BatchConfiguration extends DefaultBatchConfigurer {

    @Autowired
    private IOMFeedReader iomFeedReader;

    @Autowired
    private IOMFeedProcessor iomFeedProcessor;

    @Autowired
    private IOMFeedWriter iomFeedWriter;

    @Autowired
    private DummyReader dummyReader;

    @Autowired
    private DummyProcessor dummyProcessor;

    @Autowired
    private DummyWriter dummyWriter;


    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public Job job() {
        return jobs.get("myJob").start(step1(steps, iomFeedReader, iomFeedWriter, iomFeedProcessor))
                .next(step2(steps, dummyReader, dummyWriter, dummyProcessor))
                .build();
    }
    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, final ItemReader<List<IOMIncentive>> itemReader,
                      final ItemWriter<OfferFeed> itemWriter, final ItemProcessor<List<IOMIncentive>, OfferFeed> itemProcessor) {
        final StepBuilder stepBuilder= stepBuilderFactory.get("step1");


        Step step1 = stepBuilder.tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                try {
                    List<IOMIncentive> iomIncentives = itemReader.read();
                    if (iomIncentives != null) {
                        OfferFeed offerFeed = itemProcessor.process(iomIncentives);
                        itemWriter.write(Arrays.asList(offerFeed));
                    }
                } finally {
                    // After step execution, if you need to do anthing
                }
                    //return RepeatStatus.CONTINUABLE; // means more work to do so call the same step again
                return null; // it is same as RepeatStatus.FINISHED
                // There is RepeatStatus to repeat the same step and there is ExitStatus. You can set custom ExitStatus("custom") or ExitStatus.COMPLETED OR ExitStatus.FAILED.
                // Based on ExitStatus, you can decide what should be the next step. Look at SpringBatchProject's PrintTasklet for more information.
                }
            }).build();

            return step1;
        }

        @Bean
    public Step step2(StepBuilderFactory stepBuilderFactory, final ItemReader<String> itemReader,
                      final ItemWriter<String> itemWriter, final ItemProcessor<String, String> itemProcessor) {

            // Chunk oriented
            /*
            StepBuilder step21 = stepBuilderFactory.get("step2");
            SimpleStepBuilder<String, String> chunk = step21.chunk(1);
            chunk.reader(itemReader);
            chunk.processor(itemProcessor);
            chunk.writer(itemWriter);
            TaskletStep taskletStep = chunk.build();
            return taskletStep;
            */

            //OR
            /*
            return stepBuilderFactory.get("step2")
                .<String, String> chunk(1)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
            */

            // Tasklet oriented
            final StepBuilder stepBuilder= stepBuilderFactory.get("step2");
            Step step2 = stepBuilder.tasklet(new Tasklet() {
                @Override
                public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                    try {
                        String str = itemReader.read();
                        if (str != null) {
                            String str1 = itemProcessor.process(str);
                            itemWriter.write(Arrays.asList(str1));
                        }
                    } finally {
                        // After step execution, if you need to do anthing
                    }
                    //return RepeatStatus.CONTINUABLE; // means more work to do so call the same step again
                    return null; // it is same as RepeatStatus.FINISHED
                }
            }).build();

            return step2;
        }


}
