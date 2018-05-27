package com.langostinko.account.account;

public class Account {
    private int Count_ = 0;

    public int GetCount() {
        return Count_;
    }

    public void Add(int x) {
        this.Count_ += x;
    }

    public void Subscribe(int x) throws NotEnoughFundsException {
        if (this.Count_ >= x) {
            this.Count_ -= x;
        } else {
            throw new NotEnoughFundsException(x, this.Count_);
        }
    }
}
