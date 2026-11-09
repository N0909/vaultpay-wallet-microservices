package com.vaultpay.wallet_service.exception.wallet;

public class IllegalAuthException extends RuntimeException {
    public IllegalAuthException(String message) {
        super(message);
    }
    public IllegalAuthException(String message, Throwable cause){
        super(message, cause);
    }
}
