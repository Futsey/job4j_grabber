package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {

    private Connection initConnection(Properties config) throws SQLException, ClassNotFoundException {
        Class.forName(config.getProperty("driver"));
        return DriverManager.getConnection(
                config.getProperty("url"),
                config.getProperty("username"),
                config.getProperty("password"));
    }

    private void initStatement(String sql, Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("AlertRabbit: initStatement(): Table is missing: " + e);
        }
    }

    public void createTable(String tableName, Connection connection) {
        String sql = String.format(
                "Create table if not exists %s(%s, %s);",
                tableName,
                "id serial primary key",
                "created_date timestamp");
        initStatement(sql, connection);
    }

    public Properties getProperties() {
        Properties properties;
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return properties;
    }

    public void scheduleStarter(Properties config, Connection connection) throws SchedulerException, InterruptedException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        JobDataMap data = new JobDataMap();
        data.put("connection", connection);
        JobDetail job = newJob(Rabbit.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(config.getProperty("interval")))
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
        Properties config = rabbit.getProperties();
        try (Connection connection =  rabbit.initConnection(config)) {
            rabbit.createTable("rabbit", connection);
            rabbit.scheduleStarter(config, connection);
        }
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connection");
            try (PreparedStatement ps = ((Connection) context.getJobDetail().getJobDataMap()
                    .get("connection")).prepareStatement(
                    "insert into rabbit (created_date) values (?);"
            )) {
                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
