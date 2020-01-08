package com.zopa.reader;

import com.zopa.error.ReadFileError;
import com.zopa.model.Lender;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvReader {

    //loading the whole csv into a list, but could load line per line, if the csv is large
    public List<Lender> processInputFile(String fileLocation) {
        List<Lender> inputList = new ArrayList<Lender>();
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

    private Function<String, Lender> mapToItem = (line) -> {
        String[] lenderRecord = line.split(",");

        Lender lender = new Lender();
        lender.setName(lenderRecord[0]);
        lender.setRate(new BigDecimal(lenderRecord[1]));
        lender.setAvailable(Integer.parseInt(lenderRecord[2]));
        return lender;
    };
}
