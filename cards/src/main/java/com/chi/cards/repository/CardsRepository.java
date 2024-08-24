package com.chi.cards.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chi.cards.entity.Cards;

@Repository
public interface CardsRepository extends JpaRepository<Cards, Long> {
    
    Optional<Cards> findByMobile(String mobile);
    Optional<Cards> findByCardNumber(String CardNumber);
}
