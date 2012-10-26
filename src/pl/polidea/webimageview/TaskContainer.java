package pl.polidea.webimageview;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TaskContainer {

    Map<String, Set<WebCallback>> map = new HashMap<String, Set<WebCallback>>();

    public void addTask(final String path, final WebCallback listener) {
        if (map.containsKey(path)) {
            final Set<WebCallback> set = map.get(path);
            set.add(listener);
        } else {
            final Set<WebCallback> set = new HashSet<WebCallback>();
            set.add(listener);
            map.put(path, set);
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

}
