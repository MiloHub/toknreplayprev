import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Map which clears objects after a specified time. The objects are stored in the
 * underlying hashMap.  
 * 
 */
public final class TimeLimitedCacheMap {
        
    private final ConcurrentHashMap<String, Object> objectMap = new ConcurrentHashMap<String, Object>(10);
    private final ConcurrentHashMap<String, Long> timeMap = new ConcurrentHashMap<String, Long>();
    private final ReentrantReadWriteLock accessLock = new ReentrantReadWriteLock();
     private final Runnable evictor = new Runnable() {
        
      
        @Override
        public void run() {
            if(timeMap.isEmpty()){
                Thread.yield();
            }
            long currentTime = System.nanoTime();
            accessLock.writeLock().lock();
            Set<String> keys = new HashSet<String>(timeMap.keySet());
            accessLock.writeLock().unlock();
            Set<String> markedForRemoval = new HashSet<String>(10);
            for (String key : keys) {
                long lastTime = timeMap.get(key);
                if(lastTime == 0){
                    continue;
                }
                long interval = currentTime - lastTime;
                long elapsedTime = TimeUnit.NANOSECONDS.convert(interval, expiryTimeUnit);
                if(elapsedTime > expiryTime){
                    markedForRemoval.add(key);
                }
            }
            accessLock.writeLock().lock();
            for (String key : markedForRemoval) {
                long lastTime = timeMap.get(key);
                if(lastTime == 0){
                    continue;
                }
                long interval = currentTime - lastTime;
                long elapsedTime = TimeUnit.NANOSECONDS.convert(interval, expiryTimeUnit);
                if(elapsedTime > expiryTime){
                    remove(key);
                }
            }
            accessLock.writeLock().unlock();
        }
    };
    
    private final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor(new MyThreadFactory(true));
    private final class MyThreadFactory implements ThreadFactory {
        
        private boolean isDaemon = false;
        
        public MyThreadFactory(boolean daemon){
            isDaemon = daemon;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(isDaemon);
            return t;
        }        
        
    };
    private final long expiryTime;
    private final TimeUnit expiryTimeUnit;
    
   
    public TimeLimitedCacheMap(long initialDelay, long evictionDelay, long expiryTime, TimeUnit unit){
        timer.scheduleWithFixedDelay(evictor, initialDelay, evictionDelay, unit);
        this.expiryTime = expiryTime;
        this.expiryTimeUnit = unit;
    }

    
    public void put(String key, Object value) {        
        accessLock.readLock().lock();
        Long nanoTime = System.nanoTime();
        timeMap.put(key, nanoTime);
        objectMap.put(key, value);
        accessLock.readLock().unlock();        
    }

    
    public Object remove(Object key) {        
        accessLock.readLock().lock();
        Object value = objectMap.remove(key);
        timeMap.remove(key);
        accessLock.readLock().unlock();
        return value;
        
    }
    
    /* Clone Map
     */
    public Map<String, Object> getClonedMap(){
        accessLock.writeLock().lock();
        HashMap<String, Object> mapClone = new HashMap<String, Object>(objectMap);
        accessLock.writeLock().unlock();
        return Collections.unmodifiableMap(mapClone);
    }

}