package gc;

import org.openjdk.jol.info.GraphLayout;

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
        for (int i = 0; i < 1; i++) {
            new Person(i, "N" + i);
        }
        System.gc();
        info();
        System.out.println("PERSON---------" + System.lineSeparator());

        System.out.println("USER---------");
        info();

        int count = 0;
        for (int i = 0; i < 3000; i++) {
            if (i < 250) {
                System.out.println(GraphLayout.parseInstance(new User(i + 1, (i + 16) * i)).toFootprint());
                System.out.println("Молодые объекты находятся в Eden и места им там хватает, "
                        + "переполнения нет, соответственно GC молчит и объекты не стирает из памяти");
            }
            if (i > 1000 && i < 2000) {
                System.out.println(GraphLayout.parseInstance(new User(i + 1, (i + 16) * i)).toFootprint());
                System.out.println("В данном случае GC  начинает работать, так как райское место Eden переполнено"
                        + "а в выжившие (Survivor) объекты без ссылок направлять некошерно, "
                        + "а потому все объекты из Eden приговорены к MinorGC");
            }
            if (i >= 2000) {
                String[] user = new String[3000];
                user[i] = String.valueOf(new User(i + 1, (i + 16) * i));
                System.out.println("А вот эти граждане - объекты точно встретят старость сначала "
                        + "до определенных итераций в зоне выживших, а затем и вовсе попадут к олдам, "
                        + "так как имеют теплое местечко в массиве (а это объект с выделенной памятью)"
                        + "соответственно и ссылка рабочая имеется - а это билет к бессмертию (на время жизни программы)");
            }
        }
        info();
        System.out.println("USER---------");
    }
}
