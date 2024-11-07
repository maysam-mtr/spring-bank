package com.example.bankingApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "debit_cards")
public class DebitCard {
    @Id
    @Column(name="card_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id; //card id
    @ManyToOne
    @JoinColumn(name="card_account_nb", nullable = false)
    private Account cardAccount; //account to which the card belongs to
    @ManyToOne
    @JoinColumn(name="card_user_id", nullable = false)
    private User cardOwner; //card owner
    @Column(name="card_cvv", nullable = false)
    @NotNull
    private int cardCvv; //card pin
    @Column(name="card_expiration_date", nullable = false)
    @NotEmpty
    private String expirationDate; //card expiration date

    //default constructor
    public DebitCard(){}

    /**
     * Arg contructor
     * @param cardAccount
     * @param cardOwner
     */
    public DebitCard(Account cardAccount, User cardOwner) {
        setCardAccount(cardAccount);
        setCardOwner(cardOwner);
        setCardCvv(generateCvv());

        LocalDate futureDate = LocalDate.now().plusYears(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        setExpirationDate(futureDate.format(formatter));
    }

    public Long getId() {
        return id;
    }

    public Account getCard_account() {
        return cardAccount;
    }

    public void setCardAccount(Account cardAccount) {
        this.cardAccount = cardAccount;
    }

    public User getCardOwner() {
        return cardOwner;
    }

    public void setCardOwner(User cardOwner) {
        this.cardOwner = cardOwner;
    }

    public int getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(int cardCvv) {
        this.cardCvv = cardCvv;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    private int generateCvv(){
        return ((int)(Math.random() * 900) + 100);
    }
}
