package gc.leak;

import java.util.Random;
import java.util.Scanner;

public class Menu {

    public static int addPost = 1;
    public static int addManyPost = 2;
    public static int showAllPosts = 3;
    public static int deletePost = 4;

    public static String SELECT = "Выберите меню";
    public static String COUNT = "Выберите количество создаваемых постов";
    public static String TEXT_OF_POST = "Введите текст";
    public static String EXIT = "Конец работы";

    public static String MENU = """
                Введите 1 для создание поста.
                Введите 2, чтобы создать определенное количество постов.
                Введите 3, чтобы показать все посты.
                Введите 4, чтобы удалить все посты.
                Введите любое другое число для выхода.
            """;

    public static void main(String[] args) {
        Random random = new Random();
        UserGenerator userGenerator = new UserGenerator(random);
        CommentGenerator commentGenerator = new CommentGenerator(random, userGenerator);
        Scanner scanner = new Scanner(System.in);
        PostStore postStore = new PostStore();
        start(commentGenerator, scanner, userGenerator, postStore);
    }

    private static void start(CommentGenerator commentGenerator, Scanner scanner, UserGenerator userGenerator, PostStore postStore) {
        boolean run = true;
        while (run) {
            System.out.println(MENU);
            System.out.println(SELECT);
            int userChoice = Integer.parseInt(scanner.nextLine());
            System.out.println(userChoice);
            if (addPost == userChoice) {
                System.out.println(TEXT_OF_POST);
                String text = scanner.nextLine();
                userGenerator.generate();
                commentGenerator.generate();
                postStore.add(new Post(text, CommentGenerator.getComments()));
            } else if (addManyPost == userChoice) {
                System.out.println(TEXT_OF_POST);
                String text = scanner.nextLine();
                System.out.println(COUNT);
                String count = scanner.nextLine();
                for (int i = 0; i < Integer.parseInt(count); i++) {
                    createPost(commentGenerator, userGenerator, postStore, text);
                }
            } else if (showAllPosts == userChoice) {
                System.out.println(PostStore.getPosts());
            } else if (deletePost == userChoice) {
                postStore.removeAll();
            } else {
                run = false;
                System.out.println(EXIT);
            }
        }
    }

    private static void createPost(CommentGenerator commentGenerator,
                                   UserGenerator userGenerator, PostStore postStore, String text) {
        userGenerator.generate();
        commentGenerator.generate();
        postStore.add(new Post(text, CommentGenerator.getComments()));
    }
}