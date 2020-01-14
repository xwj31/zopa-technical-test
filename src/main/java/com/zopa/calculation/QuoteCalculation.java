package com.zopa.calculation;

import com.zopa.error.LenderRetrievalError;
import com.zopa.model.LoanQuote;

import java.math.BigDecimal;
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
        loanQuotes.sort(Comparator.comparing(LoanQuote::getRate));

        //recursively find a set of lenders which results in the loanQuote amount requested
        findRequestedTotalLoanByLeastLenders(loanQuotes, new ArrayList<>());

        List<LoanQuote> loanQuoteList = completeLoanQuoteSet
                .stream()
                .findFirst()
                .orElseThrow(LenderRetrievalError::new);

        loanQuoteList.forEach(LoanQuote::calculateQuote);

        double monthlyRepayment = loanQuoteList
                .stream()
                .mapToDouble(LoanQuote::getMonthlyRepayment)
                .sum();

        double totalRepayment = loanQuoteList
                .stream()
                .mapToDouble(LoanQuote::getTotalRepayment)
                .sum();

        double sum = 0.0;
        for (LoanQuote loanQuote : loanQuoteList) {
            double loanQuoteRate = loanQuote.getRate().doubleValue();
            sum += loanQuoteRate;
        }

        //blended rate
        //TODO: implement half round up
        BigDecimal rate = BigDecimal.valueOf(sum / loanQuoteList.size());

        //TODO: null checks for returned values before printing
        System.out.println("Requested amount: " +loanAmount);
        System.out.println("Rate " + rate);
        System.out.println("Monthly repayment: £" + monthlyRepayment);
        System.out.println("Total repayment: £" + totalRepayment);
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
            cumulativeAmount += loanQuote.getAmountAvailable();
        }
        if (cumulativeAmount == loanAmount) {
            completeLoanQuoteSet.add(workingList);
        }
    }
}
