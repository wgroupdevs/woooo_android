package pk.muneebahmad.lib.services;

import android.os.AsyncTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 12:06 pm 04/10/2023
 * <code>class</code> ServiceManager.java
 */
public class ServiceManager {

    private static final String TAG = ServiceManager.class.getSimpleName() + ".java";
    private static final int THREAD_POOL_SIZE = 15;

    private static ServiceManager sInstance = null;

    private final ExecutorService executors = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    private ServiceManager() { }

    /**
     *
     * @param t
     */
    public void execute(Thread t) {
        executors.execute(t);
    }

    /**
     *
     * @param r
     */
    public void execute(Runnable r) {
        executors.execute(r);
    }

    /**
     *
     * @return
     */
    public static ServiceManager sharedManager() {
        synchronized (ServiceManager.class) {
            if (sInstance == null) {
                sInstance = new ServiceManager();
            }
            return sInstance;
        }
    }

} /** end class. */
