package pl.polidea.webimageview.processor;

import pl.polidea.utils.Dimensions;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
class ProgramaticallyCreated extends BitmapProcessorCreationChain {

    private final Dimensions dimensions;

    public ProgramaticallyCreated(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public BitmapProcessorCreationChain next(){
        return new BothWidthAndHeightNotFixed(dimensions);
    }

    @Override
    protected Processor create() {
        if (dimensions == null) {
            return new Processor(Processor.ProcessorType.ORIGNAL);
        }
        return NOT_CREATED_PROCESSOR;
    }
}
