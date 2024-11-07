package com.example.bankingApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class for managing accounts.
 */
@Entity
@Table(name = "accounts")
public class Account {
    /**
     * The account number.
     */
    @Id
    @Column(name="account_nb", nullable = false)
    @NotNull
    private Long accountNb;
    /**
     * The type of the account.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;
    /**
     * The name of the account.
     */
    @Column(name = "account_name", nullable = false)
    @NotEmpty
    private String accountName;

    /**
     * The branch of the account.
     */
    @Column(name = "account_branch", nullable = false)
    @NotEmpty
    private String branch;

    /**
     * The balance of the account.
     */
    @Column(name = "account_balance", nullable = false)
    @NotNull
    private double balance;

    /**
     * The creation date of the account.
     */
    @Column(name = "account_creation_date", nullable = false)
    private String creationDate;

    /**
     * Default constructor for Account.
     */
    public Account(){}

    /**
     * Constructor for Account.
     * @param accountNb The account number.
     * @param accountType The type of the account.
     * @param accountName The name of the account.
     * @param branch The branch of the account.
     * @param balance The balance of the account.
     */
    public Account(Long accountNb, AccountType accountType, String accountName, String branch, double balance) {
        setAccountNb(accountNb);
        setAccountName(accountName);
        setAccountType(accountType);
        setBalance(balance);
        setBranch(branch);
    }

    public Long getAccountNb() {
        return accountNb;
    }

    public void setAccountNb(Long accountNb) {
        this.accountNb = accountNb;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

}
