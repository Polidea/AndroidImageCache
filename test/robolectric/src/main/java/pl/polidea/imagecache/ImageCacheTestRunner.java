/**
 * 
 */
package pl.polidea.imagecache;

import java.io.File;

import org.junit.runners.model.InitializationError;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * @author Wojciech Piwonski
 * 
 */
public class ImageCacheTestRunner extends RobolectricTestRunner {

    /**
     * @param testClass
     * @throws InitializationError
     */
    public ImageCacheTestRunner(final Class< ? > testClass) throws InitializationError {
        super(testClass, new File("."));
    }

    @Override
    protected void bindShadowClasses() {
        Robolectric.bindShadowClass(MyShadowActivityManager.class);
    }

}
