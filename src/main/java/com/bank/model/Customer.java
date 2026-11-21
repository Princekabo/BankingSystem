package com.bank.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A bank customer who can hold several accounts. */
public class Customer {
    private int customerId;
    private final String firstName;
    private final String surname;
    private final String address;
    private final String username;
    private final String passwordHash;
    private final List<Account> accounts = new ArrayList<>();

    /** Registers a new customer (the plain password is hashed immediately). */
    public Customer(String firstName, String surname, String address, String username, String password) {
        this(0, firstName, surname, address, username, hash(requireText(password, "Password", 6)));
        requireText(firstName, "First name", 1);
        requireText(surname, "Surname", 1);
        requireText(address, "Address", 1);
        requireText(username, "Username", 3);
    }

    private Customer(int id, String firstName, String surname, String address, String username, String passwordHash) {
        this.customerId = id;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    /** Rebuilds a customer from a database row. */
    public static Customer restore(int id, String firstName, String surname, String address,
                                   String username, String passwordHash) {
        return new Customer(id, firstName, surname, address, username, passwordHash);
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && passwordHash.equals(hash(password == null ? "" : password));
    }

    public void addAccount(Account account) {
        if (account.getOwner() != this) {
            throw new BankException("Account belongs to a different customer.");
        }
        accounts.add(account);
    }

    public Account findAccount(String accountNumber) {
        for (Account a : accounts) {
            if (a.getAccountNumber().equals(accountNumber)) {
                return a;
            }
        }
        return null;
    }

    public void clearAccounts() { accounts.clear(); }

    public List<Account> getAccounts() { return Collections.unmodifiableList(accounts); }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getFullName() { return firstName + " " + surname; }
    public String getAddress() { return address; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }

    private static String requireText(String value, String field, int minLength) {
        if (value == null || value.trim().length() < minLength) {
            throw new BankException(field + " is required"
                    + (minLength > 1 ? " (at least " + minLength + " characters)." : "."));
        }
        return value.trim();
    }

    static String hash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
