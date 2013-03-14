/**
 *
 */
package pl.polidea.webimageview.net;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.xtremelabs.robolectric.Robolectric;
import java.util.concurrent.ThreadPoolExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.MockUtil;
import pl.polidea.imagecache.ImageCacheTestRunner;

/**
 * @author Marek Multarzynski
 */
@RunWith(ImageCacheTestRunner.class)
public class DefaultWebClientTest {

    @Test
    public void testDefaultHttpClient() {

        // when
        final WebClient client = new WebClient(Robolectric.application);

        // then
        assertFalse(new MockUtil().isMock(client.httpClient));
    }

    @Test
    public void testDefaultExecutor() {

        // when
        final WebClient client = new WebClient(Robolectric.application);

        // then
        assertTrue(client.taskExecutor instanceof ThreadPoolExecutor);
    }

}
