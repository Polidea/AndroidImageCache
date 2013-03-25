package pl.polidea.webimageview.processor;

import pl.polidea.utils.Dimensions;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
class BothWidthAndHeightNotFixed extends BitmapProcessorCreationChain {

    private final int height;

    private final int width;

    public BothWidthAndHeightNotFixed(Dimensions dimensions) {
        this.height = dimensions.height;
        this.width = dimensions.width;
    }

    @Override
    public BitmapProcessorCreationChain next() {
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
