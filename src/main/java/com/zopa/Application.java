package com.zopa;

import com.zopa.error.LenderRetrievalError;
import com.zopa.model.LoanQuote;
import com.zopa.reader.CsvReader;
import picocli.CommandLine;

import java.util.*;

@CommandLine.Command(name = "ratecalculator", description = "Calculate the best " +
        "available rate from a supplied list of lenders")

public class Application implements Runnable {

    @CommandLine.Parameters( index = "0", arity = "1..*", description = "a file location is required" )
    private String fileLocation;

    @CommandLine.Parameters( index = "1", arity = "1..*", description = "Loan amount is required" )
    private int loanAmount;

    public List<List<LoanQuote>> completeLoanQuoteSet = new ArrayList<>();

    public static void main(String... args) {
        System.exit(new CommandLine(new Application()).execute(args));
    }

    public void run() {
        //check the loan amount is a 100 increment
        //TODO: refactor this
        if (loanAmount % 100 == 0) {
            //also check the amount is between 1000 and 1500
            if (loanAmount >= 1000 &&
                    loanAmount <= 15000) {
                CsvReader csvReader = new CsvReader();
                List<LoanQuote> loanQuotes = csvReader.processInputFile(fileLocation);

                //sort the input list so the lowest rate is processed first
                loanQuotes.sort(Comparator.comparing(LoanQuote::getRate));

                loanQuotes.stream().findFirst().orElseThrow(LenderRetrievalError::new);
                minChange(loanQuotes, new ArrayList<>());

                // find the lowest rate for each loan set
                for (List<LoanQuote> loanQuoteSet: completeLoanQuoteSet) {
                    loanQuoteSet.forEach(LoanQuote::calculateQuote);

                }

            } else {
                System.out.println("Please enter an amount between 1000 and 15000");
            }
        } else {
            System.out.println("Loan amount must be in increments of 100");
        }

        System.out.println("Requested amount: " +loanAmount);
        //System.out.println("Rate " + rateSum);
        //System.out.println("Monthly repayment: £" + monthlyRepayment);
        //System.out.println("Total repayment: £" + totalRepayment);

    }

    public void minChange(List<LoanQuote> loanQuoteList, List<LoanQuote> workingList) {
        for (int i = 0; i < loanQuoteList.size(); i++) {
            List<LoanQuote> list = new ArrayList<>(workingList);
            list.add(loanQuoteList.get(i));

            //recursive call
            minChange(loanQuoteList.subList(i + 1, loanQuoteList.size()), list);
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
