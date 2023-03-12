package center.buran.fast.patterns.misc;

import lombok.Getter;

/**
 * Построитель паттернов
 */
public class PatternBuilder {
    /**
     * матрица флагов, не занята ли точка каким-то паттерном
     * каждое добавления паттерна соответствующие ячейки заполняются
     * значениями `true`
     */
    private final boolean[][] isModified;
    /**
     * Текущая матрица связности
     */
    @Getter
    private final int[][] data;

    /**
     * Конструктор
     *
     * @param data матрица-результат
     */
    public PatternBuilder(int[][] data) {
        this.data = data;
        isModified = new boolean[data.length][data.length];
    }

    /**
     * Добавить паттерн
     *
     * @param pattern           паттерн
     * @param combinatoricsData комбинация точек результирующей матрицы в которые нужно добавить паттерн
     * @return флаг, получилось ли добавить
     */
    public boolean putPattern(int[][] pattern, CombinatoricsData combinatoricsData) {
        if (pattern.length != combinatoricsData.getData().length)
            throw new AssertionError("размер паттерна " + pattern.length + " не равен" +
                    " размеру комбинации " + combinatoricsData.getData().length);

        // перебираем пары элементов комбинаций
        for (int i = 0; i < combinatoricsData.getData().length; i++)
            for (int j = 0; j < combinatoricsData.getData().length; j++) {
                // получаем две координаты по соответствующим элементам из перебираемой пары
                int posX = combinatoricsData.getData()[i];
                int posY = combinatoricsData.getData()[j];
                // если элемент в матрице уже занят другим паттерном и при
                // этом значение в матрице не совпадает с новым
                if (isModified[posX][posY] && pattern[i][j] != data[posX][posY])
                    // возвращаем флаг, что не удалось добавить паттерн
                    return false;
            }

        // перебираем пары элементов комбинаций
        for (int i = 0; i < combinatoricsData.getData().length; i++)
            for (int j = 0; j < combinatoricsData.getData().length; j++) {
                // получаем две координаты по соответствующим элементам из перебираемой пары
                int posX = combinatoricsData.getData()[i];
                int posY = combinatoricsData.getData()[j];
                // заполняем соответствующий элемент матрицы
                data[posX][posY] = pattern[i][j];
                // выставляем флаг, что э
                isModified[posX][posY] = true;
            }
        // возвращаем флаг, что удалось добавить паттерн
        return true;
    }
}
