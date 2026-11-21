package com.bank.model;

/** Cheque: for salaried customers; deposits/withdrawals of any amount; no interest. */
public class ChequeAccount extends Account {
    private final String employerName;
    private final String employerAddress;

    public ChequeAccount(String accountNumber, String branch, Customer owner, double balance,
                         String employerName, String employerAddress) {
        super(accountNumber, branch, owner, balance);
        if (employerName == null || employerName.isBlank()
                || employerAddress == null || employerAddress.isBlank()) {
            throw new BankException("A Cheque account requires the employer's name and address.");
        }
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    public String getEmployerName() { return employerName; }
    public String getEmployerAddress() { return employerAddress; }

    @Override
    public boolean allowsWithdrawals() { return true; }

    @Override
    public String getAccountType() { return "CHEQUE"; }
}
