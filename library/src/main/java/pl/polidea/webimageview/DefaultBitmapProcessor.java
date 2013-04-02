package pl.polidea.webimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import java.io.File;
import pl.polidea.utils.Dimensions;
import pl.polidea.webimageview.processor.BitmapProcessor;
import pl.polidea.webimageview.processor.Processor;
import pl.polidea.webimageview.processor.ProcessorFactory;

/**
 * @author Przemysław Jakubczyk <przemyslaw.jakubczyk@pl.polidea.pl>
 */
public class DefaultBitmapProcessor implements BitmapProcessor {

    private final Dimensions dimensions;

    public DefaultBitmapProcessor(Context context, AttributeSet attributeSet) {
        this.dimensions = Dimensions.fromAttributesSet(context, attributeSet);
    }

    @Override
    public Bitmap process(final File pathToBitmap) throws BitmapDecodeException {
        final Bitmaps bitmaps = new Bitmaps(pathToBitmap.getPath());
        final Processor processor = determineProcessor();
        return processor.processBitmap(bitmaps);
    }

    private Processor determineProcessor() {
        return new ProcessorFactory().createProcessor(dimensions);
    }
}
