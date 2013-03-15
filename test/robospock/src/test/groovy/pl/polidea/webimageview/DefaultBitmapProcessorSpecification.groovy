package pl.polidea.webimageview

import static pl.polidea.webimageview.DefaultBitmapProcessor.ProcessorType.FIX_BOTH
import static pl.polidea.webimageview.DefaultBitmapProcessor.ProcessorType.FIX_HEIGHT
import static pl.polidea.webimageview.DefaultBitmapProcessor.ProcessorType.FIX_WIDTH
import static pl.polidea.webimageview.DefaultBitmapProcessor.ProcessorType.ORIGNAL

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
import spock.lang.Unroll

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
@UseShadows([MyShadowActivityManager, HighDensityShadowResources])
class DefaultBitmapProcessorSpecification extends RoboSpecification {

    def LayoutInflater inflater;
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

    @Unroll
    def "should have #expectedProcessorType for id #idResId"() {
        when:
        DefaultBitmapProcessor.ProcessorType type = getProcessor(idResId).determineProcessor().type;

        then:
        type == expectedProcessorType;

        where:
        idResId                  | expectedProcessorType
        R.id._wrap_contents      | ORIGNAL
        R.id._match_parents      | ORIGNAL
        R.id._fixed_width_wrap   | FIX_WIDTH
        R.id._fixed_width_match  | FIX_WIDTH
        R.id._fixed_height_wrap  | FIX_HEIGHT
        R.id._fixed_height_match | FIX_HEIGHT
        R.id._fixed_both         | FIX_BOTH
    }

    def "should have ORIGINAL type by default, when no attributes passed to processor"() {
        given:
        final DefaultBitmapProcessor processor = new DefaultBitmapProcessor(new WebImageView(Robolectric.application));

        when:
        final DefaultBitmapProcessor.ProcessorType type = processor.determineProcessor().type;

        then:
        ORIGNAL == type
    }

    @Unroll
    def "should be w:#bitmapWidth h:#bitmapHeight for input w:#inputWidth h:#inputHeight and processor type #processorTypeAsString"() {
        when:
        final DefaultBitmapProcessor processor = getProcessor(processorTypeResId);
        ShadowBitmapFactory.provideWidthAndHeightHints(file.getPath(), inputWidth, inputHeight);
        final Bitmap bitmap = processor.process(file);

        then:
        bitmap.getWidth() == bitmapWidth
        bitmap.getHeight() == bitmapHeight

        where:
        processorTypeAsString | processorTypeResId      | inputWidth | inputHeight | bitmapWidth | bitmapHeight
        "_dips_and_pix"       | R.id._dips_and_pix      | 100        | 80          | 25          | 20
        "_wrap_contents"      | R.id._wrap_contents     | 50         | 60          | 50          | 60
        "_fixed_width_wrap"   | R.id._fixed_width_wrap  | 100        | 80          | 60          | 48
        "_fixed_height_wrap"  | R.id._fixed_height_wrap | 100        | 80          | 75          | 60
        "_fixed_both"         | R.id._fixed_both        | 100        | 80          | 47          | 38
    }

    @Unroll
    def "should calculate = #expectedValue for input as string = #inputAsString"() {
        when:
        DefaultBitmapProcessor processor = getProcessor(R.id._fixed_both);
        int value = processor.calculateValue(inputAsString);

        then:
        value == expectedValue;

        where:
        processorTypeAsString | processorTypeResId | inputAsString | expectedValue
        "_fixed_both"         | R.id._fixed_both   | "40dip"       | 60
        "_fixed_both"         | R.id._fixed_both   | "40dp"        | 60
        "_fixed_both"         | R.id._fixed_both   | "60px"        | 60
    }

    DefaultBitmapProcessor getProcessor(final int id) {
        final WebImageView view = getView(id);
        // TODO: place here implementation of reading xml
        // TODO: arguments can be passed via Mockito
        return new DefaultBitmapProcessor(view);
    }

    WebImageView getView(final int id) {
        final View inflate = inflater.inflate(R.layout.for_tests, null);
        return (WebImageView) inflate.findViewById(id);
    }
}
