package pl.polidea.webimageview.processor;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
class Unknown extends AbstractBitmapProcessorCreationChain {

    @Override
    public AbstractBitmapProcessorCreationChain next() {
        throw new UnsupportedOperationException("Reached last element of chain");
    }

    @Override
    protected Processor create() {
        return new Processor(Processor.ProcessorType.ORIGNAL);
    }
}
