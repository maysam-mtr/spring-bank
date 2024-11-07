package com.example.bankingApp.repositories;

import com.example.bankingApp.models.Account;
import com.example.bankingApp.models.AccountHolder;
import com.example.bankingApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for account holder class
 */
@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {

    /**
     * find all account holder objects by user
     * @param user
     * @return list of account holders
     */
    List<AccountHolder> findAllByUser(User user);

    /**
     * find all account holder objects by account
     * @param account
     * @return list of account holders
     */
    List<AccountHolder> findAllByAccount(Account account);

    /**
     * find account holder object by user and account
     * @param account
     * @param user
     * @return account holder object
     */
    AccountHolder findByAccountAndUser(Account account, User user);

}
