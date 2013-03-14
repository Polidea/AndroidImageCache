package pl.polidea.webimageview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import java.io.File;
import java.io.IOException;

public class Bitmaps {

    String path;
    Options options;

    public Bitmaps(String path) {
        if (path == null || !new File(path).exists()) {
            throw new IllegalArgumentException("Can't find a bitmap under path: " + path);
        }

        this.path = path;
        options = getOptions();
    }

    public Bitmap generateBitmap() throws BitmapDecodeException {
        return getBitmap(options.outWidth, options.outHeight);
    }

    public Bitmap generateScaledWidthBitmap(int width) throws BitmapDecodeException {
        final float scale = options.outWidth / (float) width;
        final int height = (int) (options.outHeight / scale);
        return getBitmap(width, height);
    }

    public Bitmap generateScaledHeightBitmap(int height) throws BitmapDecodeException {
        final float scale = options.outHeight / (float) height;
        final int width = (int) (options.outWidth / scale);
        return getBitmap(width, height);
    }

    public Bitmap generateBitmap(int desiredWidth, int desiredHeight) throws BitmapDecodeException {
        return getBitmap(desiredWidth, desiredHeight);
    }

    Bitmap getBitmap(float desiredWidth, float desiredHeight) throws BitmapDecodeException {
        try {
            float scale;
            int width, height;
            int originalHeight = options.outHeight;
            int originalWidth = options.outWidth;
            float scaleH = originalHeight / desiredHeight;
            float scaleW = originalWidth / desiredWidth;

            scale = Math.max(1, Math.max(scaleH, scaleW));

            width = (int) (originalWidth / scale);
            height = (int) (originalHeight / scale);
            options.inJustDecodeBounds = false;
            options.inSampleSize = (int) scale;
            options.inScaled = false;

            Bitmap bitmapFromFile = BitmapFactory.decodeFile(path, options);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapFromFile, width, height, true);

            if (scaledBitmap != bitmapFromFile) {// LOL :)
                bitmapFromFile.recycle();
            }

            int orientation = getOrientation();
            if (orientation != 0) {
                final Bitmap rotatedBitmap;
                final Matrix matrix = new Matrix();
                matrix.postRotate(orientation);
                rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(),
                        matrix, true);

                scaledBitmap.recycle();

                return rotatedBitmap;
            }

            return scaledBitmap;
        } catch (OutOfMemoryError e) {
            throw new BitmapDecodeException();
        }
    }

    Options getOptions() {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        return options;
    }

    int getOrientation() {
        try {
            final ExifInterface exif = new ExifInterface(path);
            switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
            }

        } catch (final IOException e) {
        }
        return 0;
    }

}
