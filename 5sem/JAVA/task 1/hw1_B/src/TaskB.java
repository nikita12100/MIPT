import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class TaskB {
    public static double area(int[][] points, int size) {
        double res = 0;
        for (int i = 0; i < size; ++i) {
            int p1X = 0;
            int p1Y = 0;
            int p2X = 0;
            int p2Y = 0;
            if (i == 0) {
                p1X = points[size - 1][0];
                p1Y = points[size - 1][1];
                p2X = points[i][0];
                p2Y = points[i][1];
            } else {
                p1X = points[i - 1][0];
                p1Y = points[i - 1][1];
                p2X = points[i][0];
                p2Y = points[i][1];
            }
            res += (p1X - p2X) * (p1Y + p2Y);
        }
        return Math.abs((res) / 2);
    }

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            try (BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(System.out))) {

                int n = Integer.parseInt(reader.readLine());

                int[][] points = new int[n][2];

                for (int i = 0; i < n; ++i) {
                    StringTokenizer point = new StringTokenizer(reader.readLine(), " ");
                    points[i][0] = Integer.parseInt(point.nextToken());
                    points[i][1] = Integer.parseInt(point.nextToken());
                }

                System.out.println(area(points, n));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
