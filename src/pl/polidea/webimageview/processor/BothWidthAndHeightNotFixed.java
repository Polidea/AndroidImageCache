package pl.polidea.webimageview.processor;

import pl.polidea.utils.Dimensions;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
class BothWidthAndHeightNotFixed extends AbstractBitmapProcessorCreationChain {

    private final int height;

    private final int width;

    public BothWidthAndHeightNotFixed(Dimensions dimensions) {
        this.height = dimensions.heightPX;
        this.width = dimensions.widthPX;
    }

    @Override
    public AbstractBitmapProcessorCreationChain next() {
        return new BothWidthAndHeightFixed(height, width);

    }

    @Override
    protected Processor create() {
        if (height + width < 0) {
            return new Processor(Processor.ProcessorType.ORIGNAL);
        }
        return NOT_CREATED_PROCESSOR;
    }


}
