package com.vaultpay.wallet_service.security.principal;

public class WalletUserPrincipal {
    private String userId;

    WalletUserPrincipal(){

    }

    public WalletUserPrincipal(String userId){
        this.userId = userId;
    }

    public void setUserId() {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
