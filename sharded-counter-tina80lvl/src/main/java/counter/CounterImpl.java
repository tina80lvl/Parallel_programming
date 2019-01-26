package counter;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class CounterImpl implements Counter {
    private static int AVIL_SLOTS = 2 * Runtime.getRuntime().availableProcessors();
    private int PADDING = 16;
    private Random random = ThreadLocalRandom.current();
    private AtomicIntegerArray slots = new AtomicIntegerArray(AVIL_SLOTS * PADDING);

    @Override
    public void inc() {
        slots.getAndAdd(random.nextInt(AVIL_SLOTS) * PADDING, 1);
    }

    @Override
    public int get() {
        int res = 0;
        for (int i = 0; i < AVIL_SLOTS; ++i) {
            res += slots.get(i * PADDING);
        }
        return res;
    }
}