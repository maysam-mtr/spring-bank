package com.example.bankingApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @Column(name="transaction_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id; //transaction id
    @ManyToOne
    @JoinColumn(name="sender_account_nb", nullable = false)
    private Account senderAccount; //sender account number
    @ManyToOne
    @JoinColumn(name="receiver_account_nb", nullable = false)
    private Account receiverAccount; //receiver account number
    @Column(name="transaction_value", nullable = false)
    @NotNull
    @Positive
    @Min(1)
    private double value; //transaction value
    @Column(name="transaction_date", nullable = false)
    @NotEmpty
    private String date; //date of transaction
    @Column(name="transaction_message", nullable = false)
    private String message; //transaction message

    //default constructor
    public Transaction(){}

    /**
     * Arg constructor
     * @param senderAccount
     * @param receiverAccount
     * @param value
     */
    public Transaction(Account senderAccount, Account receiverAccount, double value) {
        this(senderAccount, receiverAccount, value, "No message");
    }

    /**
     * Arg constructor
     * @param senderAccount
     * @param receiverAccount
     * @param value
     * @param message
     */
    public Transaction(Account senderAccount, Account receiverAccount, double value, String message) {
        setSenderAccount(senderAccount);
        setReceiverAccount(receiverAccount);
        setValue(value);
        setDate(String.valueOf(LocalDateTime.now()));
        setMessage(message);
    }

    public Long getId() {
        return id;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(Account senderAccount) {
        this.senderAccount = senderAccount;
    }

    public Account getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(Account receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
