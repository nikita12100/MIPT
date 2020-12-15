import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Table table = new Table(sc.nextLine(), sc.nextLine());

        try {
            while (sc.hasNext()) {
                table.make2Step(sc.nextLine());
            }
            table.printSetTable();
        } catch (MoveException ex) {
            sc.close();
            System.out.println(ex.getMessage());
        }

        sc.close();
    }
}
