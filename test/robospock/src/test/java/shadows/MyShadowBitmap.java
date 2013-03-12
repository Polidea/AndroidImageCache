package shadows;

import android.graphics.Bitmap;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowBitmap;


@Implements(Bitmap.class)
public class MyShadowBitmap extends ShadowBitmap {

    public static boolean shouldThrowException = false;

    @Implementation
    public static Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter) {
        if (shouldThrowException) {
            throw new OutOfMemoryError();
        } else {
            return ShadowBitmap.createScaledBitmap(src, dstWidth, dstHeight, filter);
        }
    }
}
