package com.vaultpay.wallet_service.wallet.service.interfaces;

import com.vaultpay.wallet_service.wallet.dto.DepositRequest;
import com.vaultpay.wallet_service.wallet.dto.WalletResponse;
import com.vaultpay.wallet_service.wallet.dto.WithdrawRequest;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {
    WalletResponse createWallet(String userId);
    WalletResponse getMyWallet(String userId);
    WalletResponse deposit(DepositRequest depositRequest, String userId);
    WalletResponse withdraw(WithdrawRequest withdrawRequest, String userId);
}
