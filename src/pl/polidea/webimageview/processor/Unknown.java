package pl.polidea.webimageview.processor;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
class Unknown extends BitmapProcessorCreationChain {

    @Override
    public BitmapProcessorCreationChain next() {
        throw new UnsupportedOperationException("Reached last element of chain");
    }

    @Override
    protected Processor create() {
        return new Processor(Processor.ProcessorType.ORIGNAL);
    }
}
