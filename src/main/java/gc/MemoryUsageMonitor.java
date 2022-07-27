package gc;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class MemoryUsageMonitor {

    public static void main(String[] args) {
        System.out.println(ClassLayout.parseClass(User.class).toPrintable());
        System.out.println("====================");
        System.out.println(ClassLayout.parseClass(Department.class).toPrintable());
        System.out.println("====================");
        System.out.println(ClassLayout.parseClass(String.class).toPrintable());
    }
}
