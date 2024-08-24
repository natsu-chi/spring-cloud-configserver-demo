package com.chi.loans.service;

import com.chi.loans.dto.LoansDto;

public interface ILoansService {

    /**
     *
     * @param mobile - Mobile of the Customer
     */
    void createLoan(String mobile);

    /**
     *
     * @param mobile - Input mobile Number
     * @return Loan Details based on a given mobileNumber
     */
    LoansDto fetchLoan(String mobile);

    /**
     *
     * @param loansDto - LoansDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    boolean updateLoan(LoansDto loansDto);

    /**
     *
     * @param mobile - Input Mobile Number
     * @return boolean indicating if the delete of loan details is successful or not
     */
    boolean deleteLoan(String mobile);

}