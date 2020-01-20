# Zopa Technical Test

There is a need for a rate calculation system allowing prospective borrowers to
obtain a quote from our pool of lenders for 36 month loans. This system will
take the form of a command-line application.

### Assumptions:
1. BigDecimal is used to calculate APR to 7 figures due to use of MathContext.DECIMAL32 but could be increased.

2. The input csv is of reasonable size and does not need to be read line by line.

3. All of each available loan amount must be used in the quote.

4. Multiple lenders can make up a loan quote, resulting in the Change-making problem.

5. 0% APR is possible

6. The first line of the input CSV will always contain a header.

7. The rate quoted in the CSV will always be in decimal form.

8. Rounding is done in accordance with MCOB 10.3.1 R 


```cmd> [application] [market_file] [loan_amount]
   Example:
   cmd> rate-calculator market.csv 1500

   The application should produce output in the form:
   cmd> [application] [market_file] [loan_amount]
   Requested amount: £XXXX
   Rate: X.X%
   Monthly repayment: £XXXX.XX
   Total repayment: £XXXX.XX
   
   Example:
   cmd> rate-calculator market.csv 1000
   Requested amount: £1000
   Rate: 7.0%
   Monthly repayment: £30.78
   Total repayment: £1108.10

```