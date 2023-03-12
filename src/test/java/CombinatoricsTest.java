import center.buran.fast.patterns.misc.Combinatorics;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Тесты класса комбинаторики
 */
public class CombinatoricsTest {

    /**
     * Проверка, что нахождение обратной перестановки работает корректно
     */
    @Test
    public void test1() {
        for (int i = 0; i < 100; i++) {
            int[] p = Combinatorics.getRandomPermutation(
                    Math.abs(ThreadLocalRandom.current().nextInt()) % 1000 + 1
            );
            int[] r = Combinatorics.getReversePermutation(p);
            int[] rr = Combinatorics.getReversePermutation(r);
            assert Arrays.equals(p, rr);
        }
    }

}
