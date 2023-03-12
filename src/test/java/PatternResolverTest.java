import center.buran.fast.patterns.*;
import center.buran.fast.patterns.misc.Combinatorics;
import center.buran.fast.patterns.misc.CombinatoricsData;
import center.buran.fast.patterns.misc.PatternBuilder;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Тест алгоритма поиска изоморфных подграфов перебором
 */
public class PatternResolverTest {
    /**
     * Проверка алгоритма поиска изоморфных подграфов перебором
     */
    @Test
    public void testLoop1() {
        testPatternLoop(
                100, 4, 6, 2, 4, 0, 2,
                Math.abs(ThreadLocalRandom.current().nextInt()) % 10 + 2, 0.5,
                true
        );
    }

    /**
     * Проверка алгоритма поиска изоморфных подграфов перебором
     */
    @Test
    public void testLoop2() {
        testPatternLoop(
                10, 12, 13, 5, 7, -100, 100,
                Math.abs(ThreadLocalRandom.current().nextInt()) % 10 + 2, 0.5,
                false
        );
    }

    /**
     * Проверка алгоритма поиска изоморфных подграфов перебором
     */
    @Test
    public void testLoop3() {
        testPatternLoop(
                100, 4, 6, 2, 4, 0, 2,
                Math.abs(ThreadLocalRandom.current().nextInt()) % 10 + 2, 1.0,
                false
        );
    }

    /**
     * Проверка алгоритма поиска изоморфных подграфов перебором
     */
    @Test
    public void testLoop4() {
        testPatternLoop(
                100, 4, 6, 2, 4, 0, 2,
                Math.abs(ThreadLocalRandom.current().nextInt()) % 10 + 2, 0.5,
                false
        );
    }

    /**
     * Проверка алгоритма поиска изоморфных подграфов перебором
     */
    @Test
    public void restLoop5() {
        testPatternLoop(
                100, 4, 6, 2, 4, 0, 2,
                Math.abs(ThreadLocalRandom.current().nextInt()) % 10 + 2, 0.2,
                false
        );
    }

    /**
     * Проверка быстрого алгоритма поиска изоморфных подграфов
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
    public void testPatternLoop(
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
            // выводим информацию о добавленных паттернах
            System.out.println("ADD:");
            for (CombinatoricsData rp : genCombinatoricsData)
                System.out.println(Arrays.toString(rp.getData()));

            // ищем паттерны с помощью быстрого алгоритма
            int[][] res = pb.getData();
            Set<CombinatoricsData> patterns =
                    new HashSet<>(PatternResolver.getAllPatterns(res, pattern, hardCheck));
            // выводим найденные паттерны
            System.out.println("FOUND:");
            for (CombinatoricsData rp : patterns)
                System.out.println(Arrays.toString(rp.getData()));

            // проверяем, что все комбинации, по которым был добавлен паттерн,
            // были найдены алгоритмом
            genCombinatoricsData.removeAll(patterns);
            if (!genCombinatoricsData.isEmpty()) {
                System.out.println(Arrays.deepToString(source));
                System.out.println(Arrays.deepToString(pattern));
                assert false;
            }
        }
    }
}
