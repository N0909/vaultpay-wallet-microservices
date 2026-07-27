package com.vaultpay.wallet_service.exception.wallet;

public class WalletFrozenException extends RuntimeException {
    public WalletFrozenException(String message) {
        super(message);
    }
    public WalletFrozenException(String message, Throwable cause){
        super(message, cause);
    }
}
