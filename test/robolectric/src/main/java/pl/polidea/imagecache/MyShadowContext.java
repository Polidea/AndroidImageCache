package pl.polidea.imagecache;

import android.content.Context;
import android.content.res.Resources.Theme;

import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowContext;

@Implements(Context.class)
public class MyShadowContext extends ShadowContext {

	@Override
	public Theme getTheme() {
		// TODO Auto-generated method stub
		return null;
	}

}
