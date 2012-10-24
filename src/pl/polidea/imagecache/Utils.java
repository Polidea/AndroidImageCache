/**
 * 
 */
package pl.polidea.imagecache;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

/**
 * @author Wojciech Piwonski
 * 
 */
public final class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    private Utils() {
    }

    public static String sha1(final String text) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            final byte[] sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (final NoSuchAlgorithmException e) {
            Log.i(TAG, e.getMessage());
        } catch (final UnsupportedEncodingException e) {
            Log.i(TAG, e.getMessage());
        }
        return text;
    }

    private static String convertToHex(final byte[] data) {
        final StringBuffer buf = new StringBuffer();
        for (final byte element : data) {
            int halfbyte = element >>> 4 & 0x0F;
            int two_halfs = 0;
            do {
                if (0 <= halfbyte && halfbyte <= 9) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = element & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

}
