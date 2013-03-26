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
        TypedArray heightTypedArray = context.obtainStyledAttributes(attributeSet, new int[]{R.attr.layout_height});
        int height = heightTypedArray.getLayoutDimension(0, 0);

        TypedArray widthTypedArray = context.obtainStyledAttributes(attributeSet, new int[]{R.attr.layout_width});
        int width = widthTypedArray.getLayoutDimension(0, 0);

        heightTypedArray.recycle();
        widthTypedArray.recycle();

        return new Dimensions(height, width);
    }
}
