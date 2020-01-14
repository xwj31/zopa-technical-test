package com.zopa.calculation;

import com.zopa.error.LenderRetrievalError;
import com.zopa.model.LoanQuote;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QuoteCalculation {

    private int loanAmount;
    private List<List<LoanQuote>> completeLoanQuoteSet;

    public QuoteCalculation(int loanAmount) {
        this.loanAmount = loanAmount;
        this.completeLoanQuoteSet = new ArrayList<>();
    }

    public void calculateMonthlyAndTotalRepaymentAndPrint(List<LoanQuote> loanQuotes) {

        //sort the input list so the lowest rates are first
        loanQuotes.sort(Comparator.comparing(LoanQuote::getLenderRate));

        //recursively find a set of lenders which results in the loanQuote amount requested
        findRequestedTotalLoanByLeastLenders(loanQuotes, new ArrayList<>());

        List<LoanQuote> loanQuoteList = completeLoanQuoteSet
                .stream()
                .findFirst()
                .orElseThrow(LenderRetrievalError::new);

        loanQuoteList.forEach(LoanQuote::calculateLoanQuote);

        BigDecimal monthlyRepayment = loanQuoteList
                .stream()
                .map(LoanQuote::getMonthlyRepayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRepayment = loanQuoteList
                .stream()
                .map(LoanQuote::getTotalRepayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sum = loanQuoteList
                .stream()
                .map(LoanQuote::getLenderRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //blended rate
        BigDecimal rate = sum.divide(new BigDecimal(loanQuoteList.size()), RoundingMode.HALF_UP);

        //TODO: null checks for returned values before printing

        /**
         * APR calculation: rounding
         * MCOB 10.3.4R31/10/2004
         * RP
         * Where the APR, as calculated in accordance with MCOB 10.3.1 R, has more than one decimal place it must be rounded to one decimal place as follows:
         *
         * (1) where the figure at the second decimal place is greater than or equal to five, the figure at the first decimal place must be increased by one and the decimal place (or places) following the first decimal place must be disregarded; and
         * (2) where the figure at the second decimal place is less than five, that decimal place and any decimal places following it must be disregarded.
         *
         */
        System.out.println("Requested amount: " +loanAmount);
        System.out.println("Rate " + rate.setScale(1, RoundingMode.HALF_EVEN)); //1dp
        System.out.println("Monthly repayment: £" + monthlyRepayment.setScale(2, RoundingMode.HALF_UP)); //2dp
        System.out.println("Total repayment: £" + totalRepayment.setScale(2, RoundingMode.HALF_UP)); //2dp
    }

    private void findRequestedTotalLoanByLeastLenders(List<LoanQuote> loanQuoteList, List<LoanQuote> workingList) {
        for (int i = 0; i < loanQuoteList.size(); i++) {
            List<LoanQuote> list = new ArrayList<>(workingList);
            list.add(loanQuoteList.get(i));

            //recursive call
            findRequestedTotalLoanByLeastLenders(loanQuoteList.subList(i + 1, loanQuoteList.size()), list);
        }

        int cumulativeAmount = 0;
        for (LoanQuote loanQuote : workingList) {
            cumulativeAmount += loanQuote.getLenderAmountAvailable();
        }
        if (cumulativeAmount == loanAmount) {
            completeLoanQuoteSet.add(workingList);
        }
    }
}
