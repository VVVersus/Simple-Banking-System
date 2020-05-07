import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalDateTime min;
        LocalDateTime max;
        LocalDateTime date;
        LocalDateTime bord1 = LocalDateTime.parse(scanner.nextLine());
        LocalDateTime bord2 = LocalDateTime.parse(scanner.nextLine());
        if (bord1.compareTo(bord2) > 0) {
            min = bord2;
            max = bord1;
        } else {
            min = bord1;
            max = bord2;
        }
        int n = scanner.nextInt();
        int counter = 0;
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            date = LocalDateTime.parse(scanner.nextLine());
            if (date.compareTo(min) >= 0 && date.isBefore(max)) {
                counter++;
            }
        }
        System.out.println(counter);
    }
}