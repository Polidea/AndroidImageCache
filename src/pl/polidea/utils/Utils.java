/**
 *
 */
package pl.polidea.utils;

import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Przemysław Jakubczyk <przemyslaw.jakubczyk@polidea.pl>
 */
public final class Utils {

    private static final String TAG = "ImageCache";

    private static boolean useLogs = false;

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
            int twoHalfs = 0;
            do {
                if (0 <= halfbyte && halfbyte <= 9) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = element & 0x0F;
            } while (twoHalfs++ < 1);
        }
        return buf.toString();
    }

    public static void log(final String msg) {
        if (useLogs) {
            Log.d(TAG, msg);
        }
    }

    public static void log(final Throwable t) {
        if (t != null) {
            printThrowable(t.getMessage(), t);
        }
    }

    public static void log(final String msg, final Throwable t) {
        if (t == null) {
            printThrowable(msg, t);
        } else {
            printThrowable(t.getMessage(), t);
        }
    }

    private static void printThrowable(String msg, Throwable t) {
        if (useLogs) {
            Log.d(TAG, msg, t);
        }
    }

    public static boolean isUseLogs() {
        return useLogs;
    }

    public static void setUseLogs(boolean value) {
        useLogs = value;
    }
}
