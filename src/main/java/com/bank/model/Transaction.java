package com.bank.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** A record of one movement of money on an account. */
public class Transaction {
    private long transactionId;
    private final String accountNumber;
    private final String transactionType; // DEPOSIT, WITHDRAWAL, INTEREST
    private final double amount;
    private final String description;
    private final LocalDateTime transactionDate;

    /** Creates a new transaction stamped with the current time. */
    public Transaction(String accountNumber, String transactionType, double amount, String description) {
        this(0, accountNumber, transactionType, amount, description, LocalDateTime.now());
    }

    /** Used when rebuilding a transaction from the database. */
    public Transaction(long transactionId, String accountNumber, String transactionType,
                       double amount, String description, LocalDateTime transactionDate) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    public long getTransactionId() { return transactionId; }
    public void setTransactionId(long transactionId) { this.transactionId = transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public String getTransactionType() { return transactionType; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDateTime getTransactionDate() { return transactionDate; }

    public String getTransactionDetails() {
        return String.format("%s | %-10s | BWP %,10.2f | %s",
                transactionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                transactionType, amount, description == null ? "" : description);
    }

    @Override
    public String toString() {
        return getTransactionDetails();
    }
}
