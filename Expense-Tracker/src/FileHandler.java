import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * FileHandler.java
 * Takes care of saving and loading data using plain text files.
 * Files are stored inside the "data" folder.
 */
public class FileHandler {

    private static final String DATA_FOLDER = "data";
    private static final String EXPENSE_FILE = DATA_FOLDER + "/expenses.txt";
    private static final String BUDGET_FILE = DATA_FOLDER + "/budget.txt";

    /** Creates the data folder if it does not exist yet. */
    private void createFolderIfMissing() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * Saves all expenses to the file (one expense per line).
     * Returns true if saving worked, false if there was an error.
     */
    public boolean saveExpenses(ArrayList<Expense> expenses) {
        createFolderIfMissing();
        // try-with-resources closes the file automatically
        try (PrintWriter writer = new PrintWriter(new FileWriter(EXPENSE_FILE))) {
            for (Expense expense : expenses) {
                writer.println(expense.toFileString());
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error: Could not save expenses (" + e.getMessage() + ")");
            return false;
        }
    }

    /**
     * Loads expenses from the file. Returns an empty list if the file
     * does not exist. Damaged lines are skipped with a warning.
     */
    public ArrayList<Expense> loadExpenses() {
        ArrayList<Expense> expenses = new ArrayList<>();
        File file = new File(EXPENSE_FILE);
        if (!file.exists()) {
            return expenses;   // first run: nothing saved yet
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;   // ignore blank lines
                }
                try {
                    expenses.add(Expense.fromFileString(line));
                } catch (RuntimeException e) {
                    System.out.println("Warning: Skipped damaged line " + lineNumber + " in " + EXPENSE_FILE);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Could not read expenses (" + e.getMessage() + ")");
        }
        return expenses;
    }

    /** Saves the monthly budget to its own small file. */
    public void saveBudget(double budget) {
        createFolderIfMissing();
        try (PrintWriter writer = new PrintWriter(new FileWriter(BUDGET_FILE))) {
            writer.println(budget);
        } catch (IOException e) {
            System.out.println("Error: Could not save budget (" + e.getMessage() + ")");
        }
    }

    /** Loads the monthly budget. Returns 0 if no budget was saved. */
    public double loadBudget() {
        File file = new File(BUDGET_FILE);
        if (!file.exists()) {
            return 0;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                return Double.parseDouble(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Warning: Could not read the saved budget. Using 0.");
        }
        return 0;
    }
}
