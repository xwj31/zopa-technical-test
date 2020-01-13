package com.zopa.reader;

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
        List<LoanQuote> inputList = new ArrayList<LoanQuote>();
        try{
            File inputFile = new File(fileLocation);
            InputStream inputFileStream = new FileInputStream(inputFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputFileStream));

            inputList = bufferedReader.lines()
                    .skip(1) // skip the header of the csv
                    .map(mapToItem)
                    .collect(Collectors.toList());
            bufferedReader.close();
        } catch (IOException e) {
            throw new ReadFileError();
        }
        return inputList;
    }

    private Function<String, LoanQuote> mapToItem = (line) -> {
        String[] lenderRecord = line.split(",");

        LoanQuote loanQuote = new LoanQuote();
        loanQuote.setLenderName(lenderRecord[0]);
        loanQuote.setRate(new BigDecimal(lenderRecord[1]));
        loanQuote.setAmountAvailable(Integer.parseInt(lenderRecord[2]));
        return loanQuote;
    };
}
