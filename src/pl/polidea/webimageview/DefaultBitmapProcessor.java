package pl.polidea.webimageview;

import java.io.*;

import pl.polidea.webimageview.WebImageView.BitmapProcessor;
import android.graphics.*;
import android.util.*;
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
		final Bitmaps bitmaps = new Bitmaps();
		final Processor processor = determineProcessor();
		Bitmap bitmap = null;
		final String path = pathToBitmap.getPath();
		switch (processor.type) {
		case FIX_BOTH:
			bitmap = bitmaps.generateBitmap(path, processor.width, processor.height);
			break;
		case FIX_HEIGHT:
			bitmap = bitmaps.generateScaledHeightBitmap(path, processor.height);
			break;
		case FIX_WIDTH:
			bitmap = bitmaps.generateScaledWidthBitmap(path, processor.width);
			break;
		case ORIGNAL:
			bitmap = bitmaps.generateBitmap(path);
			break;
		}
		return bitmap;
	}

	Processor determineProcessor() {
		if (webImageView.attrs == null) {
			return new Processor(ProcessorType.ORIGNAL);
		}
		final String layout_width = webImageView.attrs.getAttributeValue("", "layout_width");
		final String layout_height = webImageView.attrs.getAttributeValue("", "layout_height");
		final int width = guessValue(layout_width);
		final int height = guessValue(layout_height);

		if (height + width < 0) {
			return new Processor(ProcessorType.ORIGNAL);
		}

		if (width > 0 && height > 0) {
			return new Processor(ProcessorType.FIX_BOTH, width, height);
		} else if (width > 0 && (height == WRAP || height == MATCH)) {
			return new Processor(ProcessorType.FIX_WIDTH, width, height);
		} else if (height > 0 && (width == WRAP || width == MATCH)) {
			return new Processor(ProcessorType.FIX_HEIGHT, width, height);
		}

		return new Processor(ProcessorType.ORIGNAL);
	}

	int guessValue(final String layout_width) {
		try {
			if ("match_parent".equals(layout_width) || "fill_parent".equals(layout_width)) {
				return MATCH;
			} else if ("wrap_content".equals(layout_width)) {
				return WRAP;
			} else {
				return calculateValue(layout_width);
			}
		} catch (final NumberFormatException e) {
			return MATCH;
		}
	}

	int calculateValue(final String value) {
		final DisplayMetrics displayMetrics = webImageView.getContext().getResources().getDisplayMetrics();
		if (value.contains("dp")) {
			final String number = value.substring(0, value.length() - 2);
			return (int) ((Integer.parseInt(number) * displayMetrics.density) + 0.5);
		} else if (value.contains("dip")) {
			final String number = value.substring(0, value.length() - 3);
			return (int) ((Integer.parseInt(number) * displayMetrics.density) + 0.5);
		} else if (value.contains("px")) {
			final String number = value.substring(0, value.length() - 2);
			return Integer.parseInt(number);
		}

		return 0;
	}

	public enum ProcessorType {
		ORIGNAL, FIX_WIDTH, FIX_HEIGHT, FIX_BOTH
	}

	public static class Processor {
		ProcessorType type;
		int width;
		int height;

		public Processor(final ProcessorType type) {
			this.type = type;
		}

		public Processor(final ProcessorType type, final int width, final int height) {
			this.type = type;
			this.width = width;
			this.height = height;
		}

	}

}
