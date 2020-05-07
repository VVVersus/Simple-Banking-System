package banking;

public class Main {

    public static void main(String[] args) {
        Bank bank = null;
        String fileName = null;
        if(args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if ("-fileName".equals(args[i])) {
                    fileName = args[i + 1];
                    break;
                }
            }

        }
        bank = new Bank(fileName);
        bank.work();
    }
}
