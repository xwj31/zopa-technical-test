package com.zopa.error;

public class LoanAmountRangeError extends RuntimeException {
    public LoanAmountRangeError() {
        super("Please enter an amount between 1000 and 15000");
    }
}
