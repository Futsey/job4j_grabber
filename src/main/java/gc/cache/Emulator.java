package gc.cache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;

public class Emulator {

    /**Сделать два класса. Один собирает по относительному пути, второй по абсолютному пути.
     * Сделать их по интерфейсу (фабричный метод)
     */
    static final int SHOWALLDIR = 1;
    static final int SELECTDIR = 2;
    static final int READCACHE = 3;
    static final int WRITECACHE = 4;
    static final int EXIT = 5;
    public static final String SEPARATOR = System.lineSeparator();
    private Scanner scanner = new Scanner(System.in);

    private String menu = """
            Select item to be done:
            1. Show all directories and files
            2. Select directory
            3. Try to read from cache
            4. Write to cache
            5. Exit
            """;

    public static void main(String[] args) throws IOException {
        Emulator emulator = new Emulator();
        emulator.mainMenu();
    }

    public void mainMenu() throws IOException {
        DirFileCache dirFileCache = new DirFileCache("./src");
        int button;
        System.out.println(menu);
        button = scanner.nextInt();
        switch (button) {
            case SHOWALLDIR:
                listFiles(Path.of("./src"));
                System.out.println("Back to main menu".concat(SEPARATOR));
                mainMenu();
                break;
            case SELECTDIR:
                dirFileCache = new DirFileCache(getDirectory());
                System.out.println("Back to main menu".concat(SEPARATOR));
                mainMenu();
                break;
            case READCACHE:
                readFileFromCache(dirFileCache);
                mainMenu();
                break;
            case WRITECACHE:
                addFileInCache(dirFileCache);
                mainMenu();
                break;
            case EXIT:
                exit();
                break;
            default:
                System.out.println("Wrong item selected");
        }
    }

    private String getDirectory() throws IOException {
        String dir;
        System.out.println("Please select directory: ");
        dir = scanner.next();
        if (!Files.exists(Path.of(dir))) {
            System.out.println("Selected directory doesn`t exist. Back to main menu".concat(SEPARATOR));
            mainMenu();
        }
        Files.list(Path.of(dir))
                .forEach(System.out::println);
        return dir;
    }

    private void addFileInCache(DirFileCache dirFileCache) {
        System.out.println("Enter path of file to add in cache memory");
        String file = scanner.next();
        dirFileCache.put(file, dirFileCache.get(file));
    }

    private void readFileFromCache(DirFileCache dirFileCache) {
        System.out.println("Enter path of file to read from cache memory");
        String file = scanner.next();
        System.out.println(dirFileCache.get(file));
    }

    private void listFiles(Path path) throws IOException {
        try (Stream<Path> walk = Files.walk(path)) {
            walk.filter(Files::isRegularFile)
                    .forEach(System.out::println);
        }
    }

    private void exit() {
        System.out.println("Good bye!");
    }
}