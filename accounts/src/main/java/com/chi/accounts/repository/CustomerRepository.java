package com.chi.accounts.repository;

import com.chi.accounts.entity.Customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByMobile(String mobile);
    
}
