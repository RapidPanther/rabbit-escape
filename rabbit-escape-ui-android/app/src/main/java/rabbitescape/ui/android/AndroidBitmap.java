package rabbitescape.ui.android;

import rabbitescape.render.androidlike.Bitmap;

public class AndroidBitmap implements Bitmap
{
    public final android.graphics.Bitmap bitmap;

    public AndroidBitmap( android.graphics.Bitmap bitmap )
    {
        this.bitmap = bitmap;
    }

    @Override
    public String name()
    {
        return null;
    }

    @Override
    public int width()
    {
        return bitmap.getWidth();
    }

    @Override
    public int height()
    {
        return bitmap.getHeight();
    }

    @Override
    public void recycle()
    {
        bitmap.recycle();
    }
}
