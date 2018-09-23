package l06;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class MyCacheEngine<K,V>  implements CacheEngine<K,V> {

    private static final int TIME_THRESHOLD_MS = 5;


    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;

    private final Map<K, SoftReference<CacheElement<K,V>>> elements = new LinkedHashMap<>();
    private final Timer timer = new Timer();

    private int hit = 0;
    private int miss = 0;

    MyCacheEngine(long lifeTimeMs, long idleTimeMs, int maxElements) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0;
    }

    MyCacheEngine(long lifeTimeMs, long idleTimeMs){
        this(lifeTimeMs, idleTimeMs, Integer.MAX_VALUE);
    }

    MyCacheEngine(){
        this(0L, 0L, Integer.MAX_VALUE);
    }

    @Override
    public void put(K key, V val) {
        if (elements.size() == maxElements) {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        CacheElement<K,V> element = new CacheElement<>(key,val);
        elements.put(key, new SoftReference<>(element));

        if (!isEternal) {
            if (lifeTimeMs != 0) {
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }
            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
                timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
            }
        }
    }

    @Override
    public V get(K key) {
        SoftReference<CacheElement<K,V>> ref = elements.get(key);
        if(ref != null){
            return extractElement(ref.get());
        }
        else{
            miss++;
            return null;
        }
    }

    private V extractElement(CacheElement<K,V> ce){
        if(ce != null){
            hit++;
            ce.setAccessed();
            return ce.getValue();
        }
        else{
            miss++;
            return null;
        }
    }

    @Override
    public int getHitCount() {
        return this.hit;
    }

    @Override
    public int getMissCount() {
        return this.miss;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private class CacheElement<K,V>{
        private final K key;
        private final V value;
        private final long creationTime;
        private long lastAccessTime;


        public CacheElement(K key, V value) {
            this.key = key;
            this.value = value;
            this.creationTime = getCurrentTime();
            this.lastAccessTime = getCurrentTime();
        }

        protected long getCurrentTime() {
            return System.currentTimeMillis();
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public long getCreationTime() {
            return creationTime;
        }

        public long getLastAccessTime() {
            return lastAccessTime;
        }

        public void setAccessed() {
            lastAccessTime = getCurrentTime();
        }
    }

    private TimerTask getTimerTask(final K key, Function<CacheElement<K,V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                CacheElement<K, V> element = elements.get(key).get();
                if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                    elements.remove(key);
                    this.cancel();
                }
            }
        };
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }
}
