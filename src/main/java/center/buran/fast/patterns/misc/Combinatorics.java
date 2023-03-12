package center.buran.fast.patterns.misc;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

/**
 * Класс для работы с комбинаторикой
 */
public class Combinatorics {

    /**
     * Запустить перебор комбинаций
     *
     * @param n        общее число элементов
     * @param k        размер комбинации
     * @param consumer обработчик для каждой найденной комбинации
     */
    public static void combine(int n, int k, Consumer<int[]> consumer) {
        if (k > n)
            throw new AssertionError("размер массива: " + n + " количество элементов " + k);
        // запускаем первый шаг перебора комбинаций
        combineStep(new int[k], 0, n - 1, 0, consumer);
    }

    /**
     * Шаг перебора комбинаций
     *
     * @param consumer    обработчик для каждой найденной комбинации
     * @param combination текущая комбинация
     * @param min         минимальное значение для перебора
     * @param max         максимальное значение для перебора
     * @param pos         индекс текущего элемента
     */
    private static void combineStep(int[] combination, int min, int max, int pos, Consumer<int[]> consumer) {
        // если индекс текущего элемента равен длине комбинации,
        if (pos == combination.length)
            // значит, она составлена, передаём её обработчику
            consumer.accept(combination);
            // если минимальное значение перебора не больше максимального
        else if (min <= max) {
            // приравниваем текущий элемент комбинации минимальному значению перебора
            combination[pos] = min;
            // делаем первый вызов рекурсии, увеличивая на 1 и минимальное значение перебора,
            // и индекс текущего элемента комбинации
            combineStep(combination, min + 1, max, pos + 1, consumer);
            // когда закончатся все рекуррентные вызовы предыдущей команды;
            // Она перебрала все комбинации, у которых после элемента с
            // индексом `pos` нет значений, равных `min+1`; теперь
            // запускаем перебор комбинаций, у которых есть `min+1`
            combineStep(combination, min + 1, max, pos, consumer);
        }
    }

    /**
     * функция-генератор перестановок
     *
     * @param size     количество элементов перестановка
     * @param consumer обработчик найденной перестановки
     */
    public static void generatePermutations(int size, Consumer<int[]> consumer) {
        // стартовая перестановка - последовательность индексов от 0 до target.length-1
        int[] startP = new int[size];
        for (int i = 0; i < size; i++)
            startP[i] = i;

        // запускаем генерацию перестановок
        generatePermutationsStep(startP, 0, consumer);
    }

    /**
     * шаг функции-генератора перестановок
     *
     * @param p        текущая перестановка
     * @param pos      положение
     * @param consumer обработчик найденной перестановки
     */
    private static void generatePermutationsStep(int[] p, int pos, Consumer<int[]> consumer) {
        // Если мы дошли до последнего элемента
        if (pos == p.length - 1)
            consumer.accept(p);
        else  // иначе
            // Перебираем все оставшиеся элементы
            for (int i = pos; i < p.length; i++) {
                // меняем местами текущий элемент и перебираемый
                swap(p, pos, i);
                // Вызываем рекурсию для следующего элемента
                generatePermutationsStep(p, pos + 1, consumer);
                // меняем местами обратно
                swap(p, pos, i);
            }
    }


