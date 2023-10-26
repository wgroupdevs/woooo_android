package pk.muneebahmad.lib.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 11:46 am 04/10/2023
 * <code>class</code> CircleDrawable.java
 */
public final class CircleDrawable extends Drawable {

    private final Bitmap bitmap;
    private final Paint paint;
    private final RectF rectF;
    private final int bitmapWidth;
    private final int bitmapHeight;

    /**
     *
     * @param bitmap
     */
    public CircleDrawable(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.rectF = new RectF();
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
        final BitmapShader shader = new BitmapShader(
                this.bitmap,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        this.paint.setShader(shader);
        this.bitmapWidth = this.bitmap.getWidth();
        this.bitmapHeight = this.bitmap.getHeight();
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawOval(rectF, paint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        rectF.set(bounds);
    }

    @Override
    public void setAlpha(int i) {
        if (paint.getAlpha() != i) {
            paint.setAlpha(i);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getIntrinsicWidth() {
        return bitmapWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return bitmapHeight;
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        paint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setDither(boolean dither) {
        paint.setDither(dither);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    /**
     *
     * @return
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     *
     * @param as
     */
    public void setAntiAlias(boolean as) {
        paint.setAntiAlias(as);
        invalidateSelf();
    }

} /** end class. */
