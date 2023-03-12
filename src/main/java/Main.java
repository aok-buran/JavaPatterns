import center.buran.fast.patterns.FastPatternResolver;
import center.buran.fast.patterns.misc.CombinatoricsData;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // паттерн
        int[][] pattern = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        // матрица связности дата-графа
        int[][] source = new int[][]{
                {1, 3, 2, 2, 3, 4, 6},
                {7, 9, 1, 8, 0, 3, 3},
                {4, 0, 5, 4, 6, 6, 0},
                {4, 6, 4, 5, 5, 1, 1},
                {7, 2, 8, 3, 9, 7, 8},
                {5, 1, 0, 6, 3, 1, 2},
                {3, 6, 1, 0, 6, 4, 5}
        };

        // получаем список найденных комбинаций
        List<CombinatoricsData> fastPatterns = FastPatternResolver.getAllPatterns(source, pattern, true);
        // для каждой комбинации выводим её элементы
        for (CombinatoricsData cd : fastPatterns)
            System.out.println(Arrays.toString(cd.getData()));
    }
}
