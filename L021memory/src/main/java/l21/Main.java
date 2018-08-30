package l21;

/**
 * VM options -Xmx512m -Xms512m
 * -XX:+UseCompressedOops //on
 * -XX:-UseCompressedOops //off
 * <p>
 * Runtime runtime = Runtime.getRuntime();
 * long mem = runtime.totalMemory() - runtime.freeMemory();
 * <p>
 * System.gc()
 * <p>
 * jconsole, connect to pid
 */
@SuppressWarnings({"RedundantStringConstructorCall", "InfiniteLoopStatement"})
public class Main {
    public static void main(String... args) throws InterruptedException {
 //16 or 24 with -XX:-UseCompressedOops

        Stand.explore(new TreeMapFactory(0));
        Stand.explore(new TreeMapFactory(1));
        Stand.explore(new TreeMapFactory(2));
        Stand.explore(new TreeMapFactory(5));
        Stand.explore(new TreeMapFactory(10));

        Stand.explore(new HashMapFactory(0));
        Stand.explore(new HashMapFactory(1));
        Stand.explore(new HashMapFactory(2));
        Stand.explore(new HashMapFactory(5));
        Stand.explore(new HashMapFactory(10));

        Stand.explore(new ArrayListFactory(0));
        Stand.explore(new ArrayListFactory(1));
        Stand.explore(new ArrayListFactory(2));
        Stand.explore(new ArrayListFactory(5));
        Stand.explore(new ArrayListFactory(10));

        Stand.explore(new LinkedListFactory(0));
        Stand.explore(new LinkedListFactory(1));
        Stand.explore(new LinkedListFactory(2));
        Stand.explore(new LinkedListFactory(5));
        Stand.explore(new LinkedListFactory(10));

        Stand.explore(new HashSetFactory(0));
        Stand.explore(new HashSetFactory(1));
        Stand.explore(new HashSetFactory(2));
        Stand.explore(new HashSetFactory(5));
        Stand.explore(new HashSetFactory(10));

        Stand.explore(new TreeSetFactory(0));
        Stand.explore(new TreeSetFactory(1));
        Stand.explore(new TreeSetFactory(2));
        Stand.explore(new TreeSetFactory(5));
        Stand.explore(new TreeSetFactory(10));

        Stand.explore(new TypeFactory<Integer>(Integer.class,10));
        Stand.explore(new TypeFactory<Short>(Short.class,(short)10));
        Stand.explore(new TypeFactory<Long>(Long.class,(long)10));
        Stand.explore(new TypeFactory<Double>(Double.class,(double)10));
        Stand.explore(new TypeFactory<Float>(Float.class,(float)10));
        Stand.explore(new TypeFactory<Byte>(Byte.class,(byte)10));
        Stand.explore(new TypeFactory<String>(String.class,"a"));

        Stand.explore(new StringFactory(1));
        Stand.explore(new StringFactory(2));
        Stand.explore(new StringFactory(3));
        Stand.explore(new StringFactory(10));
        Stand.explore(new StringFactory(20));

        Stand.explore(new TestClassFactory());
    }

}