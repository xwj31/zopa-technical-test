package com.zopa.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class LoanQuote implements Comparable<LoanQuote> {

    private static final int LOAN_LENGTH_IN_MONTHS = 36;
    private static final int NUMBER_OF_MONTHS_IN_YEAR = 12;
    private static final int SCALE = 2; //2dp

    private String lenderName;
    private BigDecimal lenderRate;
    private BigDecimal lenderAmountAvailable;
    private int loanAmount;

    private BigDecimal monthlyRepayment;
    private BigDecimal totalRepayment;

    public LoanQuote(BigDecimal lenderRate,
                     BigDecimal monthlyRepayment,
                     BigDecimal totalRepayment,
                     int loanAmount) {

        this.lenderRate = lenderRate;
        this.monthlyRepayment = monthlyRepayment;
        this.totalRepayment = totalRepayment;
        this.loanAmount = loanAmount;
    }

    public LoanQuote() {}

    //not removed as we need to identify who the lender was!
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

    public BigDecimal getLenderAmountAvailable() {
        return lenderAmountAvailable;
    }

    public void setLenderAmountAvailable(BigDecimal lenderAmountAvailable) {
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

    public int getLoanAmount() {
        return loanAmount;
    }

    @Override
    public int compareTo(LoanQuote loanQuote) {
        return this.lenderAmountAvailable.compareTo(loanQuote.lenderAmountAvailable);
    }

    /**
     *                              principal * rate
     *  payment =  ----------------------------------------------------     where numberOfMonths = 12 * years,
     *              1  - (1 + monthlyInterestRate )^(-numberOfMonths)       monthlyInterestRate = rate / 12
     *
     *
     *       APR calculation: rounding
     *       MCOB 10.3.4R31/10/2004
     *       RP
     *       Where the APR, as calculated in accordance with MCOB 10.3.1 R,
     *       has more than one decimal place it must be rounded to one decimal place as follows:
     *
     *       (1) where the figure at the second decimal place is greater than or equal to five,
     *        the figure at the first decimal place must be increased by one and the decimal place
     *        (or places) following the first decimal place must be disregarded; and
     *
     *       (2) where the figure at the second decimal place is less than five, that decimal place
     *        and any decimal places following it must be disregarded.
     *
     *
     */
    public void calculateLoanQuote() {

        BigDecimal rate = getLenderRate();

        if (rate.compareTo(BigDecimal.ZERO) == 0) {
            setMonthlyRepayment(lenderAmountAvailable
                    .divide(new BigDecimal(LOAN_LENGTH_IN_MONTHS), SCALE, RoundingMode.HALF_EVEN));

            setTotalRepayment(lenderAmountAvailable);

        } else {
            BigDecimal monthlyInterestRate =
                    rate.divide(new BigDecimal(NUMBER_OF_MONTHS_IN_YEAR), RoundingMode.HALF_EVEN); //assuming rate is given in decimal form

            BigDecimal monthlyRepayment = (monthlyInterestRate.multiply(lenderAmountAvailable))
                    .divide((BigDecimal.ONE.subtract((monthlyInterestRate.add(BigDecimal.ONE)).pow(
                            - LOAN_LENGTH_IN_MONTHS, MathContext.DECIMAL32))), SCALE, RoundingMode.HALF_EVEN);

            BigDecimal totalRepayment = monthlyRepayment.multiply(new BigDecimal(LOAN_LENGTH_IN_MONTHS));

            setMonthlyRepayment(monthlyRepayment);
            setTotalRepayment(totalRepayment);
        }
    }

    public BigDecimal getWeightedAverage(){
        return lenderAmountAvailable.multiply(lenderRate);
    }

    public void printCompletedQuote() {
            System.out.println("Requested amount: " +loanAmount);
            System.out.println("Rate " + lenderRate.setScale(1, RoundingMode.HALF_EVEN)); //1dp
            System.out.println("Monthly repayment: £" + monthlyRepayment.setScale(2, RoundingMode.HALF_EVEN)); //2dp
            System.out.println("Total repayment: £" + totalRepayment.setScale(2, RoundingMode.HALF_EVEN)); //2dp
    }
}
