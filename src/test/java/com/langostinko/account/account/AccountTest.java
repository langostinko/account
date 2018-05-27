package com.langostinko.account.account;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountTest {
    @Test public void AddCount() {
        Account account = new Account();
        int x = 10;
        account.Add(x);
        assertEquals(10, account.GetCount());
    }

    @Test(expected = NotEnoughFundsException.class) public void SubscribeCountThrows() throws NotEnoughFundsException {
        Account account = new Account();
        int x = 10;
        account.Subscribe(x);
    }

    @Test public void SubscribeCount() throws NotEnoughFundsException {
        Account account = new Account();
        int x = 10;
        account.Add(x);
        account.Subscribe(x);
        assertEquals(0, account.GetCount());
    }
}
