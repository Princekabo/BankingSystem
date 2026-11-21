package com.bank.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract parent of all account types (abstraction + encapsulation).
 * An account cannot exist without an owning Customer (composition).
 */
public abstract class Account {
    private final String accountNumber;
    private final String branch;
    private final Customer owner;
    private double balance;
    private final List<Transaction> transactions = new ArrayList<>();

    protected Account(String accountNumber, String branch, Customer owner, double balance) {
        if (owner == null) {
            throw new BankException("An account must be held by a customer.");
        }
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new BankException("Account number is required.");
        }
        if (branch == null || branch.isBlank()) {
            throw new BankException("Branch is required.");
        }
        if (balance < 0) {
            throw new BankException("Opening balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.owner = owner;
        this.balance = round(balance);
    }

    /** Overloaded deposit: amount only. */
    public void deposit(double amount) {
        deposit(amount, "Deposit");
    }

    /** Overloaded deposit: amount plus a description. */
    public void deposit(double amount, String description) {
        if (amount <= 0) {
            throw new BankException("Deposit amount must be greater than zero.");
        }
        balance = round(balance + amount);
        recordTransaction("DEPOSIT", amount, description);
    }

    /**
     * Withdraws money. Returns false if the withdrawal is not possible
     * (insufficient funds). Subclasses may override (e.g. SavingsAccount).
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new BankException("Withdrawal amount must be greater than zero.");
        }
        if (amount > balance) {
            return false;
        }
        balance = round(balance - amount);
        recordTransaction("WITHDRAWAL", amount, "Withdrawal");
        return true;
    }

    /**
     * Pays this month's interest if the account is interest bearing
     * (polymorphism through the InterestBearing interface).
     * @return the interest paid, or 0 if the account earns none.
     */
    public double payInterest() {
        if (this instanceof InterestBearing) {
            double interest = ((InterestBearing) this).calculateInterest();
            if (interest > 0) {
                balance = round(balance + interest);
                recordTransaction("INTEREST", interest, "Monthly interest");
            }
            return interest;
        }
        return 0;
    }

    protected void recordTransaction(String type, double amount, String description) {
        transactions.add(new Transaction(accountNumber, type, amount, description));
    }

    public abstract String getAccountType();

    public abstract boolean allowsWithdrawals();

    public String displayDetails() {
        return String.format("%s [%s] Branch: %s Balance: BWP %,.2f",
                accountNumber, getAccountType(), branch, balance);
    }

    protected static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getBranch() { return branch; }
    public Customer getOwner() { return owner; }
    public double getBalance() { return balance; }
    public List<Transaction> getTransactions() { return Collections.unmodifiableList(transactions); }

    /** Most recent transaction created in memory (to be persisted), or null. */
    public Transaction getLastTransaction() {
        return transactions.isEmpty() ? null : transactions.get(transactions.size() - 1);
    }

    @Override
    public String toString() {
        return displayDetails();
    }
}
