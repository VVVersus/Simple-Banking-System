package banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Bank {
    private static final Scanner scanner = new Scanner(System.in);
    private static Connection conn;

    private Account activeAccount;



    public Bank(String dbFileName) {
        String url = "jdbc:sqlite:BankSystem.db";
        if (dbFileName != null) {
            url = "jdbc:sqlite:" + dbFileName;
        }
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        initializeDB();
    }

    public static Connection getConn() {
        return conn;
    }

    private void initializeDB() {
        try (Statement stmt = conn.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS card (id INTEGER, number TEXT, pin TEXT, balance INTEGER DEFAULT 0);";

            stmt.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void work() {
        int option = 1;
        while (option != 0) {
            System.out.println( "1. Create account\n" +
                                "2. Log into account\n" +
                                "0. Exit");
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    Account newAccount = new Account();
                    System.out.println("\nYour card have been created");
                    System.out.println(newAccount.toString());
                    break;
                case 2:
                    logIn();
                    break;
            }
        }
        System.out.println("Bye!");
    }

    public void printAccountMenu() {
        int option;
        while (true) {
            System.out.println("\n1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit");
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    System.out.println(activeAccount.getBalance());
                    break;
                case 2:
                    System.out.println("Enter income amount:");
                    activeAccount.addIncome(scanner.nextInt());
                    break;
                case 3:
                    System.out.println("Enter receiver's card number:");
                    String receiverCardNumber = scanner.nextLine();
                    if (receiverCardNumber.equals(activeAccount.getCardNumber())) {
                        System.out.println("You can't transfer money to the same account!");
                        return;
                    } else if (!Account.checkCardNumber(receiverCardNumber)) {
                        System.out.println("Probably you made mistake in card number. Please try again!");
                        return;
                    } else if (!Account.cardExists(receiverCardNumber)) {
                        System.out.println("Such a card does not exist.");
                        return;
                    }
                    System.out.println("Enter transfer amount:");
                    int amount = scanner.nextInt();
                    activeAccount.transfer(receiverCardNumber, amount);
                    System.out.println("\nYou have successfully transferred money!\n");
                    break;
                case 4:
                    Account.removeAccount(activeAccount);
                    activeAccount = null;
                    System.out.println("\nYou have successfully closed your account!\n");
                    return;
                case 5:
                    activeAccount = null;
                    System.out.println("\nYou have successfully logged out!\n");
                    return;
                case 0:
                    System.out.println("Bye!");
                    System.exit(0);
            }

        }
    }

    private void logIn() {
        System.out.println("\nEnter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String cardPIN = scanner.nextLine();

            if ((activeAccount = Account.getAccountFromDB(cardNumber, cardPIN)) != null) {
                System.out.println("You have successfully logged in!");
                printAccountMenu();
                return;
            }

        System.out.println("Wrong card number or PIN!");
        activeAccount = null;
    }
}
