package com.example.bankingApp.repositories;

import com.example.bankingApp.models.Account;
import com.example.bankingApp.models.Transaction;
import com.example.bankingApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for transaction class
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * find all transactions by sender account
     * @param account
     * @return list of transactions
     */
    List<Transaction> findAllBySenderAccount(Account account);

    /**
     * find all transactions by receiver account
     * @param account
     * @return list of transactions
     */
    List<Transaction> findAllByReceiverAccount(Account account);

    /**
     * get deposits of a user
     * @param userId
     * @return list of transactions
     */
    @Query(value = "SELECT * FROM transactions WHERE receiver_account_nb IN " +
            "(SELECT account_nb FROM account_holders WHERE user_id = ?1)",
            nativeQuery = true)
    List<Transaction> getUserDeposits(Long userId);

    /**
     * get user withdrawals
     * @param userId
     * @return list of transactions
     */
    @Query(value = "SELECT * FROM transactions WHERE sender_account_nb IN " +
            "(SELECT account_nb FROM account_holders WHERE user_id = ?1)",
            nativeQuery = true)
    List<Transaction> getUserWithdrawals(Long userId);

    /**
     * get latest transactions
     * @param userId
     * @return
     */
    @Query(value = "SELECT * FROM transactions WHERE sender_account_nb IN " +
            "(SELECT account_nb FROM account_holders WHERE user_id = ?1) OR receiver_account_nb IN " +
            "(SELECT account_nb FROM account_holders WHERE user_id = ?1) ORDER BY transaction_date DESC LIMIT 4",
            nativeQuery = true)
    List<Transaction> getLatestTransactions(Long userId);

    /**
     * get all tarnsactions for user
     * @param userId
     * @return list of transactions
     */
    @Query(value = "SELECT * FROM transactions WHERE sender_account_nb IN " +
            "(SELECT account_nb FROM account_holders WHERE user_id = ?1) OR receiver_account_nb IN " +
            "(SELECT account_nb FROM account_holders WHERE user_id = ?1)",
            nativeQuery = true)
    List<Transaction> getAllUserTransactions(Long userId);
}
