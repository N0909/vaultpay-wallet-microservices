package com.vaultpay.wallet_service.exception.wallet;

public class WalletNotFoundException extends RuntimeException{
    public WalletNotFoundException(String message){
        super(message);
    }
    public WalletNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
