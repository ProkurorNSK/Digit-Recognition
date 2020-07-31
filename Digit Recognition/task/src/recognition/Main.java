package recognition;

import java.io.*;
import java.util.*;

public class Main {

    public static final int COLUMNS = 3;
    public static final int ROWS = 5;
    public static final int COUNT_INPUT = COLUMNS * ROWS;
    public static final int NUMBERS = 10;
    public static final double NU = 0.5;
    public static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("1. Learn the network");
        System.out.println("2. Guess a number");
        System.out.print("Your choice: ");
        String answer = SCANNER.nextLine();
        switch (answer) {
            case "1":
                lean();
                break;
            case "2":
                guess();
                break;
        }
    }

    private static void lean() {
        System.out.println("Learning...");

        double[][] numbers = {{1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1},//0
                {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0},//1
                {1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1},//2
                {1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1},//3
                {1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1},//4
                {1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1},//5
                {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1},//6
                {1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1},//7
                {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},//8
                {1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1},//9
        };

       /* double[][] wIdeal = {{+1, +1, +1, +1, -1, +1, +1, -1, +1, +1, -1, +1, +1, +1, +1},//0
                {-1, +1, -1, -1, +1, -1, -1, +1, -1, -1, +1, -1, -1, +1, -1},//1
                {+1, +1, +1, -1, -1, +1, +1, +1, +1, +1, -1, -1, +1, +1, +1},//2
                {+1, +1, +1, -1, -1, +1, +1, +1, +1, -1, -1, +1, +1, +1, +1},//3
                {+1, -1, +1, +1, -1, +1, +1, +1, +1, -1, -1, +1, -1, -1, +1},//4
                {+1, +1, +1, +1, -1, -1, +1, +1, +1, -1, -1, +1, +1, +1, +1},//5
                {+1, +1, +1, +1, -1, -1, +1, +1, +1, +1, -1, +1, +1, +1, +1},//6
                {+1, +1, +1, -1, -1, +1, -1, -1, +1, -1, -1, +1, -1, -1, +1},//7
                {+1, +1, +1, +1, -1, +1, +1, +1, +1, +1, -1, +1, +1, +1, +1},//8
                {+1, +1, +1, +1, -1, +1, +1, +1, +1, -1, -1, +1, +1, +1, +1},//9
        };

        double[][] bIdeal = {{-1}, {6}, {1}, {0}, {2}, {0}, {-1}, {3}, {-2}, {-1}};*/

        double[][] w = initRandom(NUMBERS, COUNT_INPUT);
        double[][] b = initRandom(NUMBERS, 1);

        for (int k = 0; k < 100; k++) {
            double[][] deltaW = new double[NUMBERS][COUNT_INPUT];
            double[][] number = new double[1][COUNT_INPUT];

            for (int n = 0; n < NUMBERS; n++) {
                number[0] = numbers[n];
                double[][] o = sigmoid(add(multiplicationM(w, transposeMatrix(number)), b));
                for (int i = 0; i < NUMBERS; i++) {
                    for (int j = 0; j < COUNT_INPUT; j++) {
                        deltaW[i][j] += NU * number[0][j] * (numbers[i][j] - o[i][0]);
                    }
                }
            }

            multiplicationN(deltaW, 0.1);
            w = add(w, deltaW);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("weights.neo")))) {
            oos.writeObject(new Weights(w, b));
        } catch (IOException e) {
            return;
        }
        System.out.println("Done! Saved to the file.");
    }

    private static void guess() {
        double[][] a = new double[COUNT_INPUT][1];
        scan(a);
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("weights.neo")))) {
            Weights weights = (Weights) ois.readObject();
            double[][] w = weights.getW();
            double[][] b = weights.getB();

            double[][] result = add(multiplicationM(w, a), b);
            System.out.printf("This number is %d\n", getMaxIndex(result));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static double[][] initRandom(int rows, int columns) {
        Random random = new Random();
        double[][] result = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = random.nextGaussian();
            }
        }
        return result;
    }

    private static void scan(double[][] a) {
        System.out.println("Input grid:");
        for (int i = 0; i < COUNT_INPUT; i += COLUMNS) {
            String text = SCANNER.nextLine();
            for (int j = 0; j < COLUMNS; j++) {
                a[i + j][0] = text.charAt(j) == 'X' ? 1.0 : 0.0;
            }
        }
    }

    private static double[][] transposeMatrix(double[][] a) {
        int rows = a.length;
        int columns = a[0].length;
        double[][] c = new double[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                c[i][j] = a[j][i];
            }
        }
        return c;
    }

    private static double[][] add(double[][] a, double[][] b) {
        int rows = a.length;
        int columns = a[0].length;
        double[][] c = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                c[i][j] = a[i][j] + b[i][j];
            }
        }
        return c;
    }

    private static double[][] multiplicationM(double[][] a, double[][] b) {
        int aRows = a.length;
        int aColumns = a[0].length;
        int bColumns = b[0].length;
        double[][] c = new double[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                for (int k = 0; k < aColumns; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }

    private static void multiplicationN(double[][] a, double number) {
        int rows = a.length;
        int columns = a[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                a[i][j] *= number;
            }
        }
    }

    private static double[][] sigmoid(double[][] x) {
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                x[i][j] = sigmoid(x[i][j]);
            }
        }
        return x;
    }

    private static double sigmoid(double x) {
        return 1.0 / (1 + Math.exp(-1.0 * x));
    }

    private static int getMaxIndex(double[][] result) {
        double maxNumber = Double.MIN_VALUE;
        int maxIndex = 0;
        for (int i = 0; i < result.length; i++) {
            if (maxNumber <= result[i][0]) {
                maxNumber = result[i][0];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}

class Weights implements Serializable {
    private static final long serialVersionUID = 2L;
    private final double[][] w;
    private final double[][] b;

    public Weights(double[][] w, double[][] b) {
        this.w = w;
        this.b = b;
    }

    public double[][] getW() {
        return w;
    }

    public double[][] getB() {
        return b;
    }
}