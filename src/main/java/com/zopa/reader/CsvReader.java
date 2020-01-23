package com.zopa.reader;

import com.zopa.error.LenderRetrievalError;
import com.zopa.error.ReadFileError;
import com.zopa.model.LoanQuote;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvReader {

    //loading the whole csv into a list, but could load line per line, if the csv is large
    public List<LoanQuote> processInputFile(String fileLocation) {
        Optional<List<LoanQuote>> inputList;
        File file = new File(fileLocation);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
           inputList = Optional.of(bufferedReader.lines()
                   .skip(1) // skip the header of the csv
                   .map(mapToItem)
                   .collect(Collectors.toList()));
        } catch (IOException e) {
            throw new ReadFileError();
        }
        return inputList.orElseThrow(LenderRetrievalError::new);
    }

    private Function<String, LoanQuote> mapToItem = (line) -> {
        String[] lenderRecord = line.split(",");

        LoanQuote loanQuote = new LoanQuote();
        loanQuote.setLenderName(lenderRecord[0]);
        loanQuote.setLenderRate(new BigDecimal(lenderRecord[1]));
        loanQuote.setLenderAmountAvailable(new BigDecimal(lenderRecord[2]));
        return loanQuote;
    };
}
