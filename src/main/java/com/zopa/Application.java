package com.zopa;

import com.zopa.calculation.QuoteCalculation;
import com.zopa.model.LoanQuote;
import com.zopa.reader.CsvReader;
import picocli.CommandLine;

import java.util.*;

@CommandLine.Command(name = "rate-calculator", description = "Calculate the best " +
        "available rate from a supplied list of lenders")

public class Application implements Runnable {

    @CommandLine.Parameters( index = "0", arity = "1..*", description = "a file location is required" )
    private String fileLocation;

    @CommandLine.Parameters( index = "1", arity = "1..*", description = "Loan amount is required" )
    private int loanAmount;

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
                //TODO: optimise this
                CsvReader csvReader = new CsvReader();
                List<LoanQuote> loanQuotes = csvReader.processInputFile(fileLocation);

                QuoteCalculation quoteCalculation = new QuoteCalculation(loanAmount);
                quoteCalculation.calculateMonthlyAndTotalRepaymentAndPrint(loanQuotes);

            } else {
                System.out.println("Please enter an amount between 1000 and 15000");
            }
        } else {
            System.out.println("Loan amount must be in increments of 100");
        }
    }
}
