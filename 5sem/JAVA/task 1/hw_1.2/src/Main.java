import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            try (BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(System.out))) {
                int n = Integer.parseInt(reader.readLine());

                int i = 0;
                int[] a = new int[n];
                int[] maxA = new int[n];   // max (a[0] ... a[i])
                int[] indMaxA = new int[n];   // ind_max (a[0] ... a[i])
                int[] b = new int[n];
                int maxSumm = Integer.MIN_VALUE;
                int maxSummI = 0;
                int maxSummJ = 0;
                StringTokenizer array_a = new StringTokenizer(reader.readLine(), " ");
                StringTokenizer array_b = new StringTokenizer(reader.readLine(), " ");
                while (i < n) {
                    a[i] = Integer.parseInt(array_a.nextToken());
                    if (i == 0) {
                        maxA[0] = a[0];
                        indMaxA[0] = 0;
                    }
                    else {
                        if (a[i] > maxA[i-1]) {
                            maxA[i] = a[i];
                            indMaxA[i] = i;
                        } else {
                            maxA[i] = maxA[i-1];
                            indMaxA[i] = indMaxA[i-1];
                        }
                    }
                    b[i] = Integer.parseInt(array_b.nextToken());
                    if(b[i] + maxA[i] > maxSumm) {
                        maxSumm = b[i] + maxA[i];
                        maxSummI = indMaxA[i];
                        maxSummJ = i;
                    }
                    i++;
                }
                writter.write(maxSummI + " " + maxSummJ);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
