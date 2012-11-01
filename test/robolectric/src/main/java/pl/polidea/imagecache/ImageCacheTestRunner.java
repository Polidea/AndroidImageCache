/**
 * 
 */
package pl.polidea.imagecache;

import java.io.*;

import org.junit.runners.model.*;

import com.xtremelabs.robolectric.*;

/**
 * @author Wojciech Piwonski
 * 
 */
public class ImageCacheTestRunner extends RobolectricTestRunner {

	/**
	 * @param testClass
	 * @throws InitializationError
	 */
	public ImageCacheTestRunner(final Class<?> testClass) throws InitializationError {
		super(testClass, new File("."));
	}

	@Override
	protected void bindShadowClasses() {
		Robolectric.bindShadowClass(MyShadowActivityManager.class);
		Robolectric.bindShadowClass(HighDensityShadowResources.class);

	}

}
