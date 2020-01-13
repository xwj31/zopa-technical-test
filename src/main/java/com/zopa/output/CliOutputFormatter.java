package com.zopa.output;

import java.math.BigDecimal;

public class CliOutputFormatter {

    int amount;
    BigDecimal rate;
    BigDecimal monthlyRepayment;
    BigDecimal totalRepayment;

    //TODO: fix anchoring of class with constructor
    public CliOutputFormatter(int amount,
                              BigDecimal rate,
                              BigDecimal monthlyRepayment,
                              BigDecimal totalRepayment) {

        this.amount = amount;
        this.rate = rate;
        this.monthlyRepayment = monthlyRepayment;
        this.totalRepayment = totalRepayment;
        printQuote();
    }

    public void printQuote() {
        System.out.println("Requested amount: " + amount);
        System.out.println("Rate: " + rate); //1dp
        System.out.println("Monthly Repayment: £" + monthlyRepayment); //2dp
        System.out.println("Total repayment: £" + totalRepayment); //2dp
    }
}
