import center.buran.fast.patterns.FastPatternResolver;
import center.buran.fast.patterns.misc.PatternBuilder;
import center.buran.fast.patterns.PatternResolver;
import center.buran.fast.patterns.misc.Combinatorics;
import center.buran.fast.patterns.misc.CombinatoricsData;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Тест быстрого алгоритма поиска изоморфных подграфов
 */
public class FastPatternResolverTest {

    /**
     * Сверка результатов поиска изоморфных подграфов с помощью быстрого алгоритма и с помощью полного перебора
     */
    @Test
    public void compareTest1() {
        compare(
                20, 12, 13, 5, 7, -100, 100,
                Math.abs(ThreadLocalRandom.current().nextInt()) % 10 + 2, 0.5,
                true
        );
    }

    /**
     * Сверка результатов поиска изоморфных подграфов с помощью быстрого алгоритма и с помощью полного перебора
     */
    @Test
    public void compareTest2() {
        compare(
                20, 12, 13, 5, 7, -100, 100,
                Math.abs(ThreadLocalRandom.current().nextInt()) % 10 + 2, 0.5,
                false
        );
    }

    /**
     * Сверка результатов поиска изоморфных подграфов с помощью быстрого алгоритма и с помощью полного перебора
     */
    @Test
    public void compareTest3() {
        compare(
                20, 12, 13, 5, 7, -100, 100,
                Math.abs(ThreadLocalRandom.current().nextInt()) % 10 + 2, 1.0,
                true
        );
    }

    /**
     * Сверка результатов поиска изоморфных подграфов с помощью быстрого алгоритма и с помощью полного перебора
     */
    @Test
    public void compareTest4() {
        compare(
                20, 12, 13, 5, 7, -100, 100,
                Math.abs(ThreadLocalRandom.current().nextInt()) % 10 + 2, 1.0,
                true
        );
    }

    /**
     * Сверка результатов поиска изоморфных подграфов с помощью быстрого алгоритма и с помощью полного перебора
     */
    @Test
    public void compareTest5() {
        compare(
                20, 12, 13, 5, 7, -100, 100,
                Math.abs(ThreadLocalRandom.current().nextInt()) % 10 + 2, 1.0,
                false
        );
    }

    /**
     * Сверка результатов поиска изоморфных подграфов с помощью быстрого алгоритма и с помощью полного перебора
     */
    @Test
    public void compareTest6() {
        compare(
                20, 12, 13, 5, 7, -100, 100,
                Math.abs(ThreadLocalRandom.current().nextInt()) % 10 + 2, 1.0,
                false
        );
    }

    /**
     * Сверка результатов поиска изоморфных подграфов с помощью быстрого алгоритма и с помощью полного перебора
     *
     * @param testCnt     кол-во тестов
     * @param minS        минимальный размер матрицы-источника
     * @param maxS        максимальный размер матрицы-источника
     * @param minP        минимальный размер матрицы-паттерна
     * @param maxP        максимальный размер матрицы-паттерна
     * @param minE        минимальное значение элементов матриц
     * @param maxE        максимальный значение элементов матриц
     * @param pCnt        кол-во добавленных паттернов
     * @param nonZeroPart доля ненулевых элементов
     * @param hardCheck   флаг, нужна ли жёсткая сверка
     */
    public void compare(
            int testCnt, int minS, int maxS, int minP, int maxP, int minE, int maxE, int pCnt, double nonZeroPart,
            boolean hardCheck
    ) {
        // повторяем testCnt раз
        for (int i = 0; i < testCnt; i++) {
            // формируем случайную матрицу-источник
            int[][] source = Combinatorics.randomMatrix(minS, maxS, minE, maxE, nonZeroPart);
            // формируем случайную матрицу-паттерн
            int[][] pattern = Combinatorics.randomMatrix(minP, maxP, minE, maxE, nonZeroPart);
            // создаём объект построителя паттернов
            PatternBuilder pb = new PatternBuilder(source);
            // создаём список комбинаций, согласно которым был добавлен паттерн
            List<CombinatoricsData> genCombinatoricsData = new ArrayList<>();
            // повторяем, пока количество добавленных паттернов меньше pCnt
            for (int j = 0; j < pCnt; ) {
                // получить случайную комбинацию
                CombinatoricsData rComb = Combinatorics.getRandomCombination(source.length, pattern.length);
                // если получилось добавить в дата-граф паттерн по полученной случайной комбинации
                if (pb.putPattern(pattern, rComb)) {
                    // увеличиваем количество добавленных паттернов
                    j++;
                    // добавляем комбинацию в список
                    genCombinatoricsData.add(rComb);
                }
            }

            // получаем итоговую матрицу из построителя паттернов
            int[][] res = pb.getData();
            // сохраняем список найденных паттернов быстрым способом в хэш-множество
            Set<CombinatoricsData> fastPatterns =
                    new HashSet<>(FastPatternResolver.getAllPatterns(res, pattern, hardCheck));

            // сохраняем список найденных паттернов перебором в хэш-множество
            Set<CombinatoricsData> patterns =
                    new HashSet<>(PatternResolver.getAllPatterns(res, pattern, hardCheck));

            // если множества найденных паттернов не совпадают
            if (!fastPatterns.equals(patterns)) {
                // выводим информацию о несработавшем тесте
                System.out.println(Arrays.deepToString(source));
                System.out.println(Arrays.deepToString(pattern));
                System.out.println("ADD:");
                for (CombinatoricsData rp : genCombinatoricsData)
                    System.out.println(Arrays.toString(rp.getData()));
                System.out.println("FAST FOUND:");
                for (CombinatoricsData rp : fastPatterns)
                    System.out.println(Arrays.toString(rp.getData()));
                System.out.println("CLASSIC FOUND:");
                for (CombinatoricsData rp : patterns)
                    System.out.println(Arrays.toString(rp.getData()));

                Set<CombinatoricsData> pSet = new HashSet<>(patterns);
                pSet.removeAll(fastPatterns);
                System.out.println("PATTERNS RETAINED:");
                for (CombinatoricsData cd : pSet)
                    System.out.println(Arrays.toString(cd.getData()));
                Set<CombinatoricsData> fpSet = new HashSet<>(fastPatterns);
                fpSet.removeAll(patterns);
                System.out.println("FAST PATTERNS RETAINED:");
                for (CombinatoricsData cd : fpSet)
                    System.out.println(Arrays.toString(cd.getData()));
                assert false;
            }
        }
    }

}
