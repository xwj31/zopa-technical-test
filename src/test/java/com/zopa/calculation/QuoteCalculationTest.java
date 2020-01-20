package com.zopa.calculation;

import com.zopa.error.LenderRetrievalError;
import com.zopa.model.LoanQuote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class QuoteCalculationTest {

    public List<LoanQuote> loanQuoteList;

    @BeforeEach
    public void init() {
        LoanQuote loanQuote1 = new LoanQuote();
        loanQuote1.setLenderRate(new BigDecimal("0.7"));
        loanQuote1.setLenderAmountAvailable(new BigDecimal(1000));

        LoanQuote loanQuote2 = new LoanQuote();
        loanQuote2.setLenderRate(new BigDecimal("0.8"));
        loanQuote2.setLenderAmountAvailable(new BigDecimal(500));

        LoanQuote loanQuote3 = new LoanQuote();
        loanQuote3.setLenderRate(new BigDecimal("0.5"));
        loanQuote3.setLenderAmountAvailable(new BigDecimal(320));

        LoanQuote loanQuote4 = new LoanQuote();
        loanQuote4.setLenderRate(new BigDecimal("0.2"));
        loanQuote4.setLenderAmountAvailable(new BigDecimal(550));

        LoanQuote loanQuote5 = new LoanQuote();
        loanQuote5.setLenderRate(new BigDecimal("0.7"));
        loanQuote5.setLenderAmountAvailable(new BigDecimal(800));

        LoanQuote loanQuote6 = new LoanQuote();
        loanQuote6.setLenderRate(new BigDecimal("0.8"));
        loanQuote6.setLenderAmountAvailable(new BigDecimal(14000));

        loanQuoteList = Arrays.asList(loanQuote1, loanQuote2, loanQuote3, loanQuote4, loanQuote5, loanQuote6);
    }

    @Test
    public void quoteCalculationThrowsWhenLoanAmountCannotBeFulfilled() {
        QuoteCalculation quoteCalculation = new QuoteCalculation(1010);
        assertThrows(LenderRetrievalError.class,
                () -> quoteCalculation.calculateMonthlyAndTotalRepayment(loanQuoteList));
    }

    @Test
    public void calculateQuoteMax() {
        QuoteCalculation quoteCalculation = new QuoteCalculation(15000);
        LoanQuote loanQuote = quoteCalculation.calculateMonthlyAndTotalRepayment(loanQuoteList);
        assertThat(loanQuote.getMonthlyRepayment(), equalTo(new BigDecimal("1550.14")));
        assertThat(loanQuote.getTotalRepayment(), equalTo(new BigDecimal("55805.04")));
        assertThat(loanQuote.getLenderRate(), equalTo(new BigDecimal("0.8")));
        assertThat(loanQuote.getLoanAmount(), equalTo(15000));
    }

    @Test
    public void calculateQuoteMin() {
        QuoteCalculation quoteCalculation = new QuoteCalculation(1000);
        LoanQuote loanQuote = quoteCalculation.calculateMonthlyAndTotalRepayment(loanQuoteList);
        assertThat(loanQuote.getMonthlyRepayment(), equalTo(new BigDecimal("103.34")));
        assertThat(loanQuote.getTotalRepayment(), equalTo(new BigDecimal("3720.24")));
        assertThat(loanQuote.getLenderRate(), equalTo(new BigDecimal("0.7")));
        assertThat(loanQuote.getLoanAmount(), equalTo(1000));
    }
}
