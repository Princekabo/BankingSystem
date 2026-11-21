package com.bank.model;

/** Investment: earns 5% monthly interest; deposits and withdrawals allowed; BWP 500 to open. */
public class InvestmentAccount extends Account implements InterestBearing {
    public static final double MONTHLY_RATE = 0.05;
    public static final double MINIMUM_OPENING_DEPOSIT = 500.00;

    /** Plain constructor (also used when loading from the database). */
    public InvestmentAccount(String accountNumber, String branch, Customer owner, double balance) {
        super(accountNumber, branch, owner, balance);
    }

    /** Opens a NEW investment account, enforcing the BWP 500 initial deposit rule. */
    public static InvestmentAccount open(String accountNumber, String branch, Customer owner, double initialDeposit) {
        if (initialDeposit < MINIMUM_OPENING_DEPOSIT) {
            throw new BankException("An Investment account requires an initial deposit of at least BWP 500.00.");
        }
        InvestmentAccount account = new InvestmentAccount(accountNumber, branch, owner, 0);
        account.deposit(initialDeposit, "Opening deposit");
        return account;
    }

    @Override
    public boolean allowsWithdrawals() { return true; }

    @Override
    public double getInterestRate() { return MONTHLY_RATE; }

    @Override
    public double calculateInterest() { return round(getBalance() * MONTHLY_RATE); }

    @Override
    public String getAccountType() { return "INVESTMENT"; }
}
