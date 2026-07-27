package com.vaultpay.wallet_service.wallet.service.interfaces;

import com.vaultpay.wallet_service.wallet.dto.DepositRequest;
import com.vaultpay.wallet_service.wallet.dto.WalletResponse;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {
    WalletResponse createWallet(String userId);
    WalletResponse getMyWallet(String userId);
    WalletResponse deposit(DepositRequest depositRequest, String userId);
}
