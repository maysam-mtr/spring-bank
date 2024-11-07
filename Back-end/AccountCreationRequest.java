package com.example.bankingApp;

import com.example.bankingApp.models.Account;

import java.util.List;

public class AccountCreationRequest {
    private Account account;
    private List<Long> userIds;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
