package pl.polidea.shadows;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;

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
