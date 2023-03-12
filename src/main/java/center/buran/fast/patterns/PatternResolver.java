package center.buran.fast.patterns;

import center.buran.fast.patterns.misc.Combinatorics;
import center.buran.fast.patterns.misc.CombinatoricsData;

import java.util.*;
import java.util.function.Consumer;

/**
 * Класс поиска изоморфных подграфов полным перебором
 */
public class PatternResolver {

    /**
     * Поиск изоморфных подграфов полным перебором
     *
     * @param source    матрица-источник
     * @param pattern   искомый паттерн
     * @param hardCheck флаг, нужна ли жёсткая проверка; если `hardCheck` равен `false`, то нулевому элементу паттерна
     *                  может соответствовать произвольное значение в переставленной подматрице, а если
     *                  `true`, то все элементы паттерна и переставленной подматрицы должны
     *                  совпадать с точностью до перестановки.
     * @return список таких комбинаций точек из источника, чтобы при составлении
     * соответствующих переставленных подматриц, подграфы, построенные по ним,
     * были изоморфны заданному паттерну
     */
    public static List<CombinatoricsData> getAllPatterns(int[][] source, int[][] pattern, boolean hardCheck) {
        // множество найденных паттернов
        List<CombinatoricsData> res = new ArrayList<>();
        // перебираем все возрастающие комбинации
        Combinatorics.combine(source.length, pattern.length, c -> {
            // для каждой из них получаем матрицу из дата-графа по этой комбинации
            int[][] subMatrix = Combinatorics.getSubMatrix(source, c);
            // находим все перестановки, которые связывают паттерн
            // и составленную подматрицу
            Set<CombinatoricsData> ps = getAllIsomorphicPermutations(subMatrix, pattern, hardCheck);
            // для каждой найденной перестановки
            for (CombinatoricsData p : ps) {
                // в множество добавляем переставленную комбинацию
                // в соответствии с той, которая найдена при поиске изоморфных
                // матриц
                res.add(Combinatorics.makePermute(c, Combinatorics.getReversePermutation(p.getData())));
            }
        });

        // возвращаем множество найденных паттернов
        return res;
    }


    /**
     * Поиск всех перестановок исходной матрицы таких, что переставленная матрица совпадает
     * с целевой. Перестановка - это просто последовательность индексов всех вершин,
     * составляя по которым новую матрицу, мы получим целевую
     *
     * @param source    матрица-источник
     * @param target    целевая матрица
     * @param hardCheck флаг, нужна ли жёсткая проверка; если `hardCheck` равен `false`, то нулевому элементу целевой
     *                  матрицы может соответствовать произвольное значение в переставленной матрице, а если
     *                  `true`, то все элементы целевой и переставленной матриц должны полностью
     *                  совпадать
     * @return список всех перестановок исходной матрицы таких, что переставленная матрица совпадает
     * с целевой
     */
    public static Set<CombinatoricsData> getAllIsomorphicPermutations(int[][] source, int[][] target, boolean hardCheck) {
        // если размеры матриц не совпадают,
        if (source.length != target.length || source[0].length != target[0].length)
            // кидаем исключение
            throw new AssertionError("Размеры матрицы-источника и матрицы-цели не совпадают");

        // степени вершин
        int[] sourceSum = getPowers(source);
        int[] targetSum = getPowers(target);

        // множество подходящих перестановок
        Set<CombinatoricsData> ps = new HashSet<>();
        // запускаем перебор перестановок
        Combinatorics.generatePermutations(target.length, p -> {
                    if (arePermutatedEquals(p, source, target, sourceSum, targetSum, hardCheck))
                        ps.add(new CombinatoricsData(p.clone()));
                }
        );
        return ps;
    }

    /**
     * Получить массив степеней вершин
     *
     * @param connectivityMatrix матрица связности
     * @return массив степеней вершин
     */
    static int[] getPowers(int[][] connectivityMatrix) {
        // создаём массив степеней вершин
        int[] powers = new int[connectivityMatrix.length];
        // перебираем все точки паттерна
        for (int i = 0; i < connectivityMatrix.length; i++)
            // внутри снова перебираем все точки
            for (int j = 0; j < connectivityMatrix.length; j++) {  // заполняем множество
                // если есть ребро от i-ой точки к j-ой
                if (connectivityMatrix[i][j] != 0)
                    // увеличиваем степень i-ой вершины
                    powers[i]++;
                // если есть ребро от j-ой точки к i-ой
                if (connectivityMatrix[j][i] != 0)
                    // увеличиваем степень i-ой вершины
                    powers[i]++;
            }
        // возвращаем массив степеней
        return powers;
    }


    /**
     * Проверка переставленной подматрицы на равенство целевой матрице;
     * Этот метод проверяет, что каждому ненулевому элементу образца
     * должен соответствовать ненулевой элемент в переставленной матрице.
     * В случае жёсткой проверки каждому нулевому элементу целевой матрицы должен
     * соответствовать нулевой элемент переставленной матрицы
     *
     * @param p         перестановка
     * @param source    матрица-источник
     * @param target    матрица-цель
     * @param sourceSum степени вершин у графа-источника
     * @param targetSum степени вершин у графа-цели
     * @param hardCheck флаг, нужна ли жёсткая проверка
     * @return флаг, является ли одна матрица перестановкой другой
     */
    private static boolean arePermutatedEquals(
            int[] p, int[][] source, int[][] target, int[] sourceSum, int[] targetSum,
            boolean hardCheck
    ) {
        // проверяем, что у графов совпадают степени вершин
        for (int i = 0; i < targetSum.length; i++)
            // или её степень меньше степени в паттерне
            if (hardCheck && sourceSum[p[i]] < targetSum[i])
                return false;

        // перебираем все вершины
        for (int i = 0; i < target.length; i++)
            // снова перебираем все вершины
            for (int j = 0; j < target.length; j++) {
                // если проверка жёсткая и элемент из переставленной не совпадает с соответствующим
                // элементом из целевой матрицы
                if (hardCheck && target[i][j] != source[p[i]][p[j]])
                    // возвращаем флаг, что матрицы не равны
                    return false;
                // если проверка нежёсткая, элемент целевой матрицы не равен 0
                // и при этом элемент из переставленной не совпадает с соответствующим
                // элементом из целевой матрицы
                if (!hardCheck && target[i][j] != 0 && target[i][j] != source[p[i]][p[j]])
                    // возвращаем флаг, что матрицы не равны
                    return false;
            }
        // если не встречено неравных элементов, то возвращаем флаг, что матрицы равны
        return true;
    }


}
