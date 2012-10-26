/**
 *
 */
package pl.polidea.webimageview;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Marek Multarzynski
 * 
 */
public interface WebInterface {

    InputStream execute(String path) throws IOException;
}
