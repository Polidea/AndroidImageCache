/**
 * 
 */
package shadows;

import android.app.ActivityManager;
import com.xtremelabs.robolectric.internal.Implements;

/**
 * @author Wojciech Piwonski
 * 
 */
@Implements(ActivityManager.class)
public class MyShadowActivityManager {

    public int getMemoryClass() {
        return 64;
    }
}
