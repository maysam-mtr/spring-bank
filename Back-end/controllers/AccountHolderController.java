package com.example.bankingApp.controllers;

import com.example.bankingApp.models.Account;
import com.example.bankingApp.models.AccountHolder;
import com.example.bankingApp.models.User;
import com.example.bankingApp.services.AccountHolderService;
import com.example.bankingApp.services.AccountService;
import com.example.bankingApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing account holders.
 */
@RestController
@RequestMapping(path = "api/accountholder")
public class AccountHolderController {
    private final AccountHolderService accountHolderService;
    private final AccountService accountService;
    private final UserService userService;

    /**
     * Constructor for AccountHolderController.
     * @param accountHolderService Service for account holders.
     * @param accountService Service for accounts.
     * @param userService Service for users.
     */
    @Autowired
    public AccountHolderController(AccountHolderService accountHolderService, AccountService accountService,
                                    UserService userService) {
        this.accountHolderService = accountHolderService;
        this.accountService = accountService;
        this.userService = userService;
    }

    /**
     * Gets all users associated with a given account number.
     * @param accountNb The account number.
     * @return A list of users associated with the account number.
     */
    @GetMapping("getUsersByAccount/{accountNb}")
    public ResponseEntity<List<User>> getUsersByAccountId(@PathVariable("accountNb") Long accountNb){
        Account account = accountService.findAccountById(accountNb);
        List<User> users = accountHolderService.findUsersByAccount(account);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Gets all accounts associated with a given user ID.
     * @param userId The user ID.
     * @return A list of accounts associated with the user ID.
     */
    @GetMapping("getAccountsByUserId/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable("userId") Long userId){
        User user = userService.findUserById(userId);
        List<Account> accounts = accountHolderService.findAccountsByUser(user);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    /**
     * Adds an account holder.
     * @param userId The ID of the user.
     * @param accountNb The account number.
     * @return The added account holder.
     */
    @PostMapping("addAccountHolder/{userId}/{accountNb}")
    public ResponseEntity<AccountHolder> addAccountHolder(@PathVariable("userId") Long userId,
                                                          @PathVariable("accountNb") Long accountNb){
        AccountHolder accountHolder = accountHolderService.addAccountHolder(userId, accountNb);
        if (accountHolder == null){
            return new ResponseEntity<>(accountHolder, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accountHolder, HttpStatus.CREATED);
    }

}
