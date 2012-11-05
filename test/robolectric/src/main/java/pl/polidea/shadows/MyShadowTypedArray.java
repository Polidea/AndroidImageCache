package pl.polidea.shadows;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowTypedArray;

@Implements(TypedArray.class)
public class MyShadowTypedArray extends ShadowTypedArray {
    private final static int MATCH = LayoutParams.MATCH_PARENT;
    private final static int WRAP = LayoutParams.WRAP_CONTENT;

    private static AttributeSet set;
    private static int[] attrs;

    @Implementation
    public int getDimensionPixelSize(final int index, final int defValue) {
        String name = "";
        final int attr = attrs[index];
        switch (attr) {
        case android.R.attr.layout_width:
            name = "layout_width";
            break;
        case android.R.attr.layout_height:
            name = "layout_height";
            break;

        }
        return guessValue(set.getAttributeValue("", name));
    }

    public static void setAttrs(final AttributeSet set, final int[] attrs) {
        MyShadowTypedArray.set = set;
        MyShadowTypedArray.attrs = attrs;

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
        final DisplayMetrics displayMetrics = Robolectric.application.getResources().getDisplayMetrics();
        if (value.endsWith("dp")) {
            final String number = value.substring(0, value.length() - 2);
            return (int) ((Integer.parseInt(number) * displayMetrics.density) + 0.5);
        } else if (value.endsWith("dip")) {
            final String number = value.substring(0, value.length() - 3);
            return (int) ((Integer.parseInt(number) * displayMetrics.density) + 0.5);
        } else if (value.endsWith("px")) {
            final String number = value.substring(0, value.length() - 2);
            return Integer.parseInt(number);
        }

        return 0;
    }
}
