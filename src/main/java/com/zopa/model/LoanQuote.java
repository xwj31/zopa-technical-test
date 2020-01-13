package com.zopa.model;

import java.math.BigDecimal;

public class LoanQuote implements Comparable<LoanQuote> {

    private String lenderName;
    private BigDecimal rate;
    private int amountAvailable;

    private double monthlyRepayment;
    private double totalRepayment;

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public int getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(int amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public double getMonthlyRepayment() {
        return monthlyRepayment;
    }

    public void setMonthlyRepayment(double monthlyRepayment) {
        this.monthlyRepayment = monthlyRepayment;
    }

    public double getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(double totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    //replace with lambda
    //https://howtodoinjava.com/sort/collections-sort/
    @Override
    public int compareTo(LoanQuote loanQuote) {
        return Integer.compare(this.amountAvailable, loanQuote.amountAvailable);
    }

    /**
     *                    principal * rate
     *       payment =  -------------------      where n = 12 * years,
     *                  1  - (1 + r)^(-n)              r = (rate / 100) / 12
     *
     */
    public void calculateQuote() {

        int years = 3; //36 months loan term //TODO: move this to a properties class or constant

        double rate = getRate().doubleValue();
        double r = (rate / 100) / 12;
        double n = 12 * years;

        double monthlyRepayment = (amountAvailable * r) / ( 1 - Math.pow(1 + r, -n));
        double totalRepayment = monthlyRepayment * n;

        setMonthlyRepayment(monthlyRepayment);
        setTotalRepayment(totalRepayment);
    }

    @Override
    public String toString() {
        return "LoanQuote{" +
                "lenderName='" + lenderName + '\'' +
                ", rate=" + rate +
                ", amountAvailable=" + amountAvailable +
                ", monthlyRepayment=" + monthlyRepayment +
                ", totalRepayment=" + totalRepayment +
                '}';
    }
}
