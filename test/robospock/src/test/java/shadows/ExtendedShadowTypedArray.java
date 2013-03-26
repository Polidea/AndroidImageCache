package shadows;

import static android.view.ViewGroup.LayoutParams.*;

import android.content.res.TypedArray;
import android.view.ViewGroup;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowTypedArray;
import java.util.HashMap;
import java.util.Map;
import pl.polidea.imagecache.R;

@Implements(TypedArray.class)
public class ExtendedShadowTypedArray {

    public static class Dimen {

        private int[] dimen;

        public Dimen(int height, int width) {
            dimen = new int[]{height, width};
        }

        public int getDimenForAttrID(int index) {
            return dimen[index];
        }
    }

    public static Map<Integer, Dimen> integerDimenMap = new HashMap<Integer, Dimen>();

    public static int viewID;

    static {
        integerDimenMap.put(R.id._match_parents, new Dimen(MATCH_PARENT, MATCH_PARENT));
        integerDimenMap.put(R.id._wrap_contents, new Dimen(WRAP_CONTENT, WRAP_CONTENT));
        integerDimenMap.put(R.id._fixed_width_wrap, new Dimen(WRAP_CONTENT, 60));
        integerDimenMap.put(R.id._fixed_width_match, new Dimen(MATCH_PARENT, 60));
        integerDimenMap.put(R.id._fixed_height_wrap, new Dimen(60, WRAP_CONTENT));
        integerDimenMap.put(R.id._fixed_height_match, new Dimen(60, MATCH_PARENT));
        integerDimenMap.put(R.id._fixed_both, new Dimen(38, 47));
        integerDimenMap.put(R.id._dips_and_pix, new Dimen(25, 25));
    }

    @Implementation
    public int getLayoutDimension(int index, int defValue) {
        Dimen dimen = integerDimenMap.get(viewID);
        return dimen.getDimenForAttrID(index);
    }


}
