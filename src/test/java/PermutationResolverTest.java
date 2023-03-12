import center.buran.fast.patterns.misc.Combinatorics;
import center.buran.fast.patterns.misc.CombinatoricsData;
import center.buran.fast.patterns.PatternResolver;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс тестов перестановок
 */
public class PermutationResolverTest {
    /**
     * Ручной тест
     */
    @Test
    public void test1() {
        int[][] source = new int[][]{
                {0, 1, 1, 0, 0, 0, 0},
                {1, 0, 0, 1, 1, 0, 0},
                {1, 0, 0, 0, 1, 0, 1},
                {0, 1, 0, 0, 1, 1, 1},
                {0, 1, 1, 1, 0, 0, 1},
                {0, 0, 0, 1, 0, 0, 1},
                {0, 0, 1, 1, 1, 1, 0}
        };


        int[][] target = new int[][]{
                {0, 1, 1, 0, 0, 0, 0}, // A
                {1, 0, 0, 1, 1, 0, 0}, // Б
                {1, 0, 0, 1, 0, 1, 0}, // В
                {0, 1, 1, 0, 1, 1, 0}, // Г
                {0, 1, 0, 1, 0, 1, 1}, // Д
                {0, 0, 1, 1, 1, 0, 1}, // Е
                {0, 0, 0, 0, 1, 1, 0}  // Ж
        };

        Set<CombinatoricsData> ps = PatternResolver.getAllIsomorphicPermutations(source, target, false);

        Set<CombinatoricsData> ops = new HashSet<>();
        ops.add(new CombinatoricsData(new int[]{0, 2, 1, 4, 6, 3, 5}));
        ops.add(new CombinatoricsData(new int[]{0, 1, 2, 4, 3, 6, 5}));

        assert ps.containsAll(ops);

    }

    /**
     * Тест для проверки, что если переставить матрицу по той или иной перестановке,
     * то при поиске перестановок она будет найдена
     */
    @Test
    public void test2() {
        for (int i = 0; i < 100; i++) {
            int[][] m = Combinatorics.randomMatrix(3, 10, -100, 100, 1.0);

            int[] permutation = Combinatorics.getRandomPermutation(m.length);
            int[][] pm = Combinatorics.makePermute(m, permutation);

            Set<CombinatoricsData> cds = PatternResolver.getAllIsomorphicPermutations(m, pm, true);

            assert cds.contains(new CombinatoricsData(permutation));

            for (CombinatoricsData cd : cds) {
                int[][] lmp = Combinatorics.makePermute(m, cd.getData());
                assert Combinatorics.areEqual(lmp, pm);
            }
        }
    }

}
