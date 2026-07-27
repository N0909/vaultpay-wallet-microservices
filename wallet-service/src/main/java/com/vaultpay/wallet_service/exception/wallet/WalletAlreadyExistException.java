package com.vaultpay.wallet_service.exception.wallet;

public class WalletAlreadyExistException extends RuntimeException{
    public WalletAlreadyExistException(String message){
        super(message);
    }

    public WalletAlreadyExistException(String message, Throwable cause){
        super(message, cause);
    }

    public WalletAlreadyExistException(Throwable cause){
        super(cause);
    }
}
