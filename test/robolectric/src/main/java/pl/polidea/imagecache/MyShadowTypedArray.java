package pl.polidea.imagecache;

import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.internal.RealObject;
import com.xtremelabs.robolectric.shadows.ShadowTypedArray;

@Implements(TypedArray.class)
public class MyShadowTypedArray extends ShadowTypedArray {

	@RealObject
	TypedArray ta;
	private static AttributeSet set;
	private static int[] attrs;

	@Implementation
	public int getDimensionPixelSize(final int index, final int defValue) {
		return 2;
	}

	public static void setAttrs(final AttributeSet set, final int[] attrs) {
		MyShadowTypedArray.set = set;
		MyShadowTypedArray.attrs = attrs;

	}
}
