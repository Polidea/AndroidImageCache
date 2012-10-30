package pl.polidea.webimageview;

import java.io.File;

import pl.polidea.webimageview.WebImageView.BitmapProcessor;
import android.graphics.Bitmap;

public class DefaultBitmapProcessor implements BitmapProcessor {

    WebImageView webImageView;

    public DefaultBitmapProcessor(final WebImageView webImageView) {
        this.webImageView = webImageView;
    }

    @Override
    public Bitmap process(final File pathToBitmap) {
        return new Bitmaps().generateBitmap(pathToBitmap.getAbsolutePath());
    }

}
