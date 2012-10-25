/**
 *
 */
package pl.polidea.webimageview;

import static org.junit.Assert.*;

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

}
