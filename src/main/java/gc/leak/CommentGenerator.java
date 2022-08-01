package gc.leak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommentGenerator implements Generate {

    private static List<Comment> comments = new ArrayList<>();
    private static List<String> phrases;

    public String pathPhrases = "src/main/java/gc/leak/files/phrases.txt";
    public String separator = System.lineSeparator();
    public int count = 50;
    private UserGenerator userGenerator;
    private Random random;

    public CommentGenerator(Random random, UserGenerator userGenerator) {
        this.userGenerator = userGenerator;
        this.random = random;
        read();
    }

    private void read() {
        try {
            phrases = read(pathPhrases);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static List<Comment> getComments() {
        return comments;
    }

    @Override
    public void generate() {
        comments.clear();
        List<Integer> ints = new ArrayList<>();
        random.ints(0, phrases.size())
                .distinct().limit(3).forEach(ints::add);
        for (int i = 0; i < count; i++) {
            String comment = phrases.get(ints.get(0)) + separator
                    + phrases.get(ints.get(1)) + separator
                    + phrases.get(ints.get(2));
            comments.add(new Comment(comment,
                    userGenerator.randomUser()));
        }
    }
}
