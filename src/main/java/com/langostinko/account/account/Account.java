package com.langostinko.account.account;

class Account {
    private int Count_ = 0;

    int GetCount() {
        return Count_;
    }

    void Add(int x) {
        this.Count_ += x;
    }

    void Subscribe(int x) throws NotEnoughFundsException {
        if (this.Count_ >= x) {
            this.Count_ -= x;
        } else {
            throw new NotEnoughFundsException(x, this.Count_);
        }
    }
}
