import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * ExpenseManager.java
 * Holds the list of expenses (ArrayList) and the monthly budget.
 * Contains the logic: add, search, delete, totals and budget calculations.
 * It does not print anything - Main.java is responsible for showing results.
 */
public class ExpenseManager {

    private ArrayList<Expense> expenses;
    private double monthlyBudget;   // 0 means "no budget set"
    private int nextId;             // ID given to the next new expense

    /** Constructor: starts with the expenses and budget loaded from the file. */
    public ExpenseManager(ArrayList<Expense> loadedExpenses, double loadedBudget) {
        this.expenses = loadedExpenses;
        this.monthlyBudget = loadedBudget;

        // Next ID = (highest existing ID) + 1, so IDs never repeat
        int highest = 0;
        for (Expense e : expenses) {
            if (e.getId() > highest) {
                highest = e.getId();
            }
        }
        this.nextId = highest + 1;
    }

    /** Adds a new expense and returns it. */
    public Expense addExpense(String title, double amount, String category, LocalDate date) {
        Expense expense = new Expense(nextId, title, amount, category, date);
        nextId++;
        expenses.add(expense);
        return expense;
    }

    /** Returns the full list of expenses (used for viewing and saving). */
    public ArrayList<Expense> getAllExpenses() {
        return expenses;
    }

    /** Finds all expenses of a category (not case-sensitive). */
    public ArrayList<Expense> searchByCategory(String category) {
        ArrayList<Expense> result = new ArrayList<>();
        for (Expense e : expenses) {
            if (e.getCategory().equalsIgnoreCase(category.trim())) {
                result.add(e);
            }
        }
        return result;
    }

    /** Finds all expenses made on an exact date. */
    public ArrayList<Expense> searchByDate(LocalDate date) {
        ArrayList<Expense> result = new ArrayList<>();
        for (Expense e : expenses) {
            if (e.getDate().equals(date)) {
                result.add(e);
            }
        }
        return result;
    }

    /** Deletes the expense with the given ID. Returns the deleted expense, or null if not found. */
    public Expense deleteExpense(int id) {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getId() == id) {
                return expenses.remove(i);
            }
        }
        return null;
    }

    /** Calculates the total of all expenses. */
    public double getTotalExpenses() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        return total;
    }

    /**
     * Category-wise summary: category name -> total amount.
     * A TreeMap keeps the categories in alphabetical order.
     */
    public Map<String, Double> getCategorySummary() {
        Map<String, Double> summary = new TreeMap<>();
        for (Expense e : expenses) {
            double current = summary.getOrDefault(e.getCategory(), 0.0);
            summary.put(e.getCategory(), current + e.getAmount());
        }
        return summary;
    }

    // ----- Budget methods -----

    public void setMonthlyBudget(double budget) {
        this.monthlyBudget = budget;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    /** Total spent during the current month (the budget is a monthly budget). */
    public double getCurrentMonthTotal() {
        YearMonth thisMonth = YearMonth.now();
        double total = 0;
        for (Expense e : expenses) {
            if (YearMonth.from(e.getDate()).equals(thisMonth)) {
                total += e.getAmount();
            }
        }
        return total;
    }

    /** Remaining budget for this month (can be negative if over budget). */
    public double getRemainingBudget() {
        return monthlyBudget - getCurrentMonthTotal();
    }
}
