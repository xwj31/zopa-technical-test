package com.zopa.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class LoanQuoteTest {

    @Test
    public void calculateZeroInterest() {
        LoanQuote testQuote = new LoanQuote();
        testQuote.setLenderName("test");
        testQuote.setLenderAmountAvailable(new BigDecimal(500));
        testQuote.setLenderRate(new BigDecimal("0.0"));
        testQuote.calculateLoanQuote();

        assertThat(testQuote.getTotalRepayment(), equalTo(new BigDecimal("500")));
        assertThat(testQuote.getMonthlyRepayment(), equalTo(new BigDecimal("13.89")));
    }

    @Test
    public void calculateLoanQuoteWithNegativeAvailable() {
        LoanQuote testQuote = new LoanQuote();
        testQuote.setLenderName("test");
        testQuote.setLenderAmountAvailable(new BigDecimal(-1000));
        testQuote.setLenderRate(new BigDecimal("0.0"));
        testQuote.calculateLoanQuote();

        assertThat(testQuote.getTotalRepayment(), equalTo(new BigDecimal("1000").negate()));
        assertThat(testQuote.getMonthlyRepayment(), equalTo(new BigDecimal("27.78").negate()));
    }

    @Test
    public void calculateLoanQuote() {
        LoanQuote testQuote = new LoanQuote();
        testQuote.setLenderName("test");
        testQuote.setLenderAmountAvailable(new BigDecimal(500));
        testQuote.setLenderRate(new BigDecimal("0.051"));
        testQuote.calculateLoanQuote();

        assertThat(testQuote.getTotalRepayment(), equalTo(new BigDecimal("537.84")));
        assertThat(testQuote.getMonthlyRepayment(), equalTo(new BigDecimal("14.94")));
    }
}
