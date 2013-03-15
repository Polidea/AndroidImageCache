package pl.polidea.webimageview.processor;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
abstract class BitmapProcessorCreationChain {

    protected static final Processor NOT_CREATED_PROCESSOR = null;

    protected abstract BitmapProcessorCreationChain next();

    protected abstract Processor create();

    public static BitmapProcessorCreationChain startChain(Context context, AttributeSet attributeSet){
        return new ProgramaticallyCreated(context, attributeSet);
    }

    public Processor createProcessor(){
        Processor processor = create();
        if(processor == NOT_CREATED_PROCESSOR){
            processor = next().createProcessor();
        }
        return processor;
    }
}
