package pl.polidea.webimageview.processor;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
class ProgramaticallyCreated extends AbstractBitmapProcessorCreationChain {

    private final AttributeSet attributeSet;

    private final Context context;

    public ProgramaticallyCreated(Context context, AttributeSet attributeSet) {
        this.context = context;
        this.attributeSet = attributeSet;
    }

    public AbstractBitmapProcessorCreationChain next(){
        return new BothWidthAndHeightNotFixed(context, attributeSet);
    }

    @Override
    protected Processor create() {
        if (attributeSet == null) {
            return new Processor(Processor.ProcessorType.ORIGNAL);
        }
        return NOT_CREATED_PROCESSOR;
    }
}
