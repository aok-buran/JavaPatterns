package center.buran.fast.patterns.misc;

import lombok.Getter;
import java.util.*;

/**
 * Класс данных
 * используется потому, что у массивов
 * не переопределены методы `equals()` и `hashCode()`, поэтому
 * сравнение выполняется между адресами объектов, а не
 * их содержимым
 */
public class CombinatoricsData {
    /**
     * Элементы последовательности
     */
    @Getter
    private final int[] data;

    /**
     * Конструктор
     *
     * @param data элементы комбинации
     */
    public CombinatoricsData(int[] data) {
        this.data = data;
    }

    /**
     * Проверка комбинаций на равенство
     *
     * @param o объект сравнения
     * @return флаг, равны ли комбинации
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CombinatoricsData that = (CombinatoricsData) o;

        return Arrays.equals(data, that.data);
    }

    /**
     * Хэш-код комбинации
     *
     * @return хэш-код комбинации
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

}
