package pl.polidea.webimageview.net;

import java.io.File;
import pl.polidea.utils.TempFile;

/**
 * @author Marek Multrzynski
 */
public interface WebCallback {

    void onWebHit(String path, File file);

    void onWebMiss(String path);
}
