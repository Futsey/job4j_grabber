package gc;

public class SizeOf {

    private static final Runtime SRUNTIME = Runtime.getRuntime();

    public static void main(String[] args) throws Exception {
        runGC();
        usedMemory();
        final int count = 100000;
        User[] objects = new User[count];
        long heap1 = 0;
        for (int i = -1; i < count; ++i) {
            User object = null;
            object = new User(i, new Department("Arch"));
            if (i >= 0) {
                objects[i] = object;
            } else {
                object = null;
                runGC();
                heap1 = usedMemory();
            }
        }
        runGC();
        long heap2 = usedMemory();
        final int size = Math.round(((float) (heap2 - heap1)) / count);
        System.out.println("'before' heap: " + heap1 + ", 'after' heap: " + heap2);
        System.out.println("heap delta: " + (heap2 - heap1)
                + ", {" + objects[0].getClass() + "} size = " + size + " bytes");
        int alignmentSize = size;
        alignment(alignmentSize);
        System.out.println("After alignment size = " + alignmentSize + " bytes");
        for (int i = 0; i < count; ++i) {
            objects[i] = null;
        }
        objects = null;
    }

    private static int alignment(int size) {
        if (size % 8 != 0) {
            do {
                size++;
            } while (size % 8 == 0);
        }
        return size;
    }

    private static void runGC() throws Exception {
        for (int r = 0; r < 4; ++r) {
            anotherRunGC();
        }
    }

    private static void anotherRunGC() throws Exception {
        long usedMem1 = usedMemory(), usedMem2 = Long.MAX_VALUE;
        for (int i = 0; (usedMem1 < usedMem2) && (i < 500); ++i) {
            SRUNTIME.runFinalization();
            SRUNTIME.gc();
            Thread.currentThread().yield();
            usedMem2 = usedMem1;
            usedMem1 = usedMemory();
        }
    }

    private static long usedMemory() {
        return SRUNTIME.totalMemory() - SRUNTIME.freeMemory();
    }
}
