package gc;

public class GCDemo {

    private static final long KB = 1000;
    private static final long MB = KB * KB;
    private static final Runtime ENVIRONMENT = Runtime.getRuntime();

    public static void info() {
        final long freeMemory = ENVIRONMENT.freeMemory();
        final long totalMemory = ENVIRONMENT.totalMemory();
        final long maxMemory = ENVIRONMENT.maxMemory();
        System.out.println("=== Environment state ===");
        System.out.printf("Free: %d%n", freeMemory / MB);
        System.out.printf("Total: %d%n", totalMemory / MB);
        System.out.printf("Max: %d%n", maxMemory / MB);
    }

    public static void main(String[] args) {
        System.out.println("PERSON---------");
        info();
        for (int i = 0; i < 10; i++) {
            new Person(i, "N" + i);
        }
        System.gc();
        info();
        System.out.println("PERSON---------" + System.lineSeparator());
        System.out.println(System.lineSeparator() + "USER---------");
        info();
        User user = new User(1, Department.ARCHITECTURE);
        System.out.println("User object in memory: " + user.userMemCalc() + " byte");
        for (int i = 0; i < 100; i++) {
            if (i <= 25) {
                new User(i, Department.ARCHITECTURE);
            }
            if (i <= 50 && i > 25) {
                new User(i, Department.COMMUNAL);
            }
            if (i <= 75 && i > 50) {
                new User(i, Department.PUBLICRELATIONS);
            }
            if (i > 75) {
                new User(i, Department.SOCIETY);
            }
        }
        System.gc();
        info();
        System.out.println("USER---------");
    }
}
