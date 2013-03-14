package shadows;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowResources;

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
