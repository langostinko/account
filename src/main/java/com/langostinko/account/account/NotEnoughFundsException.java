package com.langostinko.account.account;

class NotEnoughFundsException extends Exception {
    NotEnoughFundsException(int need, int have) {
        super("Need " + need + ", but only " + have + " on account");
    }
}
