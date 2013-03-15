package pl.polidea.webimageview.net;

import java.io.File;

/**
 * @author Marek Multrzynski
 */
public interface WebCallback {

    void onWebHit(String path, File file);

    void onWebMiss(String path);
}
