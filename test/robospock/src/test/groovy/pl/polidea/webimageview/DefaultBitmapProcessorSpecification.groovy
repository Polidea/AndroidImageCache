package pl.polidea.webimageview

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import com.xtremelabs.robolectric.Robolectric
import com.xtremelabs.robolectric.shadows.ShadowBitmapFactory
import pl.polidea.imagecache.R
import pl.polidea.robospock.RoboSpecification
import pl.polidea.robospock.UseShadows
import shadows.HighDensityShadowResources
import shadows.MyShadowActivityManager
import shadows.MyShadowBitmap
import shadows.MyShadowBitmapFactory

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
@UseShadows([MyShadowActivityManager, HighDensityShadowResources])
class DefaultBitmapProcessorSpecification extends RoboSpecification {

    LayoutInflater inflater;
    def name = "testBitmapName"
    def file = new File(name)

    def setup() {
        inflater = LayoutInflater.from(Robolectric.application);
        MyShadowBitmap.shouldThrowException = false
        MyShadowBitmapFactory.shouldThrowException = false
        file.createNewFile()
    }

    def cleanup() {
        file.delete()
        ShadowBitmapFactory.reset()
    }

    def "should process two wrap content"() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._wrap_contents);

        when:
        final DefaultBitmapProcessor.ProcessorType type = processor.determineProcessor().type;

        then:
        DefaultBitmapProcessor.ProcessorType.ORIGNAL == type;
    }

    def testProcessingTwoMatchParent() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._match_parents);

        when:
        final DefaultBitmapProcessor.ProcessorType type = processor.determineProcessor().type;

        then:
        DefaultBitmapProcessor.ProcessorType.ORIGNAL == type;
    }

    def testProcessingFixedWidthWithWrapConent() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_width_wrap);

        when:
        final DefaultBitmapProcessor.ProcessorType type = processor.determineProcessor().type;

        then:
        DefaultBitmapProcessor.ProcessorType.FIX_WIDTH == type;
    }

    def testProcessingFixedWidthWithMatchParent() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_width_match);

        when:
        final DefaultBitmapProcessor.ProcessorType type = processor.determineProcessor().type;

        then:
        DefaultBitmapProcessor.ProcessorType.FIX_WIDTH == type;
    }

    def testProcessingFixedHeightWithWrapContent() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_height_wrap);

        when:
        final DefaultBitmapProcessor.ProcessorType type = processor.determineProcessor().type;

        then:
        DefaultBitmapProcessor.ProcessorType.FIX_HEIGHT == type;
    }

    def testProcessingFixedHeightWithMatchParent() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_height_match);

        when:
        final DefaultBitmapProcessor.ProcessorType type = processor.determineProcessor().type;

        then:
        DefaultBitmapProcessor.ProcessorType.FIX_HEIGHT == type;
    }

    def testProcessingFixedHeightAndWidth() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_both);

        when:
        final DefaultBitmapProcessor.ProcessorType type = processor.determineProcessor().type;

        then:
        DefaultBitmapProcessor.ProcessorType.FIX_BOTH == type;
    }

    def testProcessingNoAttributes() {
        given:
        final DefaultBitmapProcessor processor = new DefaultBitmapProcessor(new WebImageView(Robolectric.application));

        when:
        final DefaultBitmapProcessor.ProcessorType type = processor.determineProcessor().type;

        then:
        DefaultBitmapProcessor.ProcessorType.ORIGNAL == type
    }

    def testProcessingDipsAndPixels() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._dips_and_pix);
        final int width = 100;
        final int height = 80;
        ShadowBitmapFactory.provideWidthAndHeightHints(file.getPath(), width, height);

        when:
        final Bitmap bitmap = processor.process(file);

        then:
        bitmap.getWidth() == 25
        bitmap.getHeight() == 20
    }

    def testProcessingOriginal() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._wrap_contents);
        final int width = 50;
        final int height = 60;
        ShadowBitmapFactory.provideWidthAndHeightHints(file.getPath(), width, height);

        when:
        final Bitmap bitmap = processor.process(file);

        then:
        bitmap.width == 50;
        bitmap.height == 60;
    }

    def testProcessingFixedWidth() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_width_wrap);
        final int width = 100;
        final int height = 80;
        ShadowBitmapFactory.provideWidthAndHeightHints(file.getPath(), width, height);

        when:
        final Bitmap bitmap = processor.process(file);

        then:
        bitmap.getWidth() == 60;
        bitmap.getHeight() == 48;
    }

    def testProcessingFixedHeight() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_height_wrap);
        final int width = 100;
        final int height = 80;
        ShadowBitmapFactory.provideWidthAndHeightHints(file.getPath(), width, height);

        when:
        final Bitmap bitmap = processor.process(file);

        then:
        75 == bitmap.getWidth();
        60 == bitmap.getHeight();
    }

    def testProcessingFixedBoth() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_both);
        final int width = 100;
        final int height = 80;
        ShadowBitmapFactory.provideWidthAndHeightHints(file.getPath(), width, height);

        when:
        final Bitmap bitmap = processor.process(file);

        then:
        47 == bitmap.getWidth();
        38 == bitmap.getHeight();
    }

    def testCalculatingDips() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_both);

        when:
        final int value = processor.calculateValue("40dip");

        then:
        60 ==  value;
    }

    def testCalculatingDps() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_both);

        when:
        final int value = processor.calculateValue("40dp");

        then:
        60 ==  value;
    }

    def testCalculatingPix() {
        given:
        final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_both);

        when:
        final int value = processor.calculateValue("60px");

        then:
        60 == value;
    }

    DefaultBitmapProcessor getProcessor(final int id) {
        final WebImageView view = getView(id);
        // TODO: place here implementation of reading xml
        // TODO: arguments can be passed via Mockito
        final DefaultBitmapProcessor defaultBitmapProcessor = new DefaultBitmapProcessor(view);
        return defaultBitmapProcessor;
    }

    WebImageView getView(final int id) {
        final View inflate = inflater.inflate(R.layout.for_tests, null);
        return (WebImageView) inflate.findViewById(id);
    }
}
