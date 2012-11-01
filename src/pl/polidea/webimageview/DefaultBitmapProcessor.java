package pl.polidea.webimageview;

import java.io.File;

import pl.polidea.webimageview.WebImageView.BitmapProcessor;
import android.graphics.Bitmap;
import android.view.ViewGroup.LayoutParams;

public class DefaultBitmapProcessor implements BitmapProcessor {

	private final static int MATCH = LayoutParams.MATCH_PARENT;
	private final static int WRAP = LayoutParams.WRAP_CONTENT;

	WebImageView webImageView;

	public DefaultBitmapProcessor(final WebImageView webImageView) {
		this.webImageView = webImageView;
	}

	@Override
	public Bitmap process(final File pathToBitmap) {
		return new Bitmaps().generateBitmap(pathToBitmap.getAbsolutePath());
	}

	Type determineType() {
		final int width;
		final int height;
		return null;
	}

	enum Type {
		ORIGNAL, FIX_WIDTH, FIX_HEIGHT, FIX_BOTH
	}
}
