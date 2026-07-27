package com.vaultpay.wallet_service.exception.wallet;

public class InvalidAmountException extends RuntimeException{
    public InvalidAmountException(String message){
        super(message);
    }
    public InvalidAmountException(String message,Throwable cause){
        super(message, cause);
    }
}
