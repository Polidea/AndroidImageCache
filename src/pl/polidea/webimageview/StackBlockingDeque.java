package pl.polidea.webimageview;

import pl.polidea.imagecache.thridparty.LinkedBlockingDeque;

public class StackBlockingDeque extends LinkedBlockingDeque<Runnable> {

    /**
     * 
     */
    private static final long serialVersionUID = 1635542918696298694L;

    @Override
    public boolean offer(final Runnable t) {
        return add(t);
    }

    @Override
    public Runnable remove() {
        return super.removeFirst();
    }

    @Override
    public boolean add(final Runnable elem) {
        if (contains(elem)) {
            remove(elem);
            return offerFirst(elem);
        }
        return super.add(elem);
    }
}
