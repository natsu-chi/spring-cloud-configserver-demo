package com.chi.accounts.service.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.chi.accounts.constants.AccountsConstants;
import com.chi.accounts.dto.AccountsDto;
import com.chi.accounts.dto.CustomerDto;
import com.chi.accounts.entity.Accounts;
import com.chi.accounts.entity.Customer;
import com.chi.accounts.mapper.AccountsMapper;
import com.chi.accounts.mapper.CustomerMapper;
import com.chi.accounts.repository.AccountsRepository;
import com.chi.accounts.repository.CustomerRepository;
import com.chi.accounts.service.IAccountsService;
import com.chi.accounts.exceptions.CustomerAlreadyExistException;
import com.chi.accounts.exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

// 繼承介面寫法，在這邊撰寫邏輯

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {
    
    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    
    /**
     * @param customerDto - CustomerDto object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobile(customerDto.getMobile());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistException("Customer already registered with given mobile " + customerDto.getMobile());
        }
        // customer.setCreateTime(LocalDateTime.now());
        // customer.setCreateUser("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    public Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccountNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccountNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }

    /**
     * @param mobile - Input Mobile
     * @return Accounts Details based on a given mobile
     */
    @Override
    public CustomerDto fetchAccount(String mobile) {

        Customer customer = customerRepository.findByMobile(mobile).orElseThrow(
            () -> new ResourceNotFoundException("Customer", "mobile", mobile)
        );

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
            () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {

        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto != null) {
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = customerDto.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "CustomerId", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);

            isUpdated = true;
        }
        return isUpdated;

    }

    @Override
    public boolean deleteAccount(String mobile) {
        
        Customer customer = customerRepository.findByMobile(mobile).orElseThrow(
            () -> new ResourceNotFoundException("Customer", "mobile", mobile)
        );

        if(customer != null) {
            accountsRepository.deleteByCustomerId(customer.getCustomerId());
            customerRepository.deleteById(customer.getCustomerId());
            return true;
        }

        return false;

    }
    
}
