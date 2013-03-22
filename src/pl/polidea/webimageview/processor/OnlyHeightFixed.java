package pl.polidea.webimageview.processor;

import android.view.ViewGroup;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
class OnlyHeightFixed extends AbstractBitmapProcessorCreationChain {

    private final int height;

    private final int width;

    public OnlyHeightFixed(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public AbstractBitmapProcessorCreationChain next() {
        return new OnlyWidthFixed(height, width);
    }

    @Override
    protected Processor create() {
        if (height > 0 && (width == ViewGroup.LayoutParams.WRAP_CONTENT || width == ViewGroup.LayoutParams.MATCH_PARENT)) {
            return new Processor(Processor.ProcessorType.FIX_HEIGHT, width, height);
        }
        return NOT_CREATED_PROCESSOR;
    }
}
