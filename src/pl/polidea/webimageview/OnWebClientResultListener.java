package pl.polidea.webimageview;

import android.graphics.Bitmap;

/**
 * 
 * @author Marek Multrzynski
 * 
 */
public interface OnWebClientResultListener {

    void onWebHit(String path, Bitmap bitmap);

    void onWebMiss(String path);
}
