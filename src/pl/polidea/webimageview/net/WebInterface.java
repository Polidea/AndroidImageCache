/**
 *
 */
package pl.polidea.webimageview.net;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Przemysław Jakubczyk <przemyslaw.jakubczyk@polidea.pl>
 */
public interface WebInterface {

    InputStream execute(String path) throws IOException;
}
