package banking;

import java.sql.*;
import java.util.Random;

public class Account {

    private int id;
    private String cardNumber;
    private String cardPIN;
    private int balance;

    public static int getCheckSum(String numbers) {
        String[] s = numbers.split("");
        int sum = 0;
        for (int i = 0; i < 15; i++) {
            int digit = Integer.parseInt(s[i]);
            if (i % 2 == 0) {
                sum += (digit * 2 > 9) ? digit * 2 - 9 : digit * 2;
            } else {
                sum += digit;
            }
        }
        return (10 - sum % 10) % 10;
    }

    public static boolean checkCardNumber(String receiverCardNumber) {
        return getCheckSum(receiverCardNumber) == Integer.parseInt(String.valueOf(receiverCardNumber.charAt(15)));
    }

    private static String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = null;

        while (true) {
            cardNumber = new StringBuilder();
            cardNumber.append("400000");
            for (int i = 0; i < 9; i++) {
                cardNumber.append(random.nextInt(10));
            }

            cardNumber.append(getCheckSum(cardNumber.toString()));

            if (!cardExists(cardNumber.toString())) {
                return cardNumber.toString();
            }
        }
    }

    private static String generateCardPIN() {
        Random random = new Random();
        StringBuilder cardPIN = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            cardPIN.append(random.nextInt(10));
        }
        return cardPIN.toString();
    }

    public static void removeAccount(Account account) {
        String query = "DELETE FROM card WHERE id = ?;";

        try (PreparedStatement pstmt = Bank.getConn().prepareStatement(query)) {
            pstmt.setInt(1, account.getId());
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean cardExists(String receiverCardNumber) {
        String query = "SELECT COUNT(id) FROM card WHERE number = ?;";

        try (PreparedStatement pstmt = Bank.getConn().prepareStatement(query)) {

            pstmt.setString(1, receiverCardNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static void insertNewAccount(Account account) {
        String sql = "INSERT INTO card (id, number, pin, balance) VALUES (?, ?, ?, ?);";

        try (PreparedStatement pstmt = Bank.getConn().prepareStatement(sql)) {

            pstmt.setInt(1, account.id);
            pstmt.setString(2, account.cardNumber);
            pstmt.setString(3, account.cardPIN);
            pstmt.setInt(4, account.balance);

            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static int getNextId() {
        String sql = "SELECT MAX(id) + 1 FROM card;";

        try (Statement stmt = Bank.getConn().createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            return rs.getInt(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static Account getAccountFromDB(String cardNumber, String cardPIN) {
        String sql = "SELECT id, number, pin, balance FROM card WHERE number = ? AND pin = ?;";

        try (PreparedStatement pstmt = Bank.getConn().prepareStatement(sql)) {

            pstmt.setString(1, cardNumber);
            pstmt.setString(2, cardPIN);

            ResultSet rs = pstmt.executeQuery();
            return new Account(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account() {
        this(getNextId(), generateCardNumber(), generateCardPIN(), 0);
        insertNewAccount(this);
    }

    public Account(int id, String cardNumber, String cardPIN, int balance) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cardPIN = cardPIN;
        this.balance = balance;
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardPIN() {
        return cardPIN;
    }

    public int getBalance() {
        String query = "SELECT balance FROM card WHERE id = ?;";
        try (PreparedStatement pstmt = Bank.getConn().prepareStatement(query);) {

            pstmt.setInt(1, this.id);

            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("balance");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getId() {
        return id;
    }

    public void addIncome(int income) {
        String sql = "UPDATE card SET balance = ? WHERE id = ?";

        try (PreparedStatement pstmt = Bank.getConn().prepareStatement(sql)) {

            pstmt.setInt(1, this.getBalance() + income);
            pstmt.setInt(2, this.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Your card number:\n" +
                cardNumber + "\n" +
                "Your card PIN:\n" + cardPIN + "\n";
    }

    public String toSQL() {
        return String.format("(%d, '%s', '%s', %d)", id, cardNumber, cardPIN, balance);
    }

    public void transfer(String receiverCardNumber, int amount) {
        if (this.getBalance() < amount) {
            System.out.println("No enough money on your account!");
            return;
        }

        String sqlWithDraw = "UPDATE card SET balance = balance - ? WHERE id = ?;";

        String sqlIncome = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (PreparedStatement pstmtWithDraw = Bank.getConn().prepareStatement(sqlWithDraw);
             PreparedStatement pstmtIncome = Bank.getConn().prepareStatement(sqlIncome);) {

            pstmtWithDraw.setInt(1, amount);
            pstmtWithDraw.setInt(2, this.getId());
            pstmtWithDraw.executeUpdate();

            pstmtIncome.setInt(1, amount);
            pstmtIncome.setString(2, receiverCardNumber);
            pstmtIncome.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
