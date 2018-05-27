package com.langostinko.account.account;

import java.util.HashMap;

public class AccountManager {
    private HashMap<String, Account> Accounts_ = new HashMap<>();

    private Account GetAccount(String id) throws AccountNotFoundException {
        Account account = Accounts_.get(id);
        if (null == account) {
            throw new AccountNotFoundException();
        }
        return account;
    }

    public void CreateAccount(String id) throws AccountAlreadyExistsException {
        if (Accounts_.containsKey(id)) {
            throw new AccountAlreadyExistsException();
        } else {
            Accounts_.put(id, new Account());
        }
    }

    public int GetCount(String id) throws AccountNotFoundException {
        return GetAccount(id).GetCount();
    }

    public void Add(String id, int x) throws AccountNotFoundException {
        GetAccount(id).Add(x);
    }

    public void Subscribe(String id, int x) throws AccountNotFoundException, NotEnoughFundsException {
        GetAccount(id).Subscribe(x);
    }

    public void Transfer(String from, String to, int x) throws AccountNotFoundException, NotEnoughFundsException {
        Account src = GetAccount(from);
        Account dest = GetAccount(to);
        src.Subscribe(x);
        dest.Add(x);
    }
}
