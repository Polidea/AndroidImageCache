package pl.polidea.webimageview.processor;

import android.graphics.Bitmap;
import pl.polidea.webimageview.BitmapDecodeException;
import pl.polidea.webimageview.Bitmaps;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@pl.polidea.pl>
 */
public class Processor {

    ProcessorType type;

    int width;

    int height;


    Processor(final ProcessorType type) {
        this.type = type;
    }

    Processor(final ProcessorType type, final int width, final int height) {
        this.type = type;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Processor [type=" + type + ", width=" + width + ", height=" + height + "]";
    }

    public Bitmap processBitmap(Bitmaps bitmaps) throws BitmapDecodeException {
        return type.processBitmap(bitmaps, width, height);
    }

    /**
     * @author Mateusz Grzechociński <mateusz.grzechocinski@pl.polidea.pl>
     */
    public enum ProcessorType {
        ORIGNAL {
            @Override
            public Bitmap processBitmap(Bitmaps bitmaps, int width, int height) throws BitmapDecodeException {
                return bitmaps.generateBitmap();
            }
        },
        FIX_WIDTH {
            @Override
            public Bitmap processBitmap(Bitmaps bitmaps, int width, int height) throws BitmapDecodeException {
                return bitmaps.generateScaledWidthBitmap(width);
            }
        },
        FIX_HEIGHT {
            @Override
            public Bitmap processBitmap(Bitmaps bitmaps, int width, int height) throws BitmapDecodeException {
                return bitmaps.generateScaledHeightBitmap(height);
            }
        },
        FIX_BOTH {
            @Override
            public Bitmap processBitmap(Bitmaps bitmaps, int width, int height) throws BitmapDecodeException {
                return bitmaps.generateBitmap(width, height);
            }
        };

        public abstract Bitmap processBitmap(Bitmaps bitmaps, int width, int height) throws BitmapDecodeException;
    }
}
