import center.buran.fast.patterns.FastPatternResolver;
import center.buran.fast.patterns.misc.Combinatorics;
import center.buran.fast.patterns.misc.CombinatoricsData;
import center.buran.fast.patterns.misc.PatternBuilder;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Тест быстрого алгоритма поиска изоморфных подграфов
 */
public class FastPatternResolver2Test {

    /**
     * Проверка быстрого алгоритма поиска изоморфных подграфов
     */
    @Test
    public void test1() {
        testPattern(
                20, 12, 13, 5, 7, -100, 100,
                Math.abs(ThreadLocalRandom.current().nextInt()) % 10 + 2, 1.0,
                true
        );
    }

    /**
     * Проверка быстрого алгоритма поиска изоморфных подграфов
     */
    @Test
    public void test3() {
        testPattern(
                20, 12, 13, 5, 7, -100, 100,
                Math.abs(ThreadLocalRandom.current().nextInt()) % 10 + 2, 0.5,
                true
        );
    }

    /**
     * Проверка быстрого алгоритма поиска изоморфных подграфов
     */
    @Test
    public void test5() {
        testPattern(
                20, 12, 13, 5, 7, -10, 10,
                Math.abs(ThreadLocalRandom.current().nextInt()) % 10 + 2, 0.3,
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
    public void testPattern(
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
                    new HashSet<>(FastPatternResolver.getAllPatterns(res, pattern, hardCheck));
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
