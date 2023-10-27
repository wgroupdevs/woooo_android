package pk.muneebahmad.lib.net;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import pk.muneebahmad.lib.graphics.CachedImageLoaderListener;
import pk.muneebahmad.lib.services.HttpImageService;
import pk.muneebahmad.lib.services.ServiceManager;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 12:10 pm 04/10/2023
 * <code>class</code> Http.java
 */
public final class Http {

    private static final String TAG = Http.class.getSimpleName() + ".java";

    private Http() {}

    /**
     *
     * @param context
     * @param enableAutoCache
     * @param url
     * @param adapter
     */
    public void getImage(
            Context context,
            boolean enableAutoCache,
            final String url,
            final HttpImageAdapter adapter) {
        ServiceManager.sharedManager().execute(HttpImageService.getImage(context, enableAutoCache, url, adapter, new CachedImageLoaderListener() {
            @Override
            public void imageLoaded(Bitmap bitmap) {
                if (adapter != null) adapter.done(url, bitmap);
            }
        }));
    }

    /**
     *
     * @param activity
     * @param url
     * @param enableAutoCache
     * @param circularCrop
     * @param iv
     */
    public void getImage(
            @NonNull Activity activity,
            @NonNull final String url,
            boolean enableAutoCache,
            boolean circularCrop,
            final ImageView iv) {
        ServiceManager.sharedManager().execute(HttpImageService.getImage(activity,
                enableAutoCache, url, new HttpImageAdapter() {
            @Override
            public void connected(String resource) {
                Log.d(TAG, "Connected to resource [" + url + "]");
            }

            @Override
            public void failed(String resource, String reasonPhrase) {
                Log.e(TAG, "Error while downloading image: [" + reasonPhrase + "]");
            }

            @Override
            public void done(String resource, final Bitmap bitmap) {
                Log.d(TAG, "Image [" + resource + "] downloaded successfully!");
                if (activity != null) {
                    activity.runOnUiThread(() -> iv.setImageBitmap(bitmap));
                }
            }
        }, new CachedImageLoaderListener() {
            @Override
            public void imageLoaded(final Bitmap bitmap) {
                Log.d(TAG, "Image [" + url + "] loaded from cache successfully ...");
                if (activity != null) {
                    activity.runOnUiThread(() -> iv.setImageBitmap(bitmap));
                }
            }
        }));
    }

    /**
     *
     * @return
     */
    public static Http build() {
        return new Http();
    }

} /** end class. */
