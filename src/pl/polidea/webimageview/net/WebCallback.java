package pl.polidea.webimageview.net;

import java.io.File;

/**
 * @author Przemysław Jakubczyk <przemyslaw.jakubczyk@polidea.pl>
 */
public interface WebCallback {

    void onWebHit(String path, File file);

    void onWebMiss(String path);
}
