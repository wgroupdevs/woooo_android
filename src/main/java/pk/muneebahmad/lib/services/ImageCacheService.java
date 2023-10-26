package pk.muneebahmad.lib.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import pk.muneebahmad.lib.graphics.CachedImageLoaderListener;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 11:55 am 04/10/2023
 * <code>class</code> ImageCacheService.java
 */
public final class ImageCacheService {

    private static final String TAG = ImageCacheService.class.getSimpleName() + ".java";

    /**
     *
     * @param context
     * @param url
     * @param bitmap
     * @return
     */
    public static Runnable cacheImage(Context context, String url, final Bitmap bitmap) {
        File cacheDir = context.getCacheDir();
        String[] tokens = url.split("/");
        if (tokens != null && tokens.length > 0 && bitmap != null) {
            final String filename = tokens[tokens.length - 1];
            final File f = new File(cacheDir, filename);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "Caching image: [" + filename + "]");
                        Thread.sleep(100);
                        FileOutputStream fos = new FileOutputStream(f);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (InterruptedException | IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        Log.d(TAG, "Image [" + filename + "] cached successfully ...");
                    }
                }
            };
            return runnable;
        }
        return null;
    }

    /**
     *
     * @param context
     * @param url
     * @param cachedImageLoaderListener
     * @return
     */
    public static Runnable loadCachedImage(Context context, String url, final CachedImageLoaderListener cachedImageLoaderListener) {
        File cacheDir = context.getCacheDir();
        String[] tokens = url.split("/");
        if (tokens != null && tokens.length > 0 && cachedImageLoaderListener != null) {
            final String filename = tokens[tokens.length - 1];
            final File f = new File(cacheDir, filename);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "Loading image: [" + filename + "] from cache ...");
                        Thread.sleep(100);
                        FileInputStream fis = new FileInputStream(f);
                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
                        fis.close();
                        cachedImageLoaderListener.imageLoaded(bitmap);
                    } catch (InterruptedException | IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        Log.d(TAG, "Image [" + filename + "] loaded from cache ....");
                    }
                }
            };
            return runnable;
        }
        return null;
    }

    /**
     *
     * @param context
     * @param url
     * @return
     */
    public static boolean isImageCached(Context context, String url) {
        File cacheDir = context.getCacheDir();
        String[] tokens = url.split("/");
        if (tokens != null && tokens.length > 0) {
            String filename = tokens[tokens.length - 1];
            File f = new File(cacheDir, filename);
            return f.exists();
        }
        return false;
    }

} /** end class. */
