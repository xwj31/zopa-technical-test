package com.zopa.model;

import java.math.BigDecimal;

public class Lender implements Comparable<Lender> {

    String name;
    BigDecimal rate;
    int available;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public boolean checkAvailableAmount (int loanAmount) {
        return available >= loanAmount;
    }

    //replace with lambda
    //https://howtodoinjava.com/sort/collections-sort/
    @Override
    public int compareTo(Lender lender) {
        return Integer.compare(this.available, lender.available);
    }
}
