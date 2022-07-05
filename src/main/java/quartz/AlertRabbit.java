package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {

    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(10)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            JobDetail rabbitJob = newJob(Rabbit.class).build();
            SimpleScheduleBuilder rabbitTime = simpleSchedule()
                    .withIntervalInSeconds(getInterval("rabbit.properties"))
                    .withRepeatCount(2);
            Trigger rabbitTrigger = newTrigger()
                    .startNow()
                    .withSchedule(rabbitTime)
                    .build();
            scheduler.scheduleJob(rabbitJob, rabbitTrigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    public static Integer getInterval(String path) {
        String interval = "-1";
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream(path)) {
            Properties config = new Properties();
            config.load(in);
            interval = config.getProperty("rabbit.interval");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(interval);
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
        }
    }
}
