package pl.polidea.utils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@gmail.com>
 */
public class Dimensions {

    private static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    private static final String PARAM_LAYOUT_HEIGHT = "layout_height";

    private static final String PARAM_LAYOUT_WIDTH = "layout_width";

    public final int height;

    public final int width;

    private Context context;

    private Dimensions(Context context, String heightAsString, String widthAsString) {
        this.context = context;
        this.height = asInt(heightAsString);
        this.width = asInt(widthAsString);
    }

    public static Dimensions fromAttributesSet(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return null;
        }
        String heightAsString = attributeSet.getAttributeValue(ANDROID_SCHEMA, PARAM_LAYOUT_HEIGHT);
        String widthAsString = attributeSet.getAttributeValue(ANDROID_SCHEMA, PARAM_LAYOUT_WIDTH);
        return new Dimensions(context, heightAsString, widthAsString);
    }

    private int asInt(final String value) {
        try {
            if ("match_parent".equals(value) || "fill_parent".equals(value)) {
                return MATCH_PARENT;
            } else if ("wrap_content".equals(value)) {
                return WRAP_CONTENT;
            } else {
                return DimensionCalculator.toRoundedPX(context, value);
            }
        } catch (final NumberFormatException e) {
            return MATCH_PARENT;
        }
    }
}
