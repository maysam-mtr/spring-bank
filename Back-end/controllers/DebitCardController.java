package com.example.bankingApp.controllers;

import com.example.bankingApp.models.DebitCard;
import com.example.bankingApp.services.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing debit cards.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/card")
public class DebitCardController {
    private final DebitCardService debitCardService;

    /**
     * Constructor for DebitCardController.
     * @param debitCardService Service for debit cards.
     */
    @Autowired
    public DebitCardController(DebitCardService debitCardService) {
        this.debitCardService = debitCardService;
    }

    /**
     * Adds a debit card to a user's account.
     * @param userId The ID of the user.
     * @param accountNb The account number.
     * @return The added debit card.
     */
    @PostMapping("addDebitCard/{userId}/{accountNb}")
    public ResponseEntity<DebitCard> addDebitCard(@PathVariable("userId") Long userId,
                                                  @PathVariable("accountNb") Long accountNb){
        DebitCard debitCard = debitCardService.addDebitCard(userId, accountNb);
        return new ResponseEntity<>(debitCard, HttpStatus.CREATED);
    }

    /**
     * Updates the expiration date of a debit card.
     * @param userId The ID of the user.
     * @param accountNb The account number.
     * @return The updated debit card.
     */
    @PutMapping("updateExpirationDate/{userId}/{accountNb}")
    public ResponseEntity<DebitCard> updateExpirationDate(@PathVariable("userId") Long userId,
                                                          @PathVariable("accountNb") Long accountNb){
        DebitCard debitCard = debitCardService.updateExpirationDate(userId, accountNb);
        return new ResponseEntity<>(debitCard, HttpStatus.OK);
    }

    /**
     * Gets all debit cards associated with a given user ID.
     * @param userId The user ID.
     * @return A list of debit cards associated with the user ID.
     */
    @GetMapping("getCardsByUserId/{userId}")
    public ResponseEntity<List<DebitCard>> getCardsByUserId(@PathVariable("userId") Long userId){
        List<DebitCard> debitCards = debitCardService.getCardsByUserId(userId);
        return new ResponseEntity<>(debitCards, HttpStatus.OK);
    }

    /**
     * Gets all debit cards associated with a given account number.
     * @param accountNb The account number.
     * @return A list of debit cards associated with the account number.
     */
    @GetMapping("getCardsByAccountNb/{accountNb}")
    public ResponseEntity<List<DebitCard>> getCardsByAccountNb(@PathVariable("accountNb") Long accountNb){
        List<DebitCard> debitCards = debitCardService.getCardsByAccountNb(accountNb);
        return new ResponseEntity<>(debitCards, HttpStatus.OK);
    }

    /**
     * Deletes a debit card.
     * @param cardId The ID of the debit card.
     */
    @DeleteMapping("deleteCard/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable("cardId") Long cardId){
        debitCardService.deleteCard(cardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
