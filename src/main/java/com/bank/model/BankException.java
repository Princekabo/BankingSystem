package com.bank.model;

/** Unchecked exception for business-rule violations (shown to the user by the GUI). */
public class BankException extends RuntimeException {
    public BankException(String message) {
        super(message);
    }
}
