package com.example.bankingApp.services;

import com.example.bankingApp.exceptions.ResourceNotFoundException;
import com.example.bankingApp.models.Account;
import com.example.bankingApp.models.AccountHolder;
import com.example.bankingApp.models.User;
import com.example.bankingApp.repositories.AccountHolderRepository;
import com.example.bankingApp.repositories.AccountRepository;
import com.example.bankingApp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing accounts.
 */
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final AccountHolderService accountHolderService;

    /**
     * Constructor for AccountService.
     * @param accountRepository Repository for accounts.
     * @param userRepository Repository for users.
     * @param accountHolderRepository Repository for account holders.
     * @param accountHolderService Service for account holders.
     */
    @Autowired
    public AccountService(AccountRepository accountRepository, UserRepository userRepository,
                          AccountHolderRepository accountHolderRepository, AccountHolderService accountHolderService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountHolderRepository = accountHolderRepository;
        this.accountHolderService = accountHolderService;
    }

    /**
     * Gets all accounts.
     * @return A list of all accounts.
     */
    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    /**
     * Gets all accounts associated with a given user ID.
     * @param id The user ID.
     * @return A list of accounts associated with the user ID.
     */
    public List<Account> getAccountsByUserId(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<AccountHolder> accountHolders = accountHolderRepository.findAllByUser(user);
        return accountHolders.stream().map(AccountHolder::getAccount).collect(Collectors.toList());
    }


    /**
     * Gets the total balance for a given user ID.
     * @param id The user ID.
     * @return The total balance.
     */
    public double getTotalBalanceByUserId(Long id){
        List<Account> accounts = getAccountsByUserId(id);

        double totalBalance = 0;
        for (Account account : accounts) {
            totalBalance += account.getBalance();
        }

        return totalBalance;
    }


    /**
     * Gets an account by user id.
     * @param id The user ID.
     * @return The total balance.
     */
    public Account findAccountById(Long id){
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            return account.get();
        }else {
            throw new ResourceNotFoundException("Account not found");
        }
    }

    /**
     * Creates an account.
     * @param account The account to create.
     * @param userIds The user IDs to associate with the account.
     * @return The created account.
     */
    @Transactional
    public Account createAccount(Account account, List<Long> userIds){
        if (account.getBalance() < 0){
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        Long id;
        //generate random 8 digit number
        do {
            id = (long) (Math.random() * 900000000L) + 100000000L;
        } while (userRepository.existsById(id));
        account.setAccountNb(id);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        account.setCreationDate(LocalDateTime.now().format(formatter));
        Account savedAccount = accountRepository.save(account);

        // Assign the owners to the account
        for (Long userId : userIds) {
            AccountHolder accountHolder = accountHolderService.addAccountHolder(userId, savedAccount.getAccountNb());
            if (accountHolder == null) {
                throw new IllegalArgumentException("User or account not found");
            }
        }

        return savedAccount;
    }

    /**
     * Checks if an account exists by ID.
     * @param id The account ID.
     * @return True if the account exists, false otherwise.
     */
    public boolean accountExistsById(Long id){ return accountRepository.existsById(id); }

    /**
     * Deletes an account.
     * @param account The account to delete.
     */
    public void deleteAccount(Account account){
        if(accountExistsById(account.getAccountNb())) {
            accountRepository.delete(account);
        }else {
            throw new ResourceNotFoundException("Account does not exist");
        }
    }

    /**
     * Deletes an account by ID.
     * @param id The account ID.
     */
    public void deleteAccountById(Long id){
        if (accountExistsById(id)) {
            accountRepository.deleteById(id);
        }else {
            throw new ResourceNotFoundException("Account does not exist");
        }
    }

}
