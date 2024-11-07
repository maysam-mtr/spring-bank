package com.example.bankingApp.controllers;

import com.example.bankingApp.models.Transaction;
import com.example.bankingApp.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing transactions.
 */
@RestController
@RequestMapping(path = "api/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * Constructor for TransactionController.
     * @param transactionService Service for transactions.
     */
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Gets all transactions.
     * @return A list of all transactions.
     */
    @GetMapping("getAllTransactions")
    public ResponseEntity<List<Transaction>> getAllTransactions(){
        List<Transaction> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * Makes a transaction between two accounts.
     * @param senderAccountNb The account number of the sender.
     * @param receiverAccountNb The account number of the receiver.
     * @param value The value of the transaction.
     * @param message The message associated with the transaction.
     * @return The made transaction.
     */
    @PostMapping("makeTransaction/{senderAccountNb}/{receiverAccountNb}/{value}")
    public ResponseEntity<Transaction> makeTransaction(@PathVariable("senderAccountNb") Long senderAccountNb,
                                                       @PathVariable("receiverAccountNb") Long receiverAccountNb,
                                                       @PathVariable("value") double value,
                                                       @RequestBody String message){
        Transaction transaction = transactionService.makeTransaction(senderAccountNb, receiverAccountNb, value, message);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    /**
     * Gets all transactions associated with a given user ID.
     * @param userId The user ID.
     * @return A list of transactions associated with the user ID.
     */
    @GetMapping("getTransactionsByUserId/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable("userId") Long userId){
        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * Gets the latest transactions for a given user ID.
     * @param userId The user ID.
     * @return A list of the latest transactions.
     */
    @GetMapping("getLatestTransactions/{userId}")
    public ResponseEntity<List<Transaction>> getLatestTransactions(@PathVariable("userId") Long userId){
        List<Transaction> transactions = transactionService.getLatestTransactions(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * Gets the total income for a given user ID for the current month.
     * @param userId The user ID.
     * @return The total income.
     */
    @GetMapping("getTotalIncome/{userId}")
    public ResponseEntity<Double> getTotalIncome(@PathVariable("userId") Long userId){
        double totalIncome = transactionService.getTotalIncomeByUserIdForCurrentMonth(userId);
        return new ResponseEntity<>(totalIncome, HttpStatus.OK);
    }

    /**
     * Gets the total expenses for a given user ID for the current month.
     * @param userId The user ID.
     * @return The total expenses.
     */
    @GetMapping("getTotalExpenses/{userId}")
    public ResponseEntity<Double> getTotalExpenses(@PathVariable("userId") Long userId){
        double totalIncome = transactionService.getTotalExpensesByUserIdForCurrentMonth(userId);
        return new ResponseEntity<>(totalIncome, HttpStatus.OK);
    }

}