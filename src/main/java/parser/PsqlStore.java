package parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());
    private Connection cnn;

    public PsqlStore(Properties cfg) throws ClassNotFoundException {
        try {
            Class.forName(cfg.getProperty("driver-class-name"));
        } catch (Exception e) {
            LOG.error("Exception in: " + e);
        }
        try {
            cnn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password"));
        } catch (SQLException sqle) {
            LOG.error("SQLException in: " + sqle);
        }
    }

    public static Properties getConfig() {
        Properties config = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            config.load(in);
        } catch (IOException ioe) {
            LOG.error("IOException in: " + ioe);
        }
        return config;
    }

    public static void main(String[] args) {
        try {
            PsqlStore psqlStore = new PsqlStore(getConfig());
            psqlStore.createTable(psqlStore.getCnn());
            HabrCareerParse habrCareerParse = new HabrCareerParse();
            String link = String.format("%s/vacancies/java_developer", "https://career.habr.com");
            for (Post vacancy : habrCareerParse.list(link)) {
                psqlStore.save(vacancy);
                System.out.println(vacancy);
            }
        } catch (ClassNotFoundException cnfe) {
            LOG.error("ClassNotFoundException in: " + cnfe);
        }
    }

    public void createTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String sql = String.format(
                    "create table if not exists post(%s, %s, %s, %s, %s);",
                    "id serial primary key",
                    "name varchar(255)",
                    "text text",
                    "link text unique",
                    "created timestamp"
            );
            statement.execute(sql);
        } catch (SQLException sqle) {
            LOG.error("SQLException in: " + sqle);
        }
    }

    public Post getPost(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String text = resultSet.getString(3);
        String link = resultSet.getString(4);
        LocalDateTime created = resultSet.getTimestamp(5).toLocalDateTime();
        return new Post(id, name, text, link, created);
    }

    @Override
    public void save(Post post) {
        String sql = "insert into post(name, text, link, created) values (?, ?, ?, ?);";
        try (PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getLink());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            ps.execute();
        } catch (SQLException sqle) {
            LOG.error("SQLException in: " + sqle);
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> postList = new ArrayList<>();
        String sql = "select * from post;";
        try (PreparedStatement prepState = cnn.prepareStatement(sql)) {
            ResultSet resultSet = prepState.executeQuery();
            while (resultSet.next()) {
                postList.add(getPost(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postList;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        String sql = "select * from post where id = ?;";
        try (PreparedStatement prepState = cnn.prepareStatement(sql)) {
            prepState.setInt(1, id);
            ResultSet resultSet = prepState.executeQuery();
            while (resultSet.next()) {
                post = getPost(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public Connection getCnn() {
        return cnn;
    }
}


