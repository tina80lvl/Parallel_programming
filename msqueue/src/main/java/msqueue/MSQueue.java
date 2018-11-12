package msqueue;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

public class MSQueue implements Queue {
    private AtomicReference<Node> head = new AtomicReference<>();
    private AtomicReference<Node> tail = new AtomicReference<>();

    public MSQueue() {
        Node dummy = new Node(0);
        this.head.set(dummy);
        this.tail.set(dummy);
    }

    private final Node NIL = new Node(0);

    @Override
    public void enqueue(int x) {
        Node newTail = new Node(x);
        while (true) {
            Node curTail = tail.get();
            if (curTail.next.compareAndSet(null, newTail)) {
                tail.compareAndSet(curTail, newTail);
                return;
            } else {
                tail.compareAndSet(curTail, curTail.next.get());
            }
        }
    }

    @Override
    // taking into account empty queue
    public int dequeue() {
        while (true) {
            Node curHead = head.get();
            Node headNext = curHead.next.get();
            Node curTail = tail.get();
            if (head.compareAndSet(curHead, curHead)) {
                if (headNext == null) {
                    throw new NoSuchElementException();
                } else {
                    if (curHead == curTail) {
                        tail.compareAndSet(curTail, headNext);
                    }
                    if (head.compareAndSet(curHead, headNext)) {
                        return headNext.x;
                    }
                }
            }
        }
    }

    @Override
    public int peek() {
        while (true) {
            Node curHead = head.get();
            Node headNext = curHead.next.get();
            Node curTail = tail.get();
            if (head.compareAndSet(curHead, curHead)) {
                if (headNext == null) {
                    throw new NoSuchElementException();
                } else {
                    if (curHead == curTail) {
                        tail.compareAndSet(curTail, headNext);
                    }
                    if (head.compareAndSet(curHead, curHead)) {
                        return headNext.x;
                    }
                }
            }
        }
    }

    private class Node {
        final int x;
        AtomicReference<Node> next;

        Node(int x) {
            this.x = x;
            this.next = new AtomicReference<>();
        }
    }
}