/**
 *
 */
package pl.polidea.webimageview;

import static org.junit.Assert.*;

import java.util.concurrent.ThreadPoolExecutor;

import org.junit.Test;
import org.mockito.internal.util.MockUtil;

/**
 * @author Marek Multarzynski
 * 
 */
public class DefaultWebClientTest {

    @Test
    public void testDefaultHttpClient() {

        // when
        final WebClient client = new WebClient();

        // then
        assertFalse(new MockUtil().isMock(client.httpClient));
    }

    @Test
    public void testDefaultExecutor() {

        // when
        final WebClient client = new WebClient();

        // then
        assertTrue(client.taskExecutor instanceof ThreadPoolExecutor);
    }

}
