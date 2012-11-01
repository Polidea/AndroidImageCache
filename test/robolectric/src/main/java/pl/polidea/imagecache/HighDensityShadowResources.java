package pl.polidea.imagecache;

import android.content.res.*;
import android.util.*;
import android.view.*;

import com.xtremelabs.robolectric.*;
import com.xtremelabs.robolectric.internal.*;
import com.xtremelabs.robolectric.shadows.*;

@Implements(Resources.class)
public class HighDensityShadowResources extends ShadowResources {
	DisplayMetrics displayMetrics;
	Display display;

	@Override
	public DisplayMetrics getDisplayMetrics() {
		if (displayMetrics == null) {
			if (display == null) {
				display = Robolectric.newInstanceOf(Display.class);
			}

			displayMetrics = new DisplayMetrics();
			display.getMetrics(displayMetrics);
		}
		displayMetrics.density = 1.5f;
		return displayMetrics;
	}
}
