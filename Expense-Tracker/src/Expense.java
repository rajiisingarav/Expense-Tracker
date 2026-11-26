import java.time.LocalDate;

/**
 * Expense.java
 * Represents ONE expense record (ID, title, amount, category and date).
 * Demonstrates: encapsulation (private fields + getters) and constructors.
 */
public class Expense {

    // ----- Private fields (encapsulation) -----
    private int id;
    private String title;
    private double amount;
    private String category;
    private LocalDate date;

    /**
     * Constructor: creates a new expense.
     * The "|" character is replaced because we use it as a separator in the file.
     */
    public Expense(int id, String title, double amount, String category, LocalDate date) {
        this.id = id;
        this.title = title.trim().replace("|", "-");
        this.amount = amount;
        this.category = formatCategory(category);
        this.date = date;
    }

    /**
     * Makes categories consistent, e.g. "fOOD" -> "Food".
     * This lets "food" and "FOOD" be treated as the same category.
     */
    private static String formatCategory(String text) {
        String clean = text.trim().replace("|", "-");
        if (clean.isEmpty()) {
            return clean;
        }
        return clean.substring(0, 1).toUpperCase() + clean.substring(1).toLowerCase();
    }

    // ----- Getters -----
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    /**
     * Converts this expense into one line of text for saving in a file.
     * Format: id|title|amount|category|date
     */
    public String toFileString() {
        return id + "|" + title + "|" + amount + "|" + category + "|" + date;
    }

    /**
     * Creates an Expense from one line of the file (the opposite of toFileString).
     * Throws an exception if the line is damaged, so the caller can skip it.
     */
    public static Expense fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 5) {
            throw new IllegalArgumentException("Wrong number of fields");
        }
        int id = Integer.parseInt(parts[0].trim());
        double amount = Double.parseDouble(parts[2].trim());
        LocalDate date = LocalDate.parse(parts[4].trim());
        return new Expense(id, parts[1], amount, parts[3], date);
    }
}
