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
    private String defaultDirectory = "./src";
    private String dir;
    private int button;

    public static void main(String[] args) throws IOException {
        Emulator emulator = new Emulator();
        emulator.mainMenu();
    }

    public void mainMenu() throws IOException {
        DirFileCache dirFileCache = new DirFileCache(defaultDirectory);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select item to be done: "
                + System.lineSeparator()
                + "1. Show all directories and files"
                + System.lineSeparator()
                + "2. Select directory"
                + System.lineSeparator()
                + "3. Try to read from cache"
                + System.lineSeparator()
                + "4. Write to cache"
                + System.lineSeparator()
                + "5. Select file to read"
                + System.lineSeparator()
                + "6. Exit");
        button = scanner.nextInt();
        switch (button) {
            case 1:
                listFiles(Path.of(defaultDirectory));
                System.out.println("Back to main menu" + System.lineSeparator());
                mainMenu();
                break;
            case 2:
                dirFileCache = new DirFileCache(getDirectory());
                System.out.println("Back to main menu" + System.lineSeparator());
                mainMenu();
                break;
            case 3:
                readFileFromCache(dirFileCache);
                mainMenu();
                break;
            case 4:
                addFileInCache(dirFileCache);
                mainMenu();
                break;
            case 5:
                readFile(dirFileCache);
                mainMenu();
                break;
            case 6:
                exit();
                break;
            default:
                System.out.println("Wrong item selected");
        }
    }

    private String getDirectory() throws IOException {
        System.out.println("Please select directory: ");
        Scanner scanDir = new Scanner(System.in);
        dir = scanDir.next();
        if (!Files.exists(Path.of(dir))) {
            System.out.println("Selected directory doesn`t exist. Back to main menu" + System.lineSeparator());
            mainMenu();
        }
        Files.list(Path.of(dir))
                .forEach(System.out::println);
        return dir;
    }

    private void addFileInCache(DirFileCache dirFileCache) {
        System.out.println("Enter path of file to add in cache memory");
        Scanner scanFile = new Scanner(System.in);
        String file = scanFile.next();
        dirFileCache.put(file, dirFileCache.get(file));
    }

    private void readFileFromCache(DirFileCache dirFileCache) {
        System.out.println("Enter path of file to read from cache memory");
        Scanner scanFile = new Scanner(System.in);
        String file = scanFile.next();
        System.out.println(dirFileCache.getCache(file));
    }

    private void readFile(DirFileCache dirFileCache) {
        System.out.println("Enter path of file to read");
        Scanner scanFile = new Scanner(System.in);
        String file = scanFile.next();
        System.out.println(dirFileCache.load(file));
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