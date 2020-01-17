package com.zopa;

import com.zopa.error.LoanAmountRangeError;
import com.zopa.error.LoanIncrementError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ApplicationTest {

    @Test
    public void loanAmountOutsideOfRangeThrowsLoanAmountRangeError() {
        Application application = new Application();
        application.setLoanAmount(90000);
        assertThrows(LoanAmountRangeError.class, application::run);
    }

    @Test
    public void wrongIncrementAmountThrowsLoanIncrementError() {
        Application application = new Application();
        application.setLoanAmount(999);
        assertThrows(LoanIncrementError.class, application::run);
    }
}