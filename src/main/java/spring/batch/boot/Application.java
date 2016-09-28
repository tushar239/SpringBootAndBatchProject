package spring.batch.boot;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

/**
 * Created by chokst on 3/5/15.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(BatchConfiguration.class, args);

        JobParameters jobParameters = new DefaultJobParametersConverter().getJobParameters(StringUtils.splitArrayElementsIntoProperties(args, "="));
        executeJob(ctx, jobParameters);
    }
    private static void executeJob(ConfigurableApplicationContext ctx, JobParameters parameters) {
        Job job = ctx.getBean(Job.class);
        JobLauncher launcher = ctx.getBean(JobLauncher.class);
        try {
            JobExecution jobExecution = launcher.run(job, parameters);
            System.out.println(jobExecution.getId()+":"+jobExecution.getStatus());
        }catch(Exception e) {
            e.printStackTrace();
        }
        //SomeClass<String, String> someClass = getSomeClass();

    }

    /*public static <I, O>  SomeClass<I, O>  getSomeClass() {
        return new SomeClass<I, O>();
    }*/
}
