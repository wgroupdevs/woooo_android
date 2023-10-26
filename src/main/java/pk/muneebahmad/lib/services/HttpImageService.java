package pk.muneebahmad.lib.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import pk.muneebahmad.lib.graphics.CachedImageLoaderListener;
import pk.muneebahmad.lib.net.HttpImageAdapter;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 12:02 pm 04/10/2023
 * <code>class</code> HttpImageService.java
 */
public final class HttpImageService {

    private static final String TAG = HttpImageService.class.getSimpleName() + ".java";

    protected HttpImageService() {}

    /**
     *
     * @param context
     * @param autoCache
     * @param url
     * @param adapter
     * @param listener
     * @return
     */
    public static Runnable getImage(
            final Context context,
            final boolean autoCache,
            final String url,
            final HttpImageAdapter adapter,
            CachedImageLoaderListener listener) {
        if (autoCache && ImageCacheService.isImageCached(context, url)) {
            return ImageCacheService.loadCachedImage(context, url, listener);
        } else {
            Runnable runnable = new Runnable() {
                Bitmap bitmap = null;
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "Http Image Thread Startup ... on " + new Date().toString());
                        Thread.sleep(100);
                        URL u = new URL(url);
                        InputStream is = u.openConnection().getInputStream();
                        if (adapter != null)  adapter.connected(url);
                        bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                    } catch (InterruptedException | IOException ex) {
                        ex.printStackTrace();
                        if (adapter != null) adapter.failed(url, ex.getMessage());
                    } finally {
                        if (autoCache) {
                            ServiceManager.sharedManager().execute(ImageCacheService.cacheImage(context, url, bitmap));
                        }
                        if (adapter != null) {
                            adapter.done(url, bitmap);
                        }
                    }
                }
            };
            return runnable;
        }
    }
} /** end class. */
