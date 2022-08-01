package gc.leak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserGenerator implements Generate {

    private static List<User> users = new ArrayList<>();

    public String pathNames = "src/main/java/gc/leak/files/names.txt";
    public String pathSurnames = "src/main/java/gc/leak/files/surnames.txt";
    public String pathPatrons = "src/main/java/gc/leak/files/patr.txt";

    public String separator = " ";
    public int newUsers = 1000;
    public List<String> names;
    public List<String> surnames;
    public List<String> patrons;

    private Random random;

    public UserGenerator(Random random) {
        this.random = random;
        readAll();
    }

    @Override
    public void generate() {
        users.clear();
        for (int i = 0; i < newUsers; i++) {
            users.add(new User(
                    surnames.get(random.nextInt(surnames.size())) + separator
                            + names.get(random.nextInt(names.size())) + separator
                            + patrons.get(random.nextInt(patrons.size()))));
        }
    }

    private void readAll() {
        try {
            names = read(pathNames);
            surnames = read(pathSurnames);
            patrons = read(pathPatrons);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public User randomUser() {
        return users.get(random.nextInt(users.size()));
    }

    public static List<User> getUsers() {
        return users;
    }
}
