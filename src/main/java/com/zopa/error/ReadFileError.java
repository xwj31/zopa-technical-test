package com.zopa.error;

public class ReadFileError extends RuntimeException {

    public ReadFileError() {
        super("Error reading file");
    }
}
