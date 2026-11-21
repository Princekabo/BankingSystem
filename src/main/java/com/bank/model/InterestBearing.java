package com.bank.model;

/** Interface implemented by every account type that earns monthly interest. */
public interface InterestBearing {
    /** Monthly interest rate as a fraction (0.05 = 5%). */
    double getInterestRate();

    /** Interest that would be paid this month on the current balance. */
    double calculateInterest();
}
