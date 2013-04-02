package pl.polidea.webimageview;

import pl.polidea.utils.Utils;

/**
* @author Mateusz Grzechociński <mateusz.grzechocinski@gmail.com>
*/
public interface WebImageListener {

    WebImageListener NULL = new WebImageListener() {
        @Override
        public void onImageFetchedSuccessfully(String url) {
        	Utils.log("onImageFetchedSuccessfully " + url);
        }

        @Override
        public void onImageFetchedFailed(String url) {
        	Utils.log("onImageFetchedFailed " + url);
        }
    };

    void onImageFetchedSuccessfully(String url);

    void onImageFetchedFailed(String url);
}
