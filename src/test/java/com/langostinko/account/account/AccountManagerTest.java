package com.langostinko.account.account;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountManagerTest {
    @Test
    public void CreateAccount() throws AccountAlreadyExistsException, AccountNotFoundException {
        AccountManager manager = new AccountManager();
        String id = "some_id";
        manager.CreateAccount(id);
        assertEquals(0, manager.GetCount(id));
    }
    @Test(expected = AccountAlreadyExistsException.class)
    public void CreateAccountAlreadyExists() throws AccountAlreadyExistsException, AccountNotFoundException {
        AccountManager manager = new AccountManager();
        String id = "some_id";
        manager.CreateAccount(id);
        manager.CreateAccount(id);
    }

    @Test
    public void Add() throws AccountAlreadyExistsException, AccountNotFoundException {
        AccountManager manager = new AccountManager();
        String id = "some_id";
        manager.CreateAccount(id);
        int x = 42;
        manager.Add(id, x);
        assertEquals(x, manager.GetCount(id));
    }

    @Test(expected = AccountNotFoundException.class)
    public void AddNotFound() throws AccountAlreadyExistsException, AccountNotFoundException {
        AccountManager manager = new AccountManager();
        String id = "some_id";
        manager.CreateAccount(id);
        int x = 42;
        manager.Add(id + "other", x);
    }

    @Test
    public void Subscribe() throws AccountAlreadyExistsException, AccountNotFoundException, NotEnoughFundsException {
        AccountManager manager = new AccountManager();
        String id = "some_id";
        manager.CreateAccount(id);
        int addX = 42;
        int subX = 23;
        manager.Add(id, addX);
        manager.Subscribe(id, subX);
        assertEquals(addX - subX, manager.GetCount(id));
    }

    @Test(expected = NotEnoughFundsException.class)
    public void SubscribeNotEnough() throws AccountAlreadyExistsException, AccountNotFoundException, NotEnoughFundsException {
        AccountManager manager = new AccountManager();
        String id = "some_id";
        manager.CreateAccount(id);
        int addX = 42;
        int subX = 64;
        manager.Add(id, addX);
        manager.Subscribe(id, subX);
    }

    @Test
    public void Transfer() throws AccountNotFoundException, AccountAlreadyExistsException, NotEnoughFundsException {
        AccountManager manager = new AccountManager();
        String from = "from_id";
        String to = "to_id";
        manager.CreateAccount(from);
        manager.CreateAccount(to);
        manager.Add(from, 100);
        manager.Add(to, 13);
        manager.Transfer(from, to, 50);
        assertEquals(50, manager.GetCount(from));
        assertEquals(63, manager.GetCount(to));
    }
}
