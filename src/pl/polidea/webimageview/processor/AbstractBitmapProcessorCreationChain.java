package pl.polidea.webimageview.processor;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
abstract class AbstractBitmapProcessorCreationChain {

    protected static final Processor NOT_CREATED_PROCESSOR = null;

    public static AbstractBitmapProcessorCreationChain startChain(Context context, AttributeSet attributeSet) {
        return new ProgramaticallyCreated(context, attributeSet);
    }

    protected abstract AbstractBitmapProcessorCreationChain next();

    protected abstract Processor create();

    public Processor createProcessor() {
        Processor processor = create();
        if (processor == NOT_CREATED_PROCESSOR) {
            processor = next().createProcessor();
        }
        return processor;
    }
}
