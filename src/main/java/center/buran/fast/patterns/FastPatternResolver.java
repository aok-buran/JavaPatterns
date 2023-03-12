package center.buran.fast.patterns;


import center.buran.fast.patterns.misc.CombinatoricsData;

import java.util.*;
import java.util.function.Consumer;

/**
 * Класс быстрого поиска изоморфных подграфов
 */
public class FastPatternResolver {

    /**
     * Быстрый поиск изоморфных подграфов
     *
     * @param source    дата-граф
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
        if (pattern.length > source.length)
            throw new AssertionError("размер паттерна: " + pattern.length + " превышает " +
                    "размер дата-графа " + source.length);

        // множество найденных паттернов
        List<CombinatoricsData> res = new ArrayList<>();
        // флаги, использована ли уже та или иная точка
        boolean[] used = new boolean[source.length];
        // текущая комбинация
        int[] combination = new int[pattern.length];
        // степени вершин в дата-графе
        int[] sourcePowers = PatternResolver.getPowers(source);
        // степени вершин в паттерне
        int[] patternPowers = PatternResolver.getPowers(pattern);

        // запускаем рекурсию
        findPatternStep(
                c -> res.add(new CombinatoricsData(c.clone())), used, source, pattern,
                sourcePowers, patternPowers, 0, combination, hardCheck
        );
        // возвращаем множество найденных паттернов
        return res;
    }

    /**
     * Шаг поиска паттерна
     *
     * @param consumer      обработчик найденной комбинации
     * @param used          массив флагов, использовалась ли уже i-я точка
     * @param source        дата-граф
     * @param pattern       искомый паттерн
     * @param sourcePowers  степени вершин источника
     * @param patternPowers степени вершин
     * @param cnt           кол-во обработанных элементов
     * @param combination   массив комбинации
     * @param hardCheck     флаг, нужна ли жёсткая проверка
     */
    private static void findPatternStep(
            Consumer<int[]> consumer, boolean[] used, int[][] source, int[][] pattern,
            int[] sourcePowers, int[] patternPowers, int cnt, int[] combination, boolean hardCheck
    ) {
        // если уже выбрана хотя бы одна точка для комбинации и при этом
        // матрица, составленная из дата-графа по этой комбинации
        // не совпадает с соответствующей подматрицей паттерна
        // (нам нужно проверить только новые элементы,
        // они находятся в самом нижнем ряду и в самой правой колонке)
        if (cnt > 0 && !checkMatrixEdge(source, pattern, combination, cnt, hardCheck))
            return;

        // если получено нужное кол-во элементов комбинации
        if (cnt == pattern.length)
            // обрабатываем её
            consumer.accept(combination.clone());
        else
            // в противном случае перебираем все вершины графа
            for (int i = 0; i < source.length; i++) {
                // если i-я точка уже использована или её степень меньше степени
                // следующей точки в паттерне
                if (used[i] || (hardCheck && sourcePowers[i] < patternPowers[cnt]))
                    continue;

                // говорим, что i-я точка использована
                used[i] = true;
                // добавляем индекс точки в комбинацию
                combination[cnt] = i;

                // вызываем следующий шаг рекурсии
                findPatternStep(
                        consumer, used, source, pattern, sourcePowers,
                        patternPowers, cnt + 1, combination, hardCheck
                );

                // возвращаем значение флага
                used[i] = false;
            }
    }

    /**
     * Проверить совпадение самого правого столбца
     * и самой нижней строки подматрицы дата-графа,
     * полученной по соответствующей комбинации
     * (проверяются только новые элементы,
     * они находятся в самом нижнем ряду и в самой правой колонке)
     *
     * @param source      дата-граф
     * @param pattern     паттерн
     * @param combination комбинация
     * @param cnt         кол-во элементов в комбинации
     * @param hardCheck   флаг, нужна ли жёсткая проверка
     * @return флаг, совпадают ли матрицы по углу
     */
    private static boolean checkMatrixEdge(
            int[][] source, int[][] pattern, int[] combination, int cnt, boolean hardCheck
    ) {
        // если кол-во элементов в комбинации больше её размера
        if (cnt > combination.length)
            // кидаем исключение
            throw new AssertionError("размер комбинации " + Arrays.toString(combination) + " меньше " +
                    "требуемой длины " + cnt);

        // перебираем элементы самого нижнего ряда матрицы
        // и самой правой колонки
        for (int i = 0; i < cnt; i++) {
            // если жёсткая проверка
            if (hardCheck) {
                // если элемент из паттерна в правом столбце не совпадает с соответствующим
                // элементом из дата-графа
                if (pattern[i][cnt - 1] != source[combination[i]][combination[cnt - 1]])
                    // возвращаем флаг, что матрицы не равны по уголку
                    return false;
                // если элемент из паттерна в нижней строке не совпадает с соответствующим
                // элементом из дата-графа
                if (pattern[cnt - 1][i] != source[combination[cnt - 1]][combination[i]])
                    // возвращаем флаг, что матрицы не равны по уголку
                    return false;
            } else { // если нежёсткая проверка
                // если элемент из паттерна в правом столбце ненулевой и не совпадает с соответствующим
                // элементом из дата-графа
                if (pattern[i][cnt - 1] != 0 && pattern[i][cnt - 1] != source[combination[i]][combination[cnt - 1]])
                    // возвращаем флаг, что матрицы не равны по уголку
                    return false;
                // если элемент из паттерна в нижней строке ненулевой и не совпадает с соответствующим
                // элементом из дата-графа
                if (pattern[cnt - 1][i] != 0 && pattern[cnt - 1][i] != source[combination[cnt - 1]][combination[i]])
                    // возвращаем флаг, что матрицы не равны по уголку
                    return false;
            }
        }

        // если не встречено неравных элементов, то возвращаем флаг, что матрицы равны по уголку
        return true;
    }

    /**
     * Запрещённый конструктор
     */
    private FastPatternResolver() {
        throw new AssertionError("Этот конструктор вызывать нельзя");
    }

}


