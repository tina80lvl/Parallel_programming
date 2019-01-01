package linked_list_set;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class SetImpl implements Set {

    private class Node {
        AtomicMarkableReference<Node> next;
        int x;

        Node (int x, Node next) {
            this.next = new AtomicMarkableReference<> (next, false);
            this.x = x;
        }
    }

    private class Window {
        Node cur, next, prev;

    }

    private final Node head = new Node(Integer.MIN_VALUE, new Node (Integer.MAX_VALUE, null));

    /**
     * Returns the {@link Window}, where cur.x < x <= next.x
     */
    private Window findWindow(int x) {
        Window w = new Window();

        outer_loop:
        while (true) {
            w.prev = this.head;
            w.cur = w.prev.next.getReference();

            inner_loop:
            while (true) {
                if (w.cur == null) {
                    return w;
                }

                boolean[] removed = new boolean[1];
                w.next = w.cur.next.get(removed);
                if (w.prev.next.isMarked()) {
                    continue outer_loop;
                }

                if (removed[0]) {
                    if (w.prev.next.compareAndSet(w.cur, w.next, false, false)) {
                        continue inner_loop;
                    } else {
                        continue outer_loop;
                    }
                } else {
                    if (w.cur.x == x) {
                        return w;
                    } else if (w.cur.x <= x) {
                        w.prev = w.cur;
                        w.cur = w.next;
                    } else {
                        w.next = w.cur;
                        w.cur = null;
                        return w;
                    }
                }
            }
        }
    }

    @Override
    public boolean add(int x) {
        while (true) {
            Window w = findWindow(x);
            if (w.cur != null) {
                return false;
            }

            Node node = new Node(x, w.next);
            if (w.prev.next.compareAndSet(w.next, node, false, false)) {
                return true;
            }
        }
    }

    @Override
    public boolean remove(int x) {
        while (true) {
            Window w = findWindow(x);
            if (w.cur == null) {
                return false;
            }

            if (w.cur.next.compareAndSet(w.next, w.next, false, true)) {
                w.prev.next.compareAndSet(w.cur, w.next, false, false);
                return true;
            }
        }
    }

    @Override
    public boolean contains(int x) {
        Window w = findWindow(x);
        return w.cur != null;
    }
}