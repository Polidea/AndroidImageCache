package pl.polidea.webimageview.processor;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
public class ProcessorFactory {

    public Processor createProcessor(Context context, AttributeSet attributeSet) {
        return AbstractBitmapProcessorCreationChain.startChain(context, attributeSet).createProcessor();
    }
}
