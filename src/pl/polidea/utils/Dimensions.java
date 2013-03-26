package pl.polidea.utils;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@gmail.com>
 */
public class Dimensions {

    /**
     * Height in pixels, by may be also {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT}
     * or {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}
     */
    public final int heightPX;

    /**
     * Width in pixels, by may be also {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT}
     * or {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}
     */
    public final int widthPX;

    private Dimensions(int heightPX, int widthPX) {
        this.heightPX = heightPX;
        this.widthPX = widthPX;
    }

    public static Dimensions fromAttributesSet(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return null;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, new int[]{R.attr.layout_width, R.attr.layout_height});
        int height = typedArray.getLayoutDimension(0, 0);
        int width = typedArray.getLayoutDimension(1, 0);

        typedArray.recycle();

        return new Dimensions(height, width);
    }
}
