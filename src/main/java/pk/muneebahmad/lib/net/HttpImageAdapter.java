package pk.muneebahmad.lib.net;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 12:05 pm 04/10/2023
 * <code>interface</code> HttpImageAdapter.java
 */
public interface HttpImageAdapter extends Serializable {

    /**
     * 1
     * @param resource
     */
    void connected(String resource);

    /**
     *
     * @param resource
     * @param reasonPhrase
     */
    void failed(String resource, String reasonPhrase);

    /**
     *
     * @param resource
     * @param bitmap
     */
    void done(String resource, Bitmap bitmap);

} /** end class. */
