package pl.polidea.webimageview.processor;

import pl.polidea.utils.Dimensions;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
abstract class BitmapProcessorCreationChain {

    protected static final Processor NOT_CREATED_PROCESSOR = null;

    public static BitmapProcessorCreationChain startChain(Dimensions dimensions) {
        return new ProgramaticallyCreated(dimensions);
    }

    protected abstract BitmapProcessorCreationChain next();

    protected abstract Processor create();

    public Processor createProcessor() {
        Processor processor = create();
        if (processor == NOT_CREATED_PROCESSOR) {
            processor = next().createProcessor();
        }
        return processor;
    }
}
