import java.time.LocalTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] s;
        s = scanner.nextLine().split("\\s+");
        LocalTime r1Min = LocalTime.parse(s[0]);
        LocalTime r1Max = LocalTime.parse(s[1]);
        s = scanner.nextLine().split("\\s+");
        LocalTime r2Min = LocalTime.parse(s[0]);
        LocalTime r2Max = LocalTime.parse(s[1]);
        System.out.println(r2Min.compareTo(r1Min) >= 0 && r2Min.compareTo(r1Max) <= 0
                || r2Max.compareTo(r1Min) >= 0 && r2Max.compareTo(r1Max) <= 0
                || r1Min.compareTo(r2Min) >= 0 && r1Min.compareTo(r2Max) <= 0
                || r1Max.compareTo(r2Min) >= 0 && r1Max.compareTo(r2Max) <= 0);
    }
}