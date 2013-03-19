package pl.polidea.webimageview;

/**
* @author Mateusz Grzechociński <mateusz.grzechocinski@gmail.com>
*/
public interface WebImageListener {

    WebImageListener NULL = new WebImageListener() {
        @Override
        public void onImageFetchedSuccessfully(String url) {
        }

        @Override
        public void onImageFetchedFailed(String url) {
        }
    };

    void onImageFetchedSuccessfully(String url);

    void onImageFetchedFailed(String url);
}