    /**
     * Получить обратную перестановку
     *
     * @param arr исходная перестановка
     * @return обратная перестановка
     */
    public static int[] getReversePermutation(int[] arr) {
        int[] reverse = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reverse[arr[i]] = i;
        }
        return reverse;
    }


    /**
     * Применить перестановку к массиву
     *
     * @param source источник
     * @param p      перестановка
     * @return обратная перестановка
     */
    public static CombinatoricsData makePermute(int[] source, int[] p) {
        int[] res = new int[source.length];

        for (int i = 0; i < source.length; i++)
            res[p[i]] = source[i];

        return new CombinatoricsData(res);
    }


    /**
     * Применить перестановку к матрице
     *
     * @param source источник
     * @param p      перестановка
     * @return переставленная матрица
     */
    public static int[][] makePermute(int[][] source, int[] p) {
        int[][] res = new int[source.length][source.length];

        for (int i = 0; i < source.length; i++)
            for (int j = 0; j < source.length; j++)
                res[i][j] = source[p[i]][p[j]];

        return res;
    }

    /**
     * Получить случайную комбинацию
     *
     * @param n общее количество элементов
     * @param k количество элементов комбинации
     * @return случайная комбинация
     */
    public static CombinatoricsData getRandomCombination(int n, int k) {
        if (k > n)
            throw new AssertionError("размер массива: " + n + " количество " +
                    "элементов " + k);

        // создаём множество значений случайной комбинации
        Set<Integer> combination = new HashSet<>();

        // пока не набралось достаточное количество значений в множестве
        while (combination.size() < k)
            // добавляем в него случайное значение от 0 до n
            combination.add(Math.abs(ThreadLocalRandom.current().nextInt()) % n);

        // переводим комбинацию из множества в список
        List<Integer> lst = new ArrayList<>(combination);
        // перемешиваем этот список
        Collections.shuffle(lst);

        // массив итоговой комбинации
        int[] res = new int[k];
        // заполняем массив, начиная с нулевого индекса
        int pos = 0;
        for (Integer e : lst)
            res[pos++] = e;

        return new CombinatoricsData(res);
    }


    /**
     * Получить случайную перестановку матрицы
     *
     * @param m исходная матрица
     * @return переставленная матрица
     */
    public static int[][] randomPermute(int[][] m) {
        // получаем случайную перестановку
        int[] p = getRandomPermutation(m.length);
        // применяем её к исходной матрице
        return Combinatorics.makePermute(m, p);
    }

    /**
     * Получить случайную перестановку
     *
     * @param size размер матрицы
     * @return случайная перестановка
     */
    public static int[] getRandomPermutation(int size) {
        if (size < 1)
            throw new AssertionError("Недопустимый размер перестановки: " + size);

        // заполняем массив данных заданной длины случайными числами
        int[] r = new int[size];
        for (int i = 0; i < size; i++)
            r[i] = ThreadLocalRandom.current().nextInt() % 20000 - 10000;

        // заполняем массив заданной длины индексами
        int[] rc = new int[r.length];
        for (int i = 0; i < r.length; i++)
            rc[i] = i;

        // сортируем совместно два массива по значению из массива данных
        for (int i = 0; i < r.length; i++)
            for (int j = i + 1; j < r.length; j++)
                if (r[i] < r[j]) {
                    int c = r[i];
                    r[i] = r[j];
                    r[j] = c;

                    c = rc[i];
                    rc[i] = rc[j];
                    rc[j] = c;
                }
        // возвращаем отсортированный по значениям массива данных
        // массив индексов
        return rc;
    }


    /**
     * Получить подматрицу по массиву используемых индексов
     *
     * @param source источник
     * @param select массив используемых индексов
     * @return подматрица
     */
    public static int[][] getSubMatrix(int[][] source, int[] select) {
        int[][] res = new int[select.length][select.length];

        for (int i = 0; i < select.length; i++)
            for (int j = 0; j < select.length; j++)
                res[i][j] = source[select[i]][select[j]];

        return res;
    }


    /**
     * Проверка на равенство двух матриц
     *
     * @param a первая матрица
     * @param b вторая матрица
     * @return флаг, равны ли матрицы
     */
    public static boolean areEqual(int[][] a, int[][] b) {
        if (a.length != b.length)
            return false;
        if (a[0].length != b[0].length)
            return false;

        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[0].length; j++)
                if (a[i][j] != b[i][j])
                    return false;

        return true;
    }


    /**
     * Получить случайную матрицу
     *
     * @param min         минимальный размер матрицы
     * @param max         максимальный размер матрицы
     * @param minVal      максимальное значение матрицы по модулю
     * @param maxVal      максимальное значение матрицы по модулю
     * @param nonZeroPart доля ненулевых элементов
     * @return случайная матрица
     */
    public static int[][] randomMatrix(int min, int max, int minVal, int maxVal, double nonZeroPart) {
        // получаем случайный размер матрицы
        int n = Math.abs(ThreadLocalRandom.current().nextInt()) % (max - min) + min;

        // создаём матрицу
        int[][] r = new int[n][n];

        // и заполняем её случайными значениями
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (ThreadLocalRandom.current().nextDouble() < nonZeroPart)
                    r[i][j] = minVal + Math.abs(ThreadLocalRandom.current().nextInt()) % (maxVal - minVal);

        return r;
    }

    /**
     * поменять местами элементы массива arr с индексами l и r
     *
     * @param arr массив
     * @param l   индекс первого элемента
     * @param r   индекс второго элемента
     */
    private static void swap(int[] arr, int l, int r) {
        int tmp = arr[l];
        arr[l] = arr[r];
        arr[r] = tmp;
    }

    /**
     * Запрещённый конструктор
     */
    private Combinatorics() {
        throw new AssertionError("Этот конструктор вызывать нельзя");
    }
}
