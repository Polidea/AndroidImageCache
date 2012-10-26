package pl.polidea.webimageview;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.graphics.Bitmap;

public class TaskContainer {

    Map<String, Set<WebCallback>> map = new HashMap<String, Set<WebCallback>>();

    /**
     * Adds the task.
     * 
     * @param path
     *            the path
     * @param listener
     *            the listener
     * @return true, if new key added
     */
    public boolean addTask(final String path, final WebCallback listener) {
        if (map.containsKey(path)) {
            final Set<WebCallback> set = map.get(path);
            set.add(listener);
            return false;
        } else {
            final Set<WebCallback> set = new HashSet<WebCallback>();
            set.add(listener);
            map.put(path, set);
            return true;
        }
    }

    public int size() {
        return map.size();
    }

    public void remove(final String path) {
        map.remove(path);
    }

    public int callbackSize() {
        int size = 0;
        for (final Entry<String, Set<WebCallback>> task : map.entrySet()) {
            size += task.getValue().size();
        }
        return size;
    }

    public void performCallbacks(final String path, final Bitmap bitmap) {
        final Set<WebCallback> set = map.get(path);
        for (final WebCallback webCallback : set) {
            webCallback.onWebHit(path, bitmap);
        }
    }

    public void performMissCallbacks(final String path) {
        final Set<WebCallback> set = map.get(path);
        for (final WebCallback webCallback : set) {
            webCallback.onWebMiss(path);
        }
    }

}
