package com.example.bankingApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Table(name = "account_holders")
@Entity
public class AccountHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="account_nb", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    /**
     * Default constructor
     */
    public AccountHolder(){
    }

    /**
     *
     * @param account
     * @param user
     */
    public AccountHolder(Account account, User user){
        setAccount(account);
        setUser(user);
    }
    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
