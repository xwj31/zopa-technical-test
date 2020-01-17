package com.zopa.calculation;

import com.zopa.error.LenderRetrievalError;
import com.zopa.model.LoanQuote;

import java.math.BigDecimal;
import java.math.BigInteger;
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

    public LoanQuote calculateMonthlyAndTotalRepayment(List<LoanQuote> loanQuotes) {

        //sort the input list so the lowest rates are first
        loanQuotes.sort(Comparator.comparing(LoanQuote::getLenderRate));

        //recursively find a set of lenders which results in the loanAmount requested
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
        return new LoanQuote(rate, monthlyRepayment, totalRepayment, loanAmount);
    }

    private void findRequestedTotalLoanByLeastLenders(List<LoanQuote> loanQuoteList, List<LoanQuote> workingList) {
        for (int i = 0; i < loanQuoteList.size(); i++) {
            List<LoanQuote> list = new ArrayList<>(workingList);
            list.add(loanQuoteList.get(i));

            //recursive call
            findRequestedTotalLoanByLeastLenders(loanQuoteList.subList(i + 1, loanQuoteList.size()), list);
        }

        BigDecimal cumulativeAmount = new BigDecimal(BigInteger.ZERO);
        for (LoanQuote loanQuote : workingList) {
            cumulativeAmount = cumulativeAmount.add(loanQuote.getLenderAmountAvailable());
        }
        if (cumulativeAmount.equals(new BigDecimal(loanAmount))) {
            completeLoanQuoteSet.add(workingList);
        }
    }
}
