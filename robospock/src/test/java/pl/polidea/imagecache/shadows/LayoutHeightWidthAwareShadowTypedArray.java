package pl.polidea.imagecache.shadows;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.res.TypedArray;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import java.util.HashMap;
import java.util.Map;
import pl.polidea.webimagesampleapp.R;

@Implements(TypedArray.class)
public class LayoutHeightWidthAwareShadowTypedArray {

    public static Map<Integer, Dimen> integerDimenMap = new HashMap<Integer, Dimen>();

    public static int viewID;

    static {
        float density = Robolectric.application.getResources().getDisplayMetrics().density;
        integerDimenMap.put(R.id._match_parents, new Dimen(MATCH_PARENT, MATCH_PARENT));
        integerDimenMap.put(R.id._wrap_contents, new Dimen(WRAP_CONTENT, WRAP_CONTENT));
        integerDimenMap.put(R.id._fixed_width_wrap, new Dimen(WRAP_CONTENT, (int) (density * 40)));
        integerDimenMap.put(R.id._fixed_width_match, new Dimen(MATCH_PARENT, (int) (density * 40)));
        integerDimenMap.put(R.id._fixed_height_wrap, new Dimen((int) (density * 40), WRAP_CONTENT));
        integerDimenMap.put(R.id._fixed_height_match, new Dimen((int) (density * 40), MATCH_PARENT));
        integerDimenMap.put(R.id._fixed_both, new Dimen((int) (density * 25), (int) (density * 40)));
        integerDimenMap.put(R.id._dips_and_pix, new Dimen(20, (int) (density * 40)));
    }

    @Implementation
    public int getLayoutDimension(int index, int defValue) {
        Dimen dimen = integerDimenMap.get(viewID);
        return dimen.getDimenForAttrID(index);
    }

    public static class Dimen {

        private int[] dimen;

        public Dimen(int height, int width) {
            dimen = new int[]{width, height};
        }

        public int getDimenForAttrID(int index) {
            return dimen[index];
        }
    }


}
