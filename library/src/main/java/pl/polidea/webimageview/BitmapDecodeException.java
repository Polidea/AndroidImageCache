package pl.polidea.webimageview;

/**
 * @author Przemysław Jakubczyk <przemyslaw.jakubczyk@pl.polidea.pl>
 */
public class BitmapDecodeException extends Exception {

    public BitmapDecodeException(OutOfMemoryError e) {
        super(e);
    }
}
