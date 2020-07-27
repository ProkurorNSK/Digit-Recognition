package recognition;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] a = new int[9];
        int[] w = {2, 1, 2, 4, -4, 4, 2, -1, 2};
        int b = -5;
        for (int i = 0; i < 9; i += 3) {
            String text = scanner.next();
            for (int j = 0; j < 3; j++) {
                a[i + j] = text.charAt(j) == 'X' ? 1 : 0;
            }
        }

        int result = b;
        for (int i = 0; i < 9; i++) {
            result += a[i] * w[i];
        }
        System.out.println(result > 0 ? 0 : 1);
    }
}
