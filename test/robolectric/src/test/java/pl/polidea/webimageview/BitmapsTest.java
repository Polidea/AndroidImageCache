package pl.polidea.webimageview;

import static org.junit.Assert.assertEquals;

import android.graphics.Bitmap;
import com.xtremelabs.robolectric.shadows.ShadowBitmapFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.polidea.imagecache.ImageCacheTestRunner;

@RunWith(ImageCacheTestRunner.class)
public class BitmapsTest {

    @Test
    public void testScalingBitampWithHeightAndWidth() {
        // given
        final String name = "test/robol";
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 36, 37);
        final Bitmaps bitmaps = new Bitmaps();

        // when
        final Bitmap generateBitmap = bitmaps.generateBitmap(name, 20, 20);

        // then
        assertEquals(20, generateBitmap.getHeight());
    }

    @Test
    public void testScalingBitmapWidthAspectRatioKept() {
        // given
        final String name = "test/robol";
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 1000, 800);
        final Bitmaps bitmaps = new Bitmaps();

        // when
        final Bitmap generateBitmap = bitmaps.generateScaledWidthBitmap(name, 320);

        // then
        assertEquals(256, generateBitmap.getHeight());
    }

    @Test
    public void testScalingBitmapHeightAspectRatioKept() {
        // given
        final String name = "test/robol";
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 1601, 1000);
        final Bitmaps bitmaps = new Bitmaps();

        // when
        final Bitmap generateBitmap = bitmaps.generateScaledHeightBitmap(name, 320);

        // then
        assertEquals(512, generateBitmap.getWidth());
    }

    @Test
    public void testReadingOriginalBitmap() {
        // given
        final String name = "test/robol";
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 1000, 800);
        final Bitmaps bitmaps = new Bitmaps();

        // when
        final Bitmap generateBitmap = bitmaps.generateBitmap(name);

        // then
        assertEquals(800, generateBitmap.getHeight());
        assertEquals(1000, generateBitmap.getWidth());
    }

}
