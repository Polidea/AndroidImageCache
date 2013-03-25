package pl.polidea.webimageview.processor;

import pl.polidea.utils.Dimensions;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
public class ProcessorFactory {

    public Processor createProcessor(Dimensions dimensions) {
        return BitmapProcessorCreationChain.startChain(dimensions).createProcessor();
    }
}
