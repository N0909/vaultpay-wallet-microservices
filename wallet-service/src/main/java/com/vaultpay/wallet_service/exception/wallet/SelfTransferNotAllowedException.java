package com.vaultpay.wallet_service.exception.wallet;

public class SelfTransferNotAllowedException extends RuntimeException {
    public SelfTransferNotAllowedException(String message) {
        super(message);
    }
    public SelfTransferNotAllowedException(String message, Throwable cause){
        super(message, cause);
    }
}
