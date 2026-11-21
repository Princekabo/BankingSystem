package com.bank;

import com.bank.model.Account;
import com.bank.model.BankException;
import com.bank.model.ChequeAccount;
import com.bank.model.Customer;
import com.bank.model.InterestBearing;
import com.bank.model.InvestmentAccount;
import com.bank.model.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("Thato", "Molefe", "Gaborone", "thato", "Password1");
    }

    @Test
    void savingsRejectsWithdrawals() {
        SavingsAccount s = new SavingsAccount("AC1", "Gaborone", customer, 1000);
        assertFalse(s.allowsWithdrawals());
        assertFalse(s.withdraw(100));
        assertEquals(1000.00, s.getBalance(), 0.001);
    }

    @Test
    void savingsPaysPointZeroFivePercent() {
        SavingsAccount s = new SavingsAccount("AC1", "Gaborone", customer, 10000);
        assertEquals(5.00, s.payInterest(), 0.001);
        assertEquals(10005.00, s.getBalance(), 0.001);
    }

    @Test
    void investmentPaysFivePercent() {
        InvestmentAccount i = InvestmentAccount.open("AC2", "Gaborone", customer, 1000);
        assertEquals(50.00, i.payInterest(), 0.001);
        assertEquals(1050.00, i.getBalance(), 0.001);
    }

    @Test
    void investmentNeedsFiveHundredToOpen() {
        assertThrows(BankException.class, () -> InvestmentAccount.open("AC2", "Gaborone", customer, 499.99));
        assertEquals(500.00, InvestmentAccount.open("AC3", "Gaborone", customer, 500).getBalance(), 0.001);
    }

    @Test
    void investmentAllowsWithdrawalsButNotOverdraft() {
        InvestmentAccount i = InvestmentAccount.open("AC2", "Gaborone", customer, 600);
        assertTrue(i.withdraw(100));
        assertEquals(500.00, i.getBalance(), 0.001);
        assertFalse(i.withdraw(1000));
    }

    @Test
    void chequeNeedsEmployerDetailsAndEarnsNoInterest() {
        assertThrows(BankException.class, () -> new ChequeAccount("AC4", "Gaborone", customer, 0, "", ""));
        ChequeAccount c = new ChequeAccount("AC4", "Gaborone", customer, 100, "BPC", "Gaborone");
        assertEquals(0.0, c.payInterest(), 0.001);
        assertTrue(c.withdraw(50));
    }

    @Test
    void accountCannotExistWithoutCustomer() {
        assertThrows(BankException.class, () -> new SavingsAccount("AC5", "Gaborone", null, 0));
    }

    @Test
    void overloadedDepositRecordsTransactions() {
        SavingsAccount s = new SavingsAccount("AC1", "Gaborone", customer, 0);
        s.deposit(100);
        s.deposit(50, "Salary top-up");
        assertEquals(150.00, s.getBalance(), 0.001);
        assertEquals(2, s.getTransactions().size());
        assertThrows(BankException.class, () -> s.deposit(0));
    }

    @Test
    void customerHoldsMultipleAccountsAndPolymorphism() {
        Account[] all = {
            new SavingsAccount("AC1", "G", customer, 1000),
            InvestmentAccount.open("AC2", "G", customer, 1000),
            new ChequeAccount("AC3", "G", customer, 1000, "BPC", "Gaborone")
        };
        for (Account a : all) {
            customer.addAccount(a);
        }
        assertEquals(3, customer.getAccounts().size());
        double totalInterest = 0;
        for (Account a : customer.getAccounts()) {
            if (a instanceof InterestBearing) {
                totalInterest += ((InterestBearing) a).calculateInterest();
            }
        }
        assertEquals(50.5, totalInterest, 0.001); // 0.50 savings + 50.00 investment
    }

    @Test
    void passwordsAreChecked() {
        assertTrue(customer.login("thato", "Password1"));
        assertFalse(customer.login("thato", "wrong"));
    }
}
