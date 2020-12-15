import java.util.Scanner;

public class Main {

    private static int countIndexes(int n1, int[] arrayA, int n2, int[] arrayB, int k) {
        int lastJ = 0;
        int countPairs = 0;

        for (int i = n2 - 1; i >= 0; --i) {
            int b = arrayB[i];

            for (int j = lastJ; j < n1; ++j) {
                int sum = b + arrayA[j];
                if (sum == k) {
                    ++countPairs;
                } else if (sum > k) {
                    lastJ = j;
                    break;
                }
            }
        }
        return countPairs;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n1 = sc.nextInt();
        int[] a = new int[n1];
        for (int i = 0; i < n1; ++i) {
            a[i] = sc.nextInt();
        }
        int n2 = sc.nextInt();
        int[] b = new int[n2];
        for (int i = 0; i < n2; ++i) {
            b[i] = sc.nextInt();
        }
        int k = sc.nextInt();

        System.out.println(countIndexes(n1, a, n2, b, k));

        sc.close();
    }

}
