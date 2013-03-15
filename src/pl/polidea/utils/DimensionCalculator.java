package pl.polidea.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DimensionCalculator {

    public static int toRoundedPX(Context context, String dimenValueInXML) {
        return DimensionUnit.findByStringValue(dimenValueInXML).stringValueToPX(dimenValueInXML, context.getResources().getDisplayMetrics());
    }

    enum DimensionUnit{
        DP("dp"),
        DIP("dip"),
        PX("px"){
            @Override
            protected int toPX(DisplayMetrics displayMetrics, String valueWithoutSuffix) {
                return (int) Float.parseFloat(valueWithoutSuffix);
            }
        };

        private String stringValue;

        DimensionUnit(String stringValue) {
            this.stringValue = stringValue;
        }

        public static DimensionUnit findByStringValue(String stringValue){
            for (DimensionUnit dimensionUnit : values()) {
                if (stringValue.endsWith(dimensionUnit.stringValue)) {
                    return dimensionUnit;
                }
            }
            throw new IllegalArgumentException("Unknown dimension unit: " + stringValue);
        }

        public int stringValueToPX(String dimenValueInXML, DisplayMetrics displayMetrics){
            final String valueWithoutSuffix = dimenValueInXML.substring(0, dimenValueInXML.length() - stringValue.length());
            return toPX(displayMetrics, valueWithoutSuffix);
        }

        protected int toPX(DisplayMetrics displayMetrics, String valueWithoutSuffix){
            return (int) ((Float.parseFloat(valueWithoutSuffix) * displayMetrics.density) + 0.5);
        }
    }
}