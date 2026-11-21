package com.bank.model;

/** Savings: earns 0.05% monthly interest; withdrawals are NOT allowed. */
public class SavingsAccount extends Account implements InterestBearing {
    public static final double MONTHLY_RATE = 0.0005;

    public SavingsAccount(String accountNumber, String branch, Customer owner, double balance) {
        super(accountNumber, branch, owner, balance);
    }

    @Override
    public boolean withdraw(double amount) { // overriding
        return false;
    }

    @Override
    public boolean allowsWithdrawals() { return false; }

    @Override
    public double getInterestRate() { return MONTHLY_RATE; }

    @Override
    public double calculateInterest() { return round(getBalance() * MONTHLY_RATE); }

    @Override
    public String getAccountType() { return "SAVINGS"; }
}
