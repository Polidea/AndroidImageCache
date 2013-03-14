package pl.polidea.webimageview;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import java.io.File;
import pl.polidea.webimageview.WebImageView.BitmapProcessor;

public class DefaultBitmapProcessor implements BitmapProcessor {

    public static final int[] attrsArray = new int[]{android.R.attr.layout_width, // 0
            android.R.attr.layout_height, android.R.attr.id // 1
    };
    private final static int MATCH = LayoutParams.MATCH_PARENT;
    private final static int WRAP = LayoutParams.WRAP_CONTENT;
    WebImageView webImageView;

    public DefaultBitmapProcessor(final WebImageView webImageView) {
        this.webImageView = webImageView;
    }

    @Override
    public Bitmap process(final File pathToBitmap) throws BitmapDecodeException {
        final Bitmaps bitmaps = new Bitmaps(pathToBitmap.getPath());
        final Processor processor = determineProcessor();
        Bitmap bitmap = null;

        switch (processor.type) {
            case FIX_BOTH:
                bitmap = bitmaps.generateBitmap(processor.width, processor.height);
                break;
            case FIX_HEIGHT:
                bitmap = bitmaps.generateScaledHeightBitmap(processor.height);
                break;
            case FIX_WIDTH:
                bitmap = bitmaps.generateScaledWidthBitmap(processor.width);
                break;
            case ORIGNAL:
                bitmap = bitmaps.generateBitmap();
                break;
        }
        return bitmap;
    }

    Processor determineProcessor() {
        if (webImageView.attrs == null) {
            return new Processor(ProcessorType.ORIGNAL);
        }
        final String layout_height = webImageView.layout_height;

        final String layout_width = webImageView.layout_width;

        final int width = guessValue(layout_width);
        final int height = guessValue(layout_height);
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

    int guessValue(final String layout_width) {
        try {
            if ("match_parent".equals(layout_width) || "fill_parent".equals(layout_width)) {
                return MATCH;
            } else if ("wrap_content".equals(layout_width)) {
                return WRAP;
            } else {
                return calculateValue(layout_width);
            }
        } catch (final NumberFormatException e) {
            return MATCH;
        }
    }

    int calculateValue(final String value) {
        final DisplayMetrics displayMetrics = webImageView.getResources().getDisplayMetrics();
        if (value.endsWith("dp")) {
            final String number = value.substring(0, value.length() - 2);
            return (int) ((Float.parseFloat(number) * displayMetrics.density) + 0.5);
        } else if (value.endsWith("dip")) {
            final String number = value.substring(0, value.length() - 3);
            return (int) ((Float.parseFloat(number) * displayMetrics.density) + 0.5);
        } else if (value.endsWith("px")) {
            final String number = value.substring(0, value.length() - 2);
            return (int) Float.parseFloat(number);
        }

        return 0;
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
