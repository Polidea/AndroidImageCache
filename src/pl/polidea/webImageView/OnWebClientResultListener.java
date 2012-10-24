package pl.polidea.webImageView;

import java.net.URL;

import android.graphics.Bitmap;

/**
 * 
 * @author Marek Multrzynski
 * 
 */
public interface OnWebClientResultListener {

    void onWebHit(URL url, Bitmap bitmap);

    void onWebMiss(URL url);
}
