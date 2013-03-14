/**
 * 
 */
package pl.polidea.imagecache;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import java.io.File;
import org.junit.runners.model.InitializationError;
import pl.polidea.shadows.HighDensityShadowResources;
import pl.polidea.shadows.MyShadowActivityManager;

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
        Robolectric.bindShadowClass(HighDensityShadowResources.class);
    }

}
