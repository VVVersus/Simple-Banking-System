import java.time.LocalDate;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        String[] s = new Scanner(System.in).nextLine().split("\\s+");
        LocalDate date = LocalDate.parse(s[0]);
        int n = Integer.parseInt(s[1]);
        LocalDate newDate = date.plusDays(n);
        System.out.println(newDate.getDayOfYear() == 1);
    }
}