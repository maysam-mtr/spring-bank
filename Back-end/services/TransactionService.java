package com.example.bankingApp.services;

import com.example.bankingApp.exceptions.InsufficientFundsException;
import com.example.bankingApp.exceptions.ResourceNotFoundException;
import com.example.bankingApp.models.Account;
import com.example.bankingApp.models.Transaction;
import com.example.bankingApp.repositories.AccountHolderRepository;
import com.example.bankingApp.repositories.AccountRepository;
import com.example.bankingApp.repositories.TransactionRepository;
import com.example.bankingApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * Service class for managing transactions.
 */
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountHolderRepository accountHolderRepository;

    /**
     * Constructor for TransactionService.
     * @param transactionRepository Repository for transactions.
     * @param userRepository Repository for users.
     * @param accountRepository Repository for accounts.
     * @param accountHolderRepository Repository for account holders.
     */
    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository,
                              AccountRepository accountRepository, AccountHolderRepository accountHolderRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.accountHolderRepository = accountHolderRepository;
    }

    /**
     * Makes a transaction between two accounts.
     * @param senderAccountNb The account number of the sender.
     * @param receiverAccountNb The account number of the receiver.
     * @param value The value of the transaction.
     * @param message The message associated with the transaction.
     * @return The made transaction.
     */
    public Transaction makeTransaction(Long senderAccountNb, Long receiverAccountNb,
                                       double value, String message){

        if (!Objects.equals(senderAccountNb, receiverAccountNb)) {
            Account senderAccount = accountRepository.findById(senderAccountNb).orElseThrow(() ->
                    new ResourceNotFoundException("Account not found"));
            Account receiverAccount = accountRepository.findById(receiverAccountNb).orElseThrow(() ->
                    new ResourceNotFoundException("Account not found"));

            if (senderAccount.getBalance() < value) {
                throw new InsufficientFundsException("Insufficient funds in the sender's account");
            }

            senderAccount.setBalance(senderAccount.getBalance() - value);
            receiverAccount.setBalance(receiverAccount.getBalance() + value);

            // Save the updated accounts back to the repository
            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);

            Transaction transaction;
            if (!message.isEmpty()) {
                transaction = new Transaction(senderAccount, receiverAccount, value, message);
            } else {
                transaction = new Transaction(senderAccount, receiverAccount, value);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            transaction.setDate(LocalDateTime.now().format(formatter));
            return transactionRepository.save(transaction);
        }else { //if it was an in person withdrawal/deposit from the bank
            Account account = accountRepository.findById(senderAccountNb).orElseThrow(() ->
                    new ResourceNotFoundException("Account not found"));

            if (value > 0){
                account.setBalance(account.getBalance() + value);
            } else if (value < 0) {
                if ((account.getBalance() + value) < 0){
                    throw new InsufficientFundsException("Insufficient funds in the client's account");
                }else {
                    account.setBalance(account.getBalance() + value);
                }
            } else {
                throw new IllegalArgumentException("Transaction value must not be 0 or null");
            }
            accountRepository.save(account);

            Transaction transaction = new Transaction(account, account, value, message);
            return transactionRepository.save(transaction);
        }
    }

    /**
     * Gets all transactions associated with a given user ID.
     * @param userId The user ID.
     * @return A list of transactions associated with the user ID.
     */
    public List<Transaction> getTransactionsByUserId(Long userId){
        if (userRepository.existsById(userId)) {
            return transactionRepository.getAllUserTransactions(userId);
        }else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    /**
     * Gets all transactions.
     * @return A list of all transactions.
     */
    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    /**
     * Gets the total income for a given user ID for the current month.
     * @param userId The user ID.
     * @return The total income.
     */
    public double getTotalIncomeByUserIdForCurrentMonth(Long userId) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User not found");
        }

        List<Transaction> transactions = transactionRepository.getUserDeposits(userId);

        double totalIncome = 0;
        LocalDate now = LocalDate.now();

        for (Transaction transaction : transactions) {
            //if it is an in-person withdrawal bcz sender account would be == receiver account, ignore it
            if (transaction.getSenderAccount().equals(transaction.getReceiverAccount()) && transaction.getValue() < 0){
                continue;
            }
            // Parse the date string into a LocalDate object
            LocalDate date = LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

            // Check if the transaction was made in the current month and year
            if (date.getMonth() == now.getMonth() && date.getYear() == now.getYear()) {
                totalIncome += transaction.getValue();
            }
        }

        return totalIncome;
    }

    /**
     * Gets the total expenses for a given user ID for the current month.
     * @param userId The user ID.
     * @return The total expenses.
     */
    public double getTotalExpensesByUserIdForCurrentMonth(Long userId) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User not found");
        }

        List<Transaction> transactions = transactionRepository.getUserWithdrawals(userId);

        double totalExpenses = 0;
        LocalDate now = LocalDate.now();

        for (Transaction transaction : transactions) {
            //if it is an in-person withdrawal bcz sender account would be == receiver account, ignore it
            if (transaction.getSenderAccount().equals(transaction.getReceiverAccount()) && transaction.getValue() > 0){
                continue;
            }else if (transaction.getSenderAccount().equals(transaction.getReceiverAccount()) && transaction.getValue() < 0){
                transaction.setValue(transaction.getValue()*(-1)); //change it back to positive so the number is right
            }
            // Parse the date string into a LocalDate object
            LocalDate date = LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

            // Check if the transaction was made in the current month and year
            if (date.getMonth() == now.getMonth() && date.getYear() == now.getYear()) {
                totalExpenses += transaction.getValue();
            }
        }

        return totalExpenses;
    }

    /**
     * Gets the latest transactions for a given user ID.
     * @param userId The user ID.
     * @return A list of the latest transactions.
     */
    public List<Transaction> getLatestTransactions(Long userId){
        if (userRepository.existsById(userId)) {
            return transactionRepository.getLatestTransactions(userId);
        }else {
            throw new ResourceNotFoundException("User not found");
        }
    }

}
