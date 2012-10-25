/**
 *
 */
package pl.polidea.webimageview;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

/**
 * @author Marek Multarzynski
 * 
 */
public interface WebInterface {

    HttpResponse execute(String path) throws ClientProtocolException, IOException;
}
