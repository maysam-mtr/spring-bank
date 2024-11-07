package com.example.bankingApp.services;

import com.example.bankingApp.models.Account;
import com.example.bankingApp.models.AccountHolder;
import com.example.bankingApp.models.User;
import com.example.bankingApp.repositories.AccountHolderRepository;
import com.example.bankingApp.repositories.AccountRepository;
import com.example.bankingApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Service class for managing account holders.
 */
@Service
public class AccountHolderService {
    private final AccountHolderRepository accountHolderRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    /**
     * Constructor for AccountHolderService.
     * @param accountHolderRepository Repository for account holders.
     * @param userRepository Repository for users.
     * @param accountRepository Repository for accounts.
     */
    @Autowired
    public AccountHolderService(AccountHolderRepository accountHolderRepository, UserRepository userRepository,
                                AccountRepository accountRepository) {
        this.accountHolderRepository = accountHolderRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Finds all users associated with a given account.
     * @param account The account to find users for.
     * @return A list of users associated with the account.
     */
    public List<User> findUsersByAccount(Account account){
        List<AccountHolder> accountHolders = accountHolderRepository.findAllByAccount(account);
        return accountHolders.stream().map(AccountHolder::getUser).collect(Collectors.toList());
    }

    /**
     * Finds all accounts associated with a given user.
     * @param user The user to find accounts for.
     * @return A list of accounts associated with the user.
     */
    public List<Account> findAccountsByUser(User user){
        List<AccountHolder> accountHolders = accountHolderRepository.findAllByUser(user);
        return accountHolders.stream().map(AccountHolder::getAccount).collect(Collectors.toList());
    }

    /**
     * Adds an account holder.
     * @param userId The ID of the user.
     * @param accountNb The account number.
     * @return The added account holder.
     */
    public AccountHolder addAccountHolder(Long userId, Long accountNb){
        User user = userRepository.findById(userId).orElse(null);
        Account account = accountRepository.findById(accountNb).orElse(null);
        if (user == null || account == null) {
            return null;
        }
        AccountHolder accountHolder = new AccountHolder(account, user);
        return accountHolderRepository.save(accountHolder);

    }
}
