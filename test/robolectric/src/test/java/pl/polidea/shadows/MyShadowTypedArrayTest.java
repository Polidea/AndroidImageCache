package pl.polidea.shadows;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.polidea.imagecache.ImageCacheTestRunner;

@RunWith(ImageCacheTestRunner.class)
public class MyShadowTypedArrayTest {

    private MyShadowTypedArray myShadowTypedArray;

    @Before
    public void setup() {
        myShadowTypedArray = new MyShadowTypedArray();
    }

    @Test
    public void testCalculatingDips() {
        // when
        final int value = myShadowTypedArray.calculateValue("40dip");

        // then
        assertEquals(60, value);
    }

    @Test
    public void testCalculatingDps() {
        // when
        final int value = myShadowTypedArray.calculateValue("40dp");

        // then
        assertEquals(60, value);
    }

    @Test
    public void testCalculatingPix() {
        // when
        final int value = myShadowTypedArray.calculateValue("60px");

        // then
        assertEquals(60, value);
    }
}
