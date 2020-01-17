package com.zopa.error;

public class LoanIncrementError extends RuntimeException {

    public LoanIncrementError() {
        super("Loan amount must be in increments of 100");
    }
}
