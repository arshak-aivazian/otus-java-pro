package com.example.hw13.cashmachine.bank.dao;

import com.example.hw13.cashmachine.bank.data.Account;
import com.example.hw13.cashmachine.bank.db.Accounts;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDao {
    public Account getAccount(Long accountId) {
        if (!Accounts.accounts.containsKey(accountId)) {
            throw new IllegalArgumentException("Account not found");
        }
        return Accounts.accounts.get(accountId);
    }

    public Account saveAccount(Account account) {
        if (account.getId() <= 0) {
            account.setId(Accounts.getNextId());
        }
        Accounts.accounts.put(account.getId(), account);
        return account;
    }
}
