package com.chi.cards.controller;

import org.springframework.web.bind.annotation.RestController;

import com.chi.cards.dto.CardsDto;
import com.chi.cards.dto.ResponseDto;
import com.chi.cards.service.impl.CardServiceImpl;
import com.chi.cards.constants.CardsConstants;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class CardsController {

    private CardServiceImpl iCardsService;
    
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCard(@RequestParam 
                                                  @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile must be 10 digits")
                                                  String mobile) {
        iCardsService.createCard(mobile);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CardsDto> fetchCard(@RequestParam
                                                 @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile must be 10 digits")
                                                 String mobile) {
        CardsDto cardsDto = iCardsService.fetchCard(mobile);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cardsDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCardDetails(@RequestBody CardsDto cardsDto) {
                
        boolean isUpdated = iCardsService.updateCard(cardsDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCardsDetails(@RequestParam
                                                          @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile must be 10 digits")
                                                          String mobile) {
        boolean isDeleted = iCardsService.deleteCard(mobile);
        if(isDeleted) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_DELETE));
        }
    }
    
}