package recognition;

import java.util.*;

public class Main {

    public static final int COLUMNS = 3;
    public static final int ROWS = 5;
    public static final int COUNT_INPUT = COLUMNS * ROWS;

    public static void main(String[] args) {
        int[][] a = new int[COUNT_INPUT][1];
        init(a);

        int[][] w = {{+1, +1, +1, +1, -1, +1, +1, -1, +1, +1, -1, +1, +1, +1, +1},//0
                     {-1, +1, -1, -1, +1, -1, -1, +1, -1, -1, +1, -1, -1, +1, -1},//1
                     {+1, +1, +1, -1, -1, +1, +1, +1, +1, +1, -1, -1, +1, +1, +1},//2
                     {+1, +1, +1, -1, -1, +1, +1, +1, +1, -1, -1, +1, +1, +1, +1},//3
                     {+1, -1, +1, +1, -1, +1, +1, +1, +1, -1, -1, +1, -1, -1, +1},//4
                     {+1, +1, +1, +1, -1, -1, +1, +1, +1, -1, -1, +1, +1, +1, +1},//5
                     {+1, +1, +1, +1, -1, -1, +1, +1, +1, +1, -1, +1, +1, +1, +1},//6
                     {+1, +1, +1, -1, -1, +1, -1, -1, +1, -1, -1, +1, -1, -1, +1},//7
                     {+1, +1, +1, +1, -1, +1, +1, +1, +1, +1, -1, +1, +1, +1, +1},//8
                     {+1, +1, +1, +1, -1, +1, +1, +1, +1, -1, -1, +1, +1, +1, +1},//1
        };

        int[][] b = {{-1}, {6}, {1}, {0}, {2}, {0}, {-1}, {3}, {-2}, {-1}};

        int[][] result = add(multiplicationM(w, a), b);

        System.out.println(getMaxIndex(result));
    }

    private static int[][] add(int[][] a, int[][] b) {
        int rows = a.length;
        int columns = a[0].length;
        int[][] c = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                c[i][j] = a[i][j] + b[i][j];
            }
        }
        return c;
    }

    private static int[][] multiplicationM(int[][] a, int[][] b) {
        int aRows = a.length;
        int aColumns = a[0].length;
        int bColumns = b[0].length;
        int[][] c = new int[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                for (int k = 0; k < aColumns; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }

    private static int getMaxIndex(int[][] result) {
        int maxNumber = Integer.MIN_VALUE;
        int maxIndex = 0;
        for (int i = 0; i < result.length; i++) {
            if(maxNumber <= result[i][0]){
                maxNumber = result[i][0];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private static void init(int[][] a) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < COUNT_INPUT; i += COLUMNS) {
            String text = scanner.next();
            for (int j = 0; j < COLUMNS; j++) {
                a[i + j][0] = text.charAt(j) == 'X' ? 1 : 0;
            }
        }
    }
}
