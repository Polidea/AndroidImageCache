package shadows;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowBitmapFactory;

@Implements(BitmapFactory.class)
public class MyShadowBitmapFactory extends ShadowBitmapFactory {

    public static boolean shouldThrowException = false;

    @Implementation
    public static Bitmap decodeFile(String pathName, BitmapFactory.Options options) {
        if (shouldThrowException) {
            throw new OutOfMemoryError();
        } else {
            return ShadowBitmapFactory.decodeFile(pathName, options);
        }
    }
}
