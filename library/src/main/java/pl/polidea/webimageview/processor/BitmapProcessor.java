package pl.polidea.webimageview.processor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import pl.polidea.webimageview.BitmapDecodeException;

/**
* @author Mateusz Grzechociński <mateusz.grzechocinski@gmail.com>
*/
public interface BitmapProcessor {

    BitmapProcessor DEFAULT = new BitmapProcessor() {
        @Override
        public Bitmap process(File pathToBitmap) throws BitmapDecodeException {
            try {
                return BitmapFactory.decodeFile(pathToBitmap.getPath());
            } catch (final OutOfMemoryError e) {
                 return null;
            }
        }
    };

    Bitmap process(File pathToBitmap) throws BitmapDecodeException;
}
