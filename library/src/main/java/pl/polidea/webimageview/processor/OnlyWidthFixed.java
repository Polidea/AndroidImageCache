package pl.polidea.webimageview.processor;

import android.view.ViewGroup;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@pl.polidea.pl>
 */
class OnlyWidthFixed extends AbstractBitmapProcessorCreationChain {

    private final int height;

    private final int width;

    public OnlyWidthFixed(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public AbstractBitmapProcessorCreationChain next() {
        return new Unknown();
    }

    @Override
    protected Processor create() {
        if (width > 0 && (height == ViewGroup.LayoutParams.WRAP_CONTENT || height == ViewGroup.LayoutParams.MATCH_PARENT)) {
            return new Processor(Processor.ProcessorType.FIX_WIDTH, width, height);
        }
        return NOT_CREATED_PROCESSOR;
    }
}
