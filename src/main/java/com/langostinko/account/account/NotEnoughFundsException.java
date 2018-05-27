package com.langostinko.account.account;

public class NotEnoughFundsException extends Exception {
    public NotEnoughFundsException(int need, int have) {
        super("Need " + need + ", but only " + have + " on account");
    }
}
