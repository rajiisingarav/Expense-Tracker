# 💰 Expense-Tracker

A beginner-friendly, console-based **Personal Expense Tracker** built with **Core Java**. Record your daily expenses, search them, see where your money goes, set a monthly budget, and keep everything saved in a simple text file.

---

## 📖 Project Description

This project helps you keep track of personal spending from the command line. Expenses are kept in an `ArrayList` while the program runs and are saved to a plain text file (`data/expenses.txt`), so your records are still there the next time you start the program. No database, framework or external library is needed — just Core Java.

It is a good practice project for learning **OOP**, **ArrayList**, **file handling** and **input validation**.

---

## ✨ Features

- ➕ Add a new expense (title, amount, category, date)
- 📋 View all expenses in a neat table
- 🔍 Search expenses by **category** (not case-sensitive)
- 📅 Search expenses by **date**
- 🗑️ Delete an expense by its ID
- 🧮 Calculate total expenses
- 📊 Category-wise summary with percentage share
- 🎯 Set a **monthly budget**
- 💵 Display **remaining budget** for the current month (with an over-budget warning)
- 💾 Save expenses to a file (automatic after every add/delete, and on exit)
- 📂 Load saved expenses (and budget) automatically when the program starts
- ✅ Input validation — wrong input never crashes the program

---

## 🛠️ Technologies Used

| Technology | Purpose |
|------------|---------|
| Java (JDK 11 or higher) | Programming language |
| OOP (classes, objects, encapsulation) | Program design |
| `ArrayList` | Storing expenses in memory |
| File handling (`FileReader`, `FileWriter`, `BufferedReader`, `PrintWriter`) | Saving and loading data |
| `java.time.LocalDate` | Working with dates |
| `Scanner` | Reading console input |

---

## 📁 Project Folder Structure

```
Expense-Tracker/
├── src/
│   ├── Main.java              # Main menu and user interaction
│   ├── Expense.java           # One expense (ID, title, amount, category, date)
│   ├── ExpenseManager.java    # Add, search, delete and calculate expenses
│   └── FileHandler.java       # Save and load data from files
├── data/                      # Saved data goes here (expenses.txt, budget.txt)
│   └── .gitkeep
├── README.md
└── .gitignore
```

---

## 🚀 How to Run the Project in VS Code

### Step 1 – Install the requirements
1. Install the **JDK 11 or higher** (for example from [adoptium.net](https://adoptium.net)).
2. Install **Visual Studio Code**.
3. In VS Code, open the **Extensions** tab (`Ctrl+Shift+X`), search for **"Extension Pack for Java"** (by Microsoft) and install it.

### Step 2 – Open the project
1. In VS Code choose **File → Open Folder…**
2. Select the **`Expense-Tracker`** folder (the one that contains `src` and `data`).

> ⚠️ Open the **`Expense-Tracker` folder itself**, not the `src` folder. The program saves its data in `data/` relative to the folder you opened.

### Step 3 – Run
1. Open `src/Main.java`.
2. Click the **▶ Run** button at the top right of the editor (or the small **Run** link above `public static void main`).
3. The menu appears in the **Terminal** panel at the bottom. Click inside the terminal and type your choices.

### Alternative: run from a terminal
Open a terminal inside the `Expense-Tracker` folder and run:

```bash
mkdir out
javac -d out src/*.java
java -cp out Main
```
(On Windows Command Prompt use `javac -d out src\*.java`.)

---

## 📝 How to Use

- **Dates** use the format `yyyy-MM-dd` (example: `2026-03-15`). Press **Enter** to use today's date when adding an expense.
- **Categories** are not case-sensitive: `food`, `FOOD` and `Food` are the same category.
- The **monthly budget** is compared with what you spent in the **current month**.
- Your data is saved in `data/expenses.txt` and `data/budget.txt`.

---

## 🖥️ Sample Output

```
Welcome to the Personal Expense Tracker!
Loaded 0 saved expense(s).

========== PERSONAL EXPENSE TRACKER ==========
 1. Add New Expense
 2. View All Expenses
 3. Search Expenses by Category
 4. Search Expenses by Date
 5. Delete an Expense
 6. Show Total Expenses
 7. Show Category-wise Summary
 8. Set Monthly Budget
 9. Show Remaining Budget
10. Save Expenses to File
11. Exit
==============================================
Enter your choice (1-11): 1

Enter expense title: Lunch
Enter amount: abc
Invalid input! Please enter a number (example: 250.50).
Enter amount: 250.50
Enter category (e.g. Food, Travel): food
Enter date (yyyy-MM-dd) or press Enter for today: 2026-02-30
Invalid date! Use the format yyyy-MM-dd (example: 2026-03-15).
Enter date (yyyy-MM-dd) or press Enter for today: 2026-11-20
Expense added successfully! (ID: 1)

Enter your choice (1-11): 2

ID   Title                        Amount  Category       Date
---------------------------------------------------------------------
1    Lunch                        250.50  Food           2026-11-20
2    Bus ticket                    45.00  Travel         2026-11-26
3    Movie - night                600.00  Entertainment  2026-11-20
4    Groceries                   1200.75  Food           2026-10-15
5    Notebook                      80.00  Stationery     2026-11-26
---------------------------------------------------------------------
Number of expenses: 5

Enter your choice (1-11): 3

Enter category to search: food
ID   Title                        Amount  Category       Date
---------------------------------------------------------------------
1    Lunch                        250.50  Food           2026-11-20
4    Groceries                   1200.75  Food           2026-10-15
---------------------------------------------------------------------
Number of expenses: 2
Total for this category: 1451.25

Enter your choice (1-11): 7

Category                Total    Share
----------------------------------------
Entertainment          600.00    27.6%
Food                  1451.25    66.7%
Stationery              80.00     3.7%
Travel                  45.00     2.1%
----------------------------------------
TOTAL                 2176.25

Enter your choice (1-11): 8

Enter your monthly budget: 3000
Monthly budget set to 3000.00

Enter your choice (1-11): 9

Month                : 2026-11
Monthly budget       : 3000.00
Spent this month     : 975.50
Remaining budget     : 2024.50

Enter your choice (1-11): 5

Enter the ID of the expense to delete: 5
Deleted: Notebook (80.00)

Enter your choice (1-11): 11

Data saved. Thank you for using the Expense Tracker. Goodbye!
```

When the budget is exceeded, option 9 shows a warning such as:

```
WARNING! You are over budget by 2895.50
```

---

## 🔮 Future Improvements

- Edit an existing expense
- Search by date range or month
- Export a monthly report
- Build a GUI with Java Swing

---

## 📄 License

Free to use for learning purposes.

## 👤 Author

**Your Name** – [GitHub Profile](https://github.com/<your-username>)
