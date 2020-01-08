package com.zopa;

import com.zopa.error.LenderRetrievalError;
import com.zopa.model.Lender;
import com.zopa.output.CliOutputFormatter;
import com.zopa.reader.CsvReader;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@CommandLine.Command(name = "ratecalculator", description = "Calculate the best " +
        "available rate from a supplied list of lenders")

public class Application implements Runnable {

    @CommandLine.Parameters( index = "0", arity = "1..*", description = "a file location is required" )
    private String fileLocation;

    @CommandLine.Parameters( index = "1", arity = "1..*", description = "Loan amount is required" )
    private int loanAmount;

    //TODO: possible add help -h option?

    Lender chosenLender = null;

    public static void main(String... args) {
        System.exit(new CommandLine(new Application()).execute(args));
    }

    public void run() {
        //check the loan amount is a £100 increment
        //TODO: refactor this bad code
        if (loanAmount % 100 == 0) {
            //also check the amount is between 1000 and 1500
            if (loanAmount >= 1000 &&
                    loanAmount <= 15000) {
                CsvReader csvReader = new CsvReader();
                List<Lender> lenders = csvReader.processInputFile(fileLocation);

                Stream<Lender> filteredStream = lenders.stream()
                        .filter(lender -> lender.checkAvailableAmount(loanAmount)) //remove lenders that cannot loan the full amount
                        .sorted(Comparator.comparing(Lender::getRate)); // order by loan rate //TODO: check override

                chosenLender = filteredStream.findFirst().orElseThrow(LenderRetrievalError::new); //TODO: doesnt feel right
                calculateLoan(chosenLender, loanAmount);
            } else {
                System.out.println("Please enter an amount between 1000 and 15000");
            }
        } else {
            System.out.println("Loan amount must be in increments of 100");
        }
    }

    /**
     *                    principal * rate
     *       payment =  -------------------      where n = 12 * years,
     *                  1  - (1 + r)^(-n)              r = (rate / 100) / 12
     *
     * @param lender
     * @param loanAmount
     */

    //TODO: move this to big decimal
    public static void calculateLoan(Lender lender, int loanAmount) {
        int years = 3;
        double rate = lender.getRate().doubleValue();

        double r = (rate / 100) / 12;
        double n = 12 * years;

        double monthlyRepayment = (loanAmount * r) / (1 - Math.pow(1+r, -n));
        double totalRepayment = monthlyRepayment * n;

        CliOutputFormatter cliOutputFormatter =
                new CliOutputFormatter(loanAmount,
                        lender.getRate(),
                        BigDecimal.valueOf(monthlyRepayment),
                        BigDecimal.valueOf(totalRepayment));
    }
}
