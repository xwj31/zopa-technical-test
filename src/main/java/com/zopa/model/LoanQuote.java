package com.zopa.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class LoanQuote implements Comparable<LoanQuote> {

    private static final int LOAN_LENGTH_IN_MONTHS = 36;

    private String lenderName;
    private BigDecimal lenderRate;
    private int lenderAmountAvailable;

    private BigDecimal monthlyRepayment;
    private BigDecimal totalRepayment;

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    public BigDecimal getLenderRate() {
        return lenderRate;
    }

    public void setLenderRate(BigDecimal lenderRate) {
        this.lenderRate = lenderRate;
    }

    public int getLenderAmountAvailable() {
        return lenderAmountAvailable;
    }

    public void setLenderAmountAvailable(int lenderAmountAvailable) {
        this.lenderAmountAvailable = lenderAmountAvailable;
    }

    public BigDecimal getMonthlyRepayment() {
        return monthlyRepayment;
    }

    public void setMonthlyRepayment(BigDecimal monthlyRepayment) {
        this.monthlyRepayment = monthlyRepayment;
    }

    public BigDecimal getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(BigDecimal totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    //replace with lambda
    //https://howtodoinjava.com/sort/collections-sort/
    @Override
    public int compareTo(LoanQuote loanQuote) {
        return Integer.compare(this.lenderAmountAvailable, loanQuote.lenderAmountAvailable);
    }

    /**
     *                              principal * rate
     *  payment =  ----------------------------------------------------     where numberOfMonths = 12 * years,
     *              1  - (1 + monthlyInterestRate )^(-numberOfMonths)       monthlyInterestRate = rate / 12
     *
     */

    //TODO: check 0% edge case
    public void calculateLoanQuote() {

        int years = LOAN_LENGTH_IN_MONTHS / 12; //36 months loan term

        BigDecimal rate = getLenderRate();

        BigDecimal monthlyInterestRate = rate.divide(new BigDecimal(12), RoundingMode.HALF_UP); //assuming rate is given in decimal form
        int numberOfMonths = 12 * years;

        BigDecimal monthlyRepayment = (monthlyInterestRate.multiply(new BigDecimal(lenderAmountAvailable)))
                .divide((BigDecimal.ONE.subtract((monthlyInterestRate.add(BigDecimal.ONE)).pow(
                        -numberOfMonths, MathContext.DECIMAL32))), RoundingMode.HALF_UP);

        BigDecimal totalRepayment = monthlyRepayment.multiply(new BigDecimal(numberOfMonths));

        setMonthlyRepayment(monthlyRepayment);
        setTotalRepayment(totalRepayment);
    }

    @Override
    public String toString() {
        return "LoanQuote{" +
                "lenderName='" + lenderName + '\'' +
                ", rate=" + lenderRate +
                ", amountAvailable=" + lenderAmountAvailable +
                ", monthlyRepayment=" + monthlyRepayment +
                ", totalRepayment=" + totalRepayment +
                '}';
    }
}
