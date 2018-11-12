package stack;


import com.devexperts.dxlab.lincheck.LinChecker;
import com.devexperts.dxlab.lincheck.annotations.HandleExceptionAsResult;
import com.devexperts.dxlab.lincheck.annotations.Operation;
import com.devexperts.dxlab.lincheck.annotations.Param;
import com.devexperts.dxlab.lincheck.annotations.Reset;
import com.devexperts.dxlab.lincheck.paramgen.IntGen;
import com.devexperts.dxlab.lincheck.stress.StressCTest;
import org.junit.Test;

import java.util.EmptyStackException;


@StressCTest
public class LinearizabilityTest {
    private Stack stack;

    @Reset
    public void reset() {
        stack = new StackImpl();
    }

    @Operation
    public void push(@Param(gen = IntGen.class) int x) {
        stack.push(x);
    }

    @HandleExceptionAsResult(EmptyStackException.class)
    @Operation
    public int pop() {
        return stack.pop();
    }

    @Test
    public void test() {
        LinChecker.check(LinearizabilityTest.class);
    }
}