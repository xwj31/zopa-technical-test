package com.zopa.reader;

import com.zopa.error.ReadFileError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CsvReaderTest {

    @Test
    public void CsvReaderThrowsReadFileError() {
        CsvReader csvReader = new CsvReader();
        assertThrows(ReadFileError.class, () -> csvReader.processInputFile("notValid/data.csv"));
    }
}
