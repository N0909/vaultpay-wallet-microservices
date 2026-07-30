package com.vaultpay.wallet_service.exception.wallet;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
    public InsufficientBalanceException(String message, Throwable cause){
        super(message, cause);
    }
}
