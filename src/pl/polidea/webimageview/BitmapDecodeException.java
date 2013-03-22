package pl.polidea.webimageview;

/**
 * @author Przemysław Jakubczyk <przemyslaw.jakubczyk@polidea.pl>
 */
public class BitmapDecodeException extends Exception {

    public BitmapDecodeException(OutOfMemoryError e) {
        super(e);
    }
}
