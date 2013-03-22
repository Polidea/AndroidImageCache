package pl.polidea.webimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import java.io.File;
import pl.polidea.webimageview.processor.BitmapProcessor;
import pl.polidea.webimageview.processor.Processor;
import pl.polidea.webimageview.processor.ProcessorFactory;

/**
 * @author Przemysław Jakubczyk <przemyslaw.jakubczyk@polidea.pl>
 */
public class DefaultBitmapProcessor implements BitmapProcessor {

    private final Context context;

    private final AttributeSet attributeSet;

    public DefaultBitmapProcessor(Context context, AttributeSet attributeSet) {
        this.context = context;
        this.attributeSet = attributeSet;
    }

    @Override
    public Bitmap process(final File pathToBitmap) throws BitmapDecodeException {
        final Bitmaps bitmaps = new Bitmaps(pathToBitmap.getPath());
        final Processor processor = determineProcessor();
        return processor.processBitmap(bitmaps);
    }

    private Processor determineProcessor() {
        return new ProcessorFactory().createProcessor(context, attributeSet);
    }
}
