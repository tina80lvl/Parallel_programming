package counter;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterImpl implements Counter {
    private static int AVIL_SLOTS = 2 * Runtime.getRuntime().availableProcessors();
    private Random random = ThreadLocalRandom.current();
    private AtomicInteger[] slots = new AtomicInteger[AVIL_SLOTS];

    CounterImpl() {
        for (int i = 0; i < AVIL_SLOTS; i++) {
            slots[i] = new AtomicInteger();
        }
    }

    @Override
    public void inc() {
        slots[random.nextInt(AVIL_SLOTS)].getAndIncrement();
    }

    @Override
    public int get() {
        int res = 0;
        for (AtomicInteger slot : slots) {
            res += slot.get();
        }
        return res;
    }
}