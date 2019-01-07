package faaqueue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static faaqueue.FAAQueue.Node.NODE_SIZE;


public class FAAQueue<T> implements Queue<T> {
    private static final Object DONE = new Object(); // Marker for the "DONE" slot state; to avoid memory leaks

    private AtomicReference<Node> head; // Head pointer, similarly to the Michael-Scott queue (but the first node is _not_ sentinel)
    private AtomicReference<Node> tail; // Tail pointer, similarly to the Michael-Scott queue

    public FAAQueue() {
        Node nd = new Node();
        head = new AtomicReference<>(nd);
        tail = new AtomicReference<>(nd);
    }

    @Override
    public void enqueue(T x) {
        while (true) {
            Node curTail = this.tail.get();
            Node tailNext = curTail.next.get();
            if (tailNext != null) {
                this.tail.compareAndSet(curTail, tailNext);
                continue;
            }

            int enqIdx = curTail.enqIdx.getAndIncrement();
            if (enqIdx >= NODE_SIZE) {
                Node newTail = new Node(x);

                curTail = tail.get();
                Node nextPtr = curTail.next.get();
                if (curTail == tail.get()) {
                    if (nextPtr == null) {
                        if (tail.get().next.compareAndSet(null, newTail )) {
                            break;
                        }
                    } else {
                        tail.compareAndSet(curTail, nextPtr);
                    }
                }
            } else {
                if (curTail.data.compareAndSet(enqIdx, null, x)) {
                    return;
                }
            }
        }
    }

    @Override
    public T dequeue() {
        while (true) {
            Node curHead = this.head.get();
            if (curHead.isEmpty()) {
                Node headNext = curHead.next.get();
                if (headNext == null) {
                    return null;
                }
                this.head.compareAndSet(curHead, headNext);
            } else {
                int deqIdx = curHead.deqIdx.getAndIncrement();
                if (deqIdx >= NODE_SIZE) {
                    continue;
                }
                Object res = curHead.data.getAndSet(deqIdx, DONE);
                if (res == null) {
                    continue;
                } else {
                    return (T) res;
                }
            }
        }

    }

    static class Node {
        static final int NODE_SIZE = 2; // CHANGE ME FOR BENCHMARKING ONLY

        private AtomicReference<Node> next = new AtomicReference<>();
        private AtomicInteger enqIdx = new AtomicInteger();
        private AtomicInteger deqIdx = new AtomicInteger();
        private final AtomicReferenceArray<Object> data = new AtomicReferenceArray<>(new Object[NODE_SIZE]);

        Node() {}

        Node(Object x) {
            this.enqIdx.incrementAndGet();
            this.data.set(0, x);
        }

        private boolean isEmpty() {
            return this.deqIdx.get() >= this.enqIdx.get();
        }
    }
}