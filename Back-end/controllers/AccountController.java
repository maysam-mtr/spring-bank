package com.example.bankingApp.controllers;

import com.example.bankingApp.AccountCreationRequest;
import com.example.bankingApp.models.Account;
import com.example.bankingApp.models.AccountType;
import com.example.bankingApp.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller class for managing accounts.
 */
@RestController
@RequestMapping(path = "api/account")
public class AccountController {

    private final AccountService accountService;

    /**
     * Constructor for AccountController.
     * @param accountService Service for accounts.
     */
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Gets all accounts.
     * @return A list of all accounts.
     */
    @GetMapping("getAllAccounts")
    public ResponseEntity<List<Account>> getAllUsers(){
        List<Account> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    /**
     * Gets an account by its number.
     * @param accountNb The account number.
     * @return The account.
     */
    @GetMapping("getAccountByNb/{accountNb}")
    public ResponseEntity<Account> getAccountById(@PathVariable("accountNb") Long accountNb){
        Account account = accountService.findAccountById(accountNb);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    /**
     * Gets the total balance for a given user ID.
     * @param userId The user ID.
     * @return The total balance.
     */
    @GetMapping("getUserTotalBalance/{id}")
    public ResponseEntity<Double> getTotalBalance(@PathVariable("id") Long userId){
        double totalBalance = accountService.getTotalBalanceByUserId(userId);
        return new ResponseEntity<>(totalBalance, HttpStatus.OK);
    }

    /**
     * Gets all accounts associated with a given user ID.
     * @param userId The user ID.
     * @return A list of accounts associated with the user ID.
     */
    @GetMapping("getAccountsByUser/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable("userId") Long userId){
        return new ResponseEntity<>(accountService.getAccountsByUserId(userId), HttpStatus.OK);
    }

    /**
     * Creates an account.
     * @param req The request body containing the account and user IDs.
     * @return The created account.
     */
    @PostMapping("createAccount")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountCreationRequest req){
        Account savedAccount = accountService.createAccount(req.getAccount(), req.getUserIds());
        return new ResponseEntity<Account>(savedAccount, HttpStatus.CREATED);
    }

    /**
     * Deletes an account by its ID.
     * @param id The account ID.
     */
    @DeleteMapping("deleteAccount/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id){
        accountService.deleteAccountById(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
