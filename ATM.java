import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class User {
    public static void main(String[] args) {
        ATMM atm = new ATMM();
        atm.start();
    }

    private String userId;
    private String pin;
    private double accountBalance;
    private List<String> transactionHistory;

    public User(String userId, String pin, double initialBalance) {
        this.userId = userId;
        this.pin = pin;
        this.accountBalance = initialBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double newBalance) {
        accountBalance = newBalance;
    }

    public void addTransactionToHistory(String transaction) {
        transactionHistory.add(transaction);
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }
}

class ATMM {
    private User currentUser;
    private Scanner scanner;

    public ATMM() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the ATM!");
        System.out.print("User ID: ");
        String userId = scanner.nextLine();
        System.out.print("PIN: ");
        String pin = scanner.nextLine();

        if (authenticateUser(userId, pin)) {
            showMenu();
        } else {
            System.out.println("Authentication failed. Exiting...");
        }
    }

    private boolean authenticateUser(String userId, String pin) {
        if (userId.equals("12345") && pin.equals("54321")) {
            currentUser = new User(userId, pin, 1000.0);
            return true;
        }
        return false;
    }

    private void showMenu() {
        int choice;
        do {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Transactions History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showTransactionHistory();
                    break;
                case 2:
                    performWithdrawal();
                    break;
                case 3:
                    performDeposit();
                    break;
                case 4:
                    performTransfer();
                    break;
                case 5:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    private void showTransactionHistory() {
        List<String> transactions = currentUser.getTransactionHistory();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("--- Transaction History ---");
            for (String transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    private void performWithdrawal() {
        System.out.print("Enter the amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        if (amount <= 0) {
            System.out.println("Invalid amount. Withdrawal failed.");
        } else if (amount > currentUser.getAccountBalance()) {
            System.out.println("Insufficient funds. Withdraw");
        }
    }

    private void performDeposit() {
        System.out.print("Enter the amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("Invalid amount. Deposit failed.");
        } else {
            double newBalance = currentUser.getAccountBalance() + amount;
            currentUser.setAccountBalance(newBalance);
            String transaction = "Deposit: +" + amount;
            currentUser.addTransactionToHistory(transaction);
            System.out.println("Deposit successful. New balance: " + newBalance);
        }
    }

    private void performTransfer() {
        System.out.print("Enter the user ID to transfer funds: ");
        String recipientUserId = scanner.nextLine();

        User recipient = getUserById(recipientUserId);

        if (recipient == null) {
            System.out.println("Invalid recipient user ID. Transfer failed.");
        } else {
            System.out.print("Enter the amount to transfer: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            if (amount <= 0) {
                System.out.println("Invalid amount. Transfer failed.");
            } else if (amount > currentUser.getAccountBalance()) {
                System.out.println("Insufficient funds. Transfer failed.");
            } else {
                double currentUserNewBalance = currentUser.getAccountBalance() - amount;
                double recipientNewBalance = recipient.getAccountBalance() + amount;
                currentUser.setAccountBalance(currentUserNewBalance);
                recipient.setAccountBalance(recipientNewBalance);

                String currentUserTransaction = "Transfer to " + recipient.getUserId() + ": -" + amount;
                String recipientTransaction = "Transfer from " + currentUser.getUserId() + ": +" + amount;

                currentUser.addTransactionToHistory(currentUserTransaction);
                recipient.addTransactionToHistory(recipientTransaction);

                System.out.println("Transfer successful. Your new balance: " + currentUserNewBalance);
            }
        }
    }

    private User getUserById(String userId) {
        Map<String, User> users = new HashMap<>();
        users.put("12345", new User("12345", "54321", 1000.0));
        users.put("67890", new User("67890", "09876", 2000.0));

        return users.get(userId);
    }

}
