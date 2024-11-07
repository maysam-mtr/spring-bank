package com.example.bankingApp.services;

import com.example.bankingApp.exceptions.ResourceNotFoundException;
import com.example.bankingApp.models.Account;
import com.example.bankingApp.models.AccountHolder;
import com.example.bankingApp.models.DebitCard;
import com.example.bankingApp.models.User;
import com.example.bankingApp.repositories.AccountHolderRepository;
import com.example.bankingApp.repositories.AccountRepository;
import com.example.bankingApp.repositories.DebitCardRepository;
import com.example.bankingApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service class for managing debit cards.
 */
@Service
public class DebitCardService {
    private final DebitCardRepository debitCardRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountHolderRepository accountHolderRepository;

    /**
     * Constructor for DebitCardService.
     * @param debitCardRepository Repository for debit cards.
     * @param userRepository Repository for users.
     * @param accountRepository Repository for accounts.
     * @param accountHolderRepository Repository for account holders.
     */
    @Autowired
    public DebitCardService(DebitCardRepository debitCardRepository, UserRepository userRepository,
                            AccountRepository accountRepository, AccountHolderRepository accountHolderRepository) {
        this.debitCardRepository = debitCardRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.accountHolderRepository = accountHolderRepository;
    }

    /**
     * Adds a debit card to a user's account.
     * @param userId The ID of the user.
     * @param accountNb The account number.
     * @return The added debit card.
     */
    public DebitCard addDebitCard(Long userId, Long accountNb){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Account account = accountRepository.findById(accountNb).orElseThrow(() ->
                new ResourceNotFoundException("Account not found"));

        // Check if the user is an owner of the account
        AccountHolder accountHolder = accountHolderRepository.findByAccountAndUser(account, user);
        if (accountHolder == null) {
            throw new IllegalArgumentException("User is not an owner of the account");
        }

        // Check if a debit card already exists for this user and account
        DebitCard existingCard = debitCardRepository.findByCardAccountAndCardOwner(account, user);
        if (existingCard != null) {
            throw new IllegalArgumentException("A debit card already exists for this user and account");
        }

        DebitCard debitCard = new DebitCard(account, user);
        return debitCardRepository.save(debitCard);
    }

    /**
     * Updates the expiration date of a debit card.
     * @param userId The ID of the user.
     * @param accountNb The account number.
     * @return The updated debit card.
     */
    public DebitCard updateExpirationDate(Long userId, Long accountNb){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Account account = accountRepository.findById(accountNb).orElseThrow(() ->
                new ResourceNotFoundException("Account not found"));

        DebitCard debitCard = debitCardRepository.findByCardAccountAndCardOwner(account, user);
        if (debitCard == null) {
            throw new ResourceNotFoundException("Card does not exist.");
        }

        // Parse the current expiration date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate currentExpirationDate = LocalDate.parse("01/" + debitCard.getExpirationDate(), formatter);

        // Check if the current expiration date is still valid
        if (currentExpirationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("The card's expiration date is still valid");
        }

        // Update the expiration date
        LocalDate futureDate = LocalDate.now().plusYears(5);
        String newExpirationDate = futureDate.format(formatter);

        debitCard.setExpirationDate(newExpirationDate);
        return debitCardRepository.save(debitCard);
    }

    /**
     * Gets all debit cards associated with a given user ID.
     * @param userId The user ID.
     * @return A list of debit cards associated with the user ID.
     */
    public List<DebitCard> getCardsByUserId(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return debitCardRepository.findAllByCardOwner(user);
    }

    /**
     * Gets all debit cards associated with a given account number.
     * @param accountNb The account number.
     * @return A list of debit cards associated with the account number.
     */
    public List<DebitCard> getCardsByAccountNb(Long accountNb){
        Account account = accountRepository.findById(accountNb).orElseThrow(() ->
                new ResourceNotFoundException("Account not found"));
        return debitCardRepository.findAllByCardAccount(account);
    }

    /**
     * Deletes a debit card.
     * @param cardId The ID of the debit card.
     */
    public void deleteCard(Long cardId){
        DebitCard debitCard = debitCardRepository.findById(cardId).orElseThrow(() ->
                new ResourceNotFoundException("Debit card not found"));
        debitCardRepository.delete(debitCard);
    }
}
