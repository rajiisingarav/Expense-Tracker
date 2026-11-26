import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Main.java
 * Entry point of the Personal Expense Tracker.
 * Shows the menu, reads and validates user input, and calls ExpenseManager.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final FileHandler fileHandler = new FileHandler();
    private static ExpenseManager manager;

    // ------------------------------------------------------------
    // Input helpers (every method keeps asking until the input is valid)
    // ------------------------------------------------------------

    /** Reads one line. If the input stream ends (e.g. Ctrl+D), save and quit safely. */
    private static String readLine(String message) {
        System.out.print(message);
        if (!scanner.hasNextLine()) {
            System.out.println();
            fileHandler.saveExpenses(manager.getAllExpenses());
            System.out.println("Input ended. Data saved. Goodbye!");
            System.exit(0);
        }
        return scanner.nextLine().trim();
    }

    /** Reads a whole number between min and max. */
    private static int readInt(String message, int min, int max) {
        while (true) {
            String input = readLine(message);
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Invalid input! Enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a whole number.");
            }
        }
    }

    /** Reads text that must not be empty. */
    private static String readText(String message) {
        while (true) {
            String input = readLine(message);
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Invalid input! This field cannot be empty.");
        }
    }

    /** Reads an amount that must be a number greater than 0. */
    private static double readAmount(String message) {
        while (true) {
            String input = readLine(message);
            try {
                double value = Double.parseDouble(input);
                if (Double.isNaN(value) || Double.isInfinite(value) || value <= 0) {
                    System.out.println("Invalid input! Amount must be greater than 0.");
                } else if (value > 100000000) {
                    System.out.println("Invalid input! Amount is too large.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number (example: 250.50).");
            }
        }
    }

    /** Reads a date in yyyy-MM-dd format. If allowToday is true, pressing Enter means today. */
    private static LocalDate readDate(String message, boolean allowToday) {
        while (true) {
            String input = readLine(message);
            if (allowToday && input.isEmpty()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(input);   // only accepts real dates like 2026-03-15
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date! Use the format yyyy-MM-dd (example: 2026-03-15).");
            }
        }
    }

    // ------------------------------------------------------------
    // Display helpers
    // ------------------------------------------------------------

    private static void showMenu() {
        System.out.println("\n========== PERSONAL EXPENSE TRACKER ==========");
        System.out.println(" 1. Add New Expense");
        System.out.println(" 2. View All Expenses");
        System.out.println(" 3. Search Expenses by Category");
        System.out.println(" 4. Search Expenses by Date");
        System.out.println(" 5. Delete an Expense");
        System.out.println(" 6. Show Total Expenses");
        System.out.println(" 7. Show Category-wise Summary");
        System.out.println(" 8. Set Monthly Budget");
        System.out.println(" 9. Show Remaining Budget");
        System.out.println("10. Save Expenses to File");
        System.out.println("11. Exit");
        System.out.println("==============================================");
    }

    /** Prints a list of expenses as a table. */
    private static void printExpenses(ArrayList<Expense> list) {
        if (list.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        System.out.printf("%-4s %-24s %10s  %-14s %s%n", "ID", "Title", "Amount", "Category", "Date");
        System.out.println("---------------------------------------------------------------------");
        for (Expense e : list) {
            String title = e.getTitle();
            if (title.length() > 24) {
                title = title.substring(0, 21) + "...";   // keep the table neat
            }
            System.out.printf("%-4d %-24s %10.2f  %-14s %s%n",
                    e.getId(), title, e.getAmount(), e.getCategory(), e.getDate());
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Number of expenses: " + list.size());
    }

    // ------------------------------------------------------------
    // Menu actions
    // ------------------------------------------------------------

    private static void addExpense() {
        String title = readText("Enter expense title: ");
        double amount = readAmount("Enter amount: ");
        String category = readText("Enter category (e.g. Food, Travel): ");
        LocalDate date = readDate("Enter date (yyyy-MM-dd) or press Enter for today: ", true);

        Expense added = manager.addExpense(title, amount, category, date);
        fileHandler.saveExpenses(manager.getAllExpenses());   // auto-save
        System.out.println("Expense added successfully! (ID: " + added.getId() + ")");
    }

    private static void searchByCategory() {
        String category = readText("Enter category to search: ");
        ArrayList<Expense> result = manager.searchByCategory(category);
        printExpenses(result);
        if (!result.isEmpty()) {
            double total = 0;
            for (Expense e : result) {
                total += e.getAmount();
            }
            System.out.printf("Total for this category: %.2f%n", total);
        }
    }

    private static void searchByDate() {
        LocalDate date = readDate("Enter date to search (yyyy-MM-dd): ", false);
        printExpenses(manager.searchByDate(date));
    }

    private static void deleteExpense() {
        if (manager.getAllExpenses().isEmpty()) {
            System.out.println("There are no expenses to delete.");
            return;
        }
        int id = readInt("Enter the ID of the expense to delete: ", 1, Integer.MAX_VALUE);
        Expense deleted = manager.deleteExpense(id);
        if (deleted == null) {
            System.out.println("No expense found with ID " + id + ".");
        } else {
            fileHandler.saveExpenses(manager.getAllExpenses());   // auto-save
            System.out.println("Deleted: " + deleted.getTitle() + " (" + String.format("%.2f", deleted.getAmount()) + ")");
        }
    }

    private static void showCategorySummary() {
        Map<String, Double> summary = manager.getCategorySummary();
        if (summary.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        double total = manager.getTotalExpenses();
        System.out.printf("%-16s %12s %8s%n", "Category", "Total", "Share");
        System.out.println("----------------------------------------");
        for (Map.Entry<String, Double> entry : summary.entrySet()) {
            double percent = entry.getValue() / total * 100;
            System.out.printf("%-16s %12.2f %7.1f%%%n", entry.getKey(), entry.getValue(), percent);
        }
        System.out.println("----------------------------------------");
        System.out.printf("%-16s %12.2f%n", "TOTAL", total);
    }

    private static void setBudget() {
        double budget = readAmount("Enter your monthly budget: ");
        manager.setMonthlyBudget(budget);
        fileHandler.saveBudget(budget);
        System.out.printf("Monthly budget set to %.2f%n", budget);
    }

    private static void showRemainingBudget() {
        double budget = manager.getMonthlyBudget();
        if (budget <= 0) {
            System.out.println("No monthly budget set yet. Choose option 8 to set one.");
            return;
        }
        double spent = manager.getCurrentMonthTotal();
        double remaining = manager.getRemainingBudget();
        System.out.println("Month                : " + YearMonth.now());
        System.out.printf("Monthly budget       : %.2f%n", budget);
        System.out.printf("Spent this month     : %.2f%n", spent);
        if (remaining >= 0) {
            System.out.printf("Remaining budget     : %.2f%n", remaining);
        } else {
            System.out.printf("WARNING! You are over budget by %.2f%n", -remaining);
        }
    }

    // ------------------------------------------------------------
    // main method
    // ------------------------------------------------------------

    public static void main(String[] args) {
        // Load saved data when the program starts
        ArrayList<Expense> loaded = fileHandler.loadExpenses();
        double loadedBudget = fileHandler.loadBudget();
        manager = new ExpenseManager(loaded, loadedBudget);

        System.out.println("Welcome to the Personal Expense Tracker!");
        System.out.println("Loaded " + loaded.size() + " saved expense(s).");

        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Enter your choice (1-11): ", 1, 11);
            System.out.println();

            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    printExpenses(manager.getAllExpenses());
                    break;
                case 3:
                    searchByCategory();
                    break;
                case 4:
                    searchByDate();
                    break;
                case 5:
                    deleteExpense();
                    break;
                case 6:
                    System.out.printf("Total expenses: %.2f%n", manager.getTotalExpenses());
                    break;
                case 7:
                    showCategorySummary();
                    break;
                case 8:
                    setBudget();
                    break;
                case 9:
                    showRemainingBudget();
                    break;
                case 10:
                    if (fileHandler.saveExpenses(manager.getAllExpenses())) {
                        System.out.println("Expenses saved to data/expenses.txt");
                    }
                    break;
                case 11:
                    fileHandler.saveExpenses(manager.getAllExpenses());
                    System.out.println("Data saved. Thank you for using the Expense Tracker. Goodbye!");
                    running = false;
                    break;
            }
        }
        scanner.close();
    }
}
