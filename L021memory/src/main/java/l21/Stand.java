package l21;

import java.lang.management.ManagementFactory;

public class Stand {
    public static void explore(GeneralObjectFactory SFactory) throws InterruptedException{
        System.out.println("pid: " + ManagementFactory.getRuntimeMXBean().getName());

        int size = 2000000;

        System.out.println("Starting the loop");
        long mem = getMem();
        System.out.println("Mem: " + mem);

        Object[] array = new Object[size];

        long mem2 = getMem();
        System.out.println("Ref size: " + (mem2 - mem) / array.length);

        for (int i = 0; i < array.length; i++) {
            array[i] = SFactory.get();
        }

        long mem3 = getMem();
        System.out.println("Element size: " + (mem3 - mem2) / array.length);

        array = null;
        System.out.println("Array is ready for GC");
        System.out.println("---------------------");

        Thread.sleep(1000); //wait for 1 sec
    }

    private static long getMem() throws InterruptedException {
        System.gc();
        Thread.sleep(10);
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

}
