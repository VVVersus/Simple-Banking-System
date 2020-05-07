import java.time.LocalDate;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] s = scanner.nextLine().split("\\s+");
        LocalDate x = LocalDate.parse(s[0]);
        LocalDate m = LocalDate.parse(s[1]);
        LocalDate n = LocalDate.parse(s[2]);

        System.out.println((x.isAfter(m) && x.isBefore(n)) || (x.isAfter(n) && x.isBefore(m)));
    }
}