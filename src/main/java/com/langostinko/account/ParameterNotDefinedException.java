package com.langostinko.account;

class ParameterNotDefinedException extends Exception {
    ParameterNotDefinedException(String param) {
        super(param);
    }
}
