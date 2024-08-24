package com.chi.cards.service.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.chi.cards.dto.CardsDto;
import com.chi.cards.entity.Cards;
import com.chi.cards.exception.CardAlreadyExistsException;
import com.chi.cards.exception.ResourceNotFoundException;
import com.chi.cards.mapper.CardsMapper;
import com.chi.cards.repository.CardsRepository;
import com.chi.cards.service.ICardService;
import com.chi.cards.constants.CardsConstants;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class CardServiceImpl implements ICardService {

    private CardsRepository cardsRepository;

    /**
     * @param mobile - Mobile of the Customer
     */
    @Override
    public void createCard(String mobile) {
        Optional<Cards> optionalCards= cardsRepository.findByMobile(mobile);
        if(optionalCards.isPresent()){
            throw new CardAlreadyExistsException("Card already registered with given mobile "+mobile);
        }
        cardsRepository.save(createNewCard(mobile));
    }

    /**
     * @param mobile - Mobile Number of the Customer
     * @return the new card details
     */
    private Cards createNewCard(String mobile) {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobile(mobile);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard;
    }

    /**
     *
     * @param mobile - Input mobile Number
     * @return Card Details based on a given mobile
     */
    @Override
    public CardsDto fetchCard(String mobile) {
        Cards cards = cardsRepository.findByMobile(mobile).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobile", mobile)
        );
        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    /**
     *
     * @param cardsDto - CardsDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    @Override
    public boolean updateCard(CardsDto cardsDto) {
        Cards cards = cardsRepository.findByCardNumber(cardsDto.getCardNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Card", "CardNumber", cardsDto.getCardNumber()));
        CardsMapper.mapToCards(cardsDto, cards);
        cardsRepository.save(cards);
        return  true;
    }

    /**
     * @param mobile - Input Mobile
     * @return boolean indicating if the delete of card details is successful or not
     */
    @Override
    public boolean deleteCard(String mobile) {
        Cards cards = cardsRepository.findByMobile(mobile).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobile", mobile)
        );
        cardsRepository.deleteById(cards.getCardId());
        return true;
    }
    
}
