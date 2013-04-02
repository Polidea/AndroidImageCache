package pl.polidea.webimageview.processor;

import pl.polidea.utils.Dimensions;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@pl.polidea.pl>
 */
public class ProcessorFactory {

    public Processor createProcessor(Dimensions dimensions) {
        return AbstractBitmapProcessorCreationChain.startChain(dimensions).createProcessor();
    }
}
