package pl.polidea.webimageview.net;

import android.text.TextUtils;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import pl.polidea.utils.TempFile;

/**
 * The Class TaskContainer.
 */
public class TaskContainer {

    Map<String, Set<WebCallback>> map = new HashMap();

    /**
     * Adds the task.
     *
     * @param path     the path
     * @param listener the listener
     * @return true, if new key added
     */
    public synchronized boolean addTask(final String path, final WebCallback listener) {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("path can't be empty or null");
        }

        if (map.containsKey(path)) {
            final Set<WebCallback> set = map.get(path);
            set.add(listener);
            return false;
        } else {
            final Set<WebCallback> set = new HashSet();
            set.add(listener);
            map.put(path, set);
            return true;
        }
    }

    public synchronized int size() {
        return map.size();
    }

    public synchronized void remove(final String path) {
        map.remove(path);
    }

    public synchronized int callbackSize() {
        int size = 0;
        for (final Entry<String, Set<WebCallback>> task : map.entrySet()) {
            size += task.getValue().size();
        }
        return size;
    }

    /**
     * Perform callbacks on all registered classes under path key.
     *
     * @param path
     * @param file
     */
    public synchronized void performCallbacks(final String path, final File file) {
        final Set<WebCallback> set = map.get(path);
        if (set == null) return;
        for (final WebCallback webCallback : set) {
            webCallback.onWebHit(path, file);
        }
    }

    /**
     * Perform miss callbacks on all registered classes under path key.
     *
     * @param path the path
     */
    public synchronized void performMissCallbacks(final String path) {
        final Set<WebCallback> set = map.get(path);
        if( set == null ) return;
        for (final WebCallback webCallback : set) {
            webCallback.onWebMiss(path);
        }
    }

}
