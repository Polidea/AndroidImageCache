package pl.polidea.webimageview;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;

import pl.polidea.imagecache.ImageCacheTestRunner;
import pl.polidea.imagecache.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.xtremelabs.robolectric.Robolectric;

@RunWith(ImageCacheTestRunner.class)
public class DefaultBitmapProcessorTest {

    @Test
    public void testReadingWebImageViewParameters() {
        final Context ctx = Robolectric.application;
        final View root = LayoutInflater.from(ctx).inflate(R.layout.for_tests, null);
        final WebImageView imageView = (WebImageView) root.findViewById(R.id._match_parents);
        final DefaultBitmapProcessor processor = new DefaultBitmapProcessor(imageView);
        final LayoutParams layoutParams = imageView.getLayoutParams();
        final String attributeIntValue = imageView.attrs.getAttributeValue("xxx", "layout_width");
        fail();
    }

    @Test
    public void testGeneratingDefaultBitmap() {
        fail();
    }

    @Test
    public void testProcessingBitmapWithFixedSizes() {
        fail();
    }

    @Test
    public void testProcessingBitmapWithFixedWidth() {

    }

    @Test
    public void testProcessingBitmapWithFixedHeight() {
        fail();
    }

}
