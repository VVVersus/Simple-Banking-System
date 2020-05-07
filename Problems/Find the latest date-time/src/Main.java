import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        LocalDateTime latest = LocalDateTime.of(0, 1, 1, 0, 0);
        LocalDateTime date;
        for (int i = 0; i < n; i++) {
            date = LocalDateTime.parse(scanner.nextLine());
            if (date.isAfter(latest)) {
                latest = date;
            }
        }
        System.out.println(latest);
    }
}