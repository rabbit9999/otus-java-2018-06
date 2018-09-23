package l06;

/**
 * VM options: -Xmx256m -Xms256m
 */

public class Main {
    public static void main(String ... args) throws InterruptedException{
        new Main().OutOfMemoryTesting();
//        new Main().lifeCacheExample();
//        new Main().eternalCacheExample();
    }

    private void eternalCacheExample() {
        int size = 6;
        CacheEngine<Integer, String> cache = new MyCacheEngine<>(0, 0, size);

        for (int i = 0; i < 10; i++) {
            cache.put(i, "String: " + i);
        }


        for (int i = 0; i < 10; i++) {
            String element = cache.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        cache.dispose();
    }

    private void lifeCacheExample() throws InterruptedException {
        int size = 5;
        CacheEngine<Integer, String> cache = new MyCacheEngine<>(1000, 0, size);

        for (int i = 0; i < size; i++) {
            cache.put(i, "String: " + i);
        }

        for (int i = 0; i < size; i++) {
            String element = cache.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        Thread.sleep(1000);

        for (int i = 0; i < size; i++) {
            String element = cache.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        cache.dispose();
    }

    private void OutOfMemoryTesting() {
        CacheEngine<Integer, BigObject> cache = new MyCacheEngine<>(0, 0);

        for (int i = 0; i < 200; i++) {
            cache.put(i, new BigObject());
        }


        for (int i = 0; i < 200; i++) {
            BigObject element = cache.get(i);
            System.out.println("Result for " + i + ": " + (element != null ? "OK" : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        cache.dispose();
    }


    class BigObject {
        final byte[] array = new byte[1024 * 1024];

        public byte[] getArray() {
            return array;
        }
    }
}
