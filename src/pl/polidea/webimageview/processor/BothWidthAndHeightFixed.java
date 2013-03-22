package pl.polidea.webimageview.processor;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
class BothWidthAndHeightFixed extends AbstractBitmapProcessorCreationChain {

    private final int height;

    private final int width;

    public BothWidthAndHeightFixed(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public AbstractBitmapProcessorCreationChain next() {
        return new OnlyHeightFixed(height, width);
    }

    @Override
    protected Processor create() {
        if (width * height > 0) {
            return new Processor(Processor.ProcessorType.FIX_BOTH, width, height);
        }
        return NOT_CREATED_PROCESSOR;
    }
}
