package com.vaultpay.wallet_service.exception.wallet;

public class WalletClosedException extends RuntimeException {
    public WalletClosedException(String message) {
        super(message);
    }
    public WalletClosedException(String message, Throwable cause){
        super(message, cause);
    }
}
