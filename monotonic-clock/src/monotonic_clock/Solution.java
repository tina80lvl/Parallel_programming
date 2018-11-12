package monotonic_clock;

import org.jetbrains.annotations.NotNull;

/**
 * В теле класса решения разрешено использовать только финальные переменные типа RegularInt.
 * Нельзя volatile, нельзя другие типы, нельзя блокировки, нельзя лазить в глобальные переменные.
 */
public class Solution implements MonotonicClock {
    private final RegularInt c1 = new RegularInt(0);
    private final RegularInt c2 = new RegularInt(0);
    private final RegularInt c3 = new RegularInt(0);

    private final RegularInt d1 = new RegularInt(0);
    private final RegularInt d2 = new RegularInt(0);
    private final RegularInt d3 = new RegularInt(0);

    @Override
    public void write(@NotNull Time time) {
        // write left-to-right
        d1.setValue(time.getD1());
        d2.setValue(time.getD2());
        d3.setValue(time.getD3());

        // write right-to-left
        c3.setValue(time.getD3());
        c2.setValue(time.getD2());
        c1.setValue(time.getD1());
    }

    @NotNull
    @Override
    public Time read() {
        // read left-to-right
        int cc1 = c1.getValue();
        int cc2 = c2.getValue();
        int cc3 = c3.getValue();
        // read right-to-left
        int dd3 = d3.getValue();
        int dd2 = d2.getValue();
        int dd1 = d1.getValue();
        if (cc1 == dd1)
            if (cc2 == dd2)
                if (cc3 == dd3) {
                    return new Time(cc1, cc2, cc3);
                } else {
                    return new Time(cc1, cc2, dd3);
                }
            else {
                return new Time(cc1, dd2, 0);
            }
        else {
            return new Time(dd1, 0, 0);
        }
    }
}