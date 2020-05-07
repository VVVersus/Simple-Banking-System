import java.time.LocalTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String[] s = scanner.nextLine().split("\\s+");
            if (LocalTime.parse(s[1]).compareTo(LocalTime.of(19, 30).plusMinutes(30)) > 0) {
                result.append(s[0]).append("\n");
            }
        }
        System.out.println(result);
    }
}