package pl.polidea.webimageview;

import java.io.File;

import pl.polidea.webimageview.WebImageView.BitmapProcessor;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class DefaultBitmapProcessor implements BitmapProcessor {

    private final static int MATCH = LayoutParams.MATCH_PARENT;
    private final static int WRAP = LayoutParams.WRAP_CONTENT;

    WebImageView webImageView;
    public static final int[] attrsArray = new int[] { android.R.attr.layout_width, // 0
            android.R.attr.layout_height // 1
    };

    public DefaultBitmapProcessor(final WebImageView webImageView) {
        this.webImageView = webImageView;
    }

    @Override
    public Bitmap process(final File pathToBitmap) {
        final Bitmaps bitmaps = new Bitmaps();
        final Processor processor = determineProcessor();
        Bitmap bitmap = null;
        final String path = pathToBitmap.getPath();

        switch (processor.type) {
        case FIX_BOTH:
            bitmap = bitmaps.generateBitmap(path, processor.width, processor.height);
            break;
        case FIX_HEIGHT:
            bitmap = bitmaps.generateScaledHeightBitmap(path, processor.height);
            break;
        case FIX_WIDTH:
            bitmap = bitmaps.generateScaledWidthBitmap(path, processor.width);
            break;
        case ORIGNAL:
            bitmap = bitmaps.generateBitmap(path);
            break;
        }
        return bitmap;
    }

    Processor determineProcessor() {
        final AttributeSet attrs = webImageView.attrs;
        if (attrs == null) {
            return new Processor(ProcessorType.ORIGNAL);
        }

        final TypedArray ta = webImageView.getContext().obtainStyledAttributes(attrs, attrsArray);

        final int width = ta.getDimensionPixelSize(0, ViewGroup.LayoutParams.MATCH_PARENT);
        final int height = ta.getDimensionPixelSize(1, ViewGroup.LayoutParams.MATCH_PARENT);
        if (height + width < 0) {
            return new Processor(ProcessorType.ORIGNAL);
        }

        if (width * height > 0) {
            return new Processor(ProcessorType.FIX_BOTH, width, height);
        } else if (width > 0 && (height == WRAP || height == MATCH)) {
            return new Processor(ProcessorType.FIX_WIDTH, width, height);
        } else if (height > 0 && (width == WRAP || width == MATCH)) {
            return new Processor(ProcessorType.FIX_HEIGHT, width, height);
        }

        return new Processor(ProcessorType.ORIGNAL);
    }

    public enum ProcessorType {
        ORIGNAL,
        FIX_WIDTH,
        FIX_HEIGHT,
        FIX_BOTH
    }

    public static class Processor {
        ProcessorType type;
        int width;
        int height;

        public Processor(final ProcessorType type) {
            this.type = type;
        }

        public Processor(final ProcessorType type, final int width, final int height) {
            this.type = type;
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return "Processor [type=" + type + ", width=" + width + ", height=" + height + "]";
        }

    }

}
