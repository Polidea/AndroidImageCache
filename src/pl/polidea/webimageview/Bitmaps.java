package pl.polidea.webimageview;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class Bitmaps {

    public Bitmap generateBitmap(final String path, final int desiredWidth, final int desiredHeight) {
        return getBitmap(path, getOptions(path), desiredWidth, desiredHeight);
    }

    public Bitmap generateScaledWidthBitmap(final String path, final int width) {
        final Options options = getOptions(path);
        final float scale = options.outWidth / (float) width;
        final int height = (int) (options.outHeight / scale);
        return getBitmap(path, options, width, height);
    }

    public Bitmap generateScaledHeightBitmap(final String path, final int height) {
        final Options options = getOptions(path);
        final float scale = options.outHeight / (float) height;
        final int width = (int) (options.outWidth / scale);
        return getBitmap(path, options, width, height);
    }

    public Bitmap generateBitmap(final String path) {
        return getBitmap(path, getOptions(path), getOptions(path).outWidth, getOptions(path).outHeight);
    }

    private Options getOptions(final String path) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        return options;
    }

    private Bitmap getBitmap(final String path, final Options options, final float desiredWidth,
            final float desiredHeight) {

        final float scale;
        int width, height;
        final int orignalHeight = options.outHeight;
        final int orignalWidth = options.outWidth;
        final float scaleH = orignalHeight / desiredHeight;
        final float scaleW = orignalWidth / desiredWidth;

        scale = Math.max(1, Math.max(scaleH, scaleW));

        width = (int) (orignalWidth / scale);
        height = (int) (orignalHeight / scale);
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) scale;
        options.inScaled = false;

        final Bitmap decodeFile;
        try {
            decodeFile = BitmapFactory.decodeFile(path, options);
        } catch (final OutOfMemoryError e) {
            return null;
        }

        if (decodeFile == null) {
            return null;
        }

        final Bitmap scaledBitmap;
        try {
            scaledBitmap = Bitmap.createScaledBitmap(decodeFile, width, height, true);
        } catch (final OutOfMemoryError e) {
            return null;
        }
        if (scaledBitmap == null) {
            return null;
        }

        if (scaledBitmap != decodeFile) {// LOL :)
            decodeFile.recycle();
        }

        final int orientation = getOrientation(path);
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
    }

    private int getOrientation(final String path) {
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
