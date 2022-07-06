package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {

    private static Properties properties;
    private static Connection connection;

    private Connection initConnection() throws SQLException, ClassNotFoundException {
        Class.forName(properties.getProperty("driver"));
        return DriverManager.getConnection(
                properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password"));
    }

    private void initStatement(String sql) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("TableEditor: initStatement(): Table is missing: " + e);
        }
    }

    public void createTable(String tableName) {
        String sql = String.format(
                "Create table if not exists %s(%s);",
                tableName,
                "id serial primary key",
                "created_date timestamp");
        initStatement(sql);
    }

    public void getProperties() {
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Integer getInterval(String path) {
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

    public void scheduleStarter() throws SchedulerException, InterruptedException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        JobDataMap data = new JobDataMap();
        data.put("connection", connection);
        JobDetail job = newJob(Rabbit.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(getInterval("rabbit.interval"))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
        Thread.sleep(10000);
        scheduler.shutdown();
    }

    public static void main(String[] args) throws SchedulerException, InterruptedException, SQLException, ClassNotFoundException {
        AlertRabbit rabbit = new AlertRabbit();
        rabbit.getProperties();
        rabbit.initConnection();
        rabbit.createTable("rabbit");
        rabbit.scheduleStarter();
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
        }
    }
}
