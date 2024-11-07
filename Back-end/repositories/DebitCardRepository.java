package com.example.bankingApp.repositories;

import com.example.bankingApp.models.Account;
import com.example.bankingApp.models.DebitCard;
import com.example.bankingApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for debit card class
 */
@Repository
public interface DebitCardRepository extends JpaRepository<DebitCard, Long> {

    /**
     * find all cards by user
     * @param user
     * @return list of cards
     */
    List<DebitCard> findAllByCardOwner(User user);

    /**
     * find all cards by account
     * @param account
     * @return list of cards
     */
    List<DebitCard> findAllByCardAccount(Account account);

    /**
     * find card by account and owner
     * @param account
     * @param user
     * @return debit card object
     */
    DebitCard findByCardAccountAndCardOwner(Account account, User user);

}
