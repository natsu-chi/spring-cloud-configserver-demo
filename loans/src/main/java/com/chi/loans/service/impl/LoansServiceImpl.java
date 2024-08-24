package com.chi.loans.service.impl;

import com.chi.loans.constants.LoansConstants;
import com.chi.loans.dto.LoansDto;
import com.chi.loans.entity.Loans;
import com.chi.loans.exception.LoanAlreadyExistsException;
import com.chi.loans.exception.ResourceNotFoundException;
import com.chi.loans.mapper.LoansMapper;
import com.chi.loans.repository.LoansRepository;
import com.chi.loans.service.ILoansService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoansServiceImpl implements ILoansService {

    private LoansRepository loansRepository;

    /**
     * @param mobile - Mobile of the Customer
     */
    @Override
    public void createLoan(String mobile) {
        Optional<Loans> optionalLoans= loansRepository.findByMobile(mobile);
        if(optionalLoans.isPresent()){
           throw new LoanAlreadyExistsException("Loan already registered with given mobile " + mobile);
        }
        Loans loan = createNewLoan(mobile);
        loan.setCreateUser("a_user");
        loan.setCreateTime(LocalDateTime.now());
        loansRepository.save(loan);
    }

    /**
     * @param mobile - Mobile of the Customer
     * @return the new loan details
     */
    public Loans createNewLoan(String mobile) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobile(mobile);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }

    /**
     *
     * @param mobile - Input mobile Number
     * @return Loan Details based on a given mobileNumber
     */
    @Override
    public LoansDto fetchLoan(String mobile) {
        Loans loans = loansRepository.findByMobile(mobile).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobile", mobile)
        );
        return LoansMapper.mapToLoansDto(loans, new LoansDto());
    }

    @Override
    public boolean updateLoan(LoansDto loansDto) {
        Loans loans = loansRepository.findByMobile(loansDto.getMobile()).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobile", loansDto.getMobile())
        );
        LoansMapper.mapToLoans(loansDto, loans);
        loansRepository.save(loans);
        return true;
    }

    @Override
    public boolean deleteLoan(String mobile) {
        Loans loans = loansRepository.findByMobile(mobile).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobile)
        );
        loansRepository.deleteById(loans.getLoanId());
        return true;
    }

}
