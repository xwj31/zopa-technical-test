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

        List<LoanQuote> loanQuoteList = findFirstCompleteQuote(completeLoanQuoteSet);
        loanQuoteList.forEach(LoanQuote::calculateLoanQuote);

        return new LoanQuote(calculateWeightedRateAverage(loanQuoteList),
                calculateMonthlyRepayment(loanQuoteList),
                calculateTotalRepayment(loanQuoteList),
                loanAmount);
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

    private BigDecimal calculateTotalRepayment(List<LoanQuote> loanQuoteList) {
        return loanQuoteList
                .stream()
                .map(LoanQuote::getTotalRepayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateMonthlyRepayment(List<LoanQuote> loanQuoteList) {
        return loanQuoteList
                .stream()
                .map(LoanQuote::getMonthlyRepayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<LoanQuote> findFirstCompleteQuote(List<List<LoanQuote>> completeLoanQuoteSet) {
        return completeLoanQuoteSet
                .stream()
                .findFirst()
                .orElseThrow(LenderRetrievalError::new);
    }

    private BigDecimal calculateWeightedRateAverage(List<LoanQuote> loanQuoteList) {
        return loanQuoteList
                .stream()
                .map(LoanQuote::getWeightedAverage)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(loanQuoteList.size()), RoundingMode.HALF_EVEN);
    }
}
