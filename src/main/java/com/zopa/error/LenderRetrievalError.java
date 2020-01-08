package com.zopa.error;

public class LenderRetrievalError extends RuntimeException {
    public LenderRetrievalError() {
        super("It is not possible to provide a quote at this time");
    }
}
