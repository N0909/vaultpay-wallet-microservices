package com.vaultpay.wallet_service.wallet.service.interfaces;

import com.vaultpay.wallet_service.wallet.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {
    WalletResponse createWallet(String userId);
    WalletResponse getMyWallet(String userId);
    WalletResponse deposit(DepositRequest depositRequest, String userId);
    WalletResponse withdraw(WithdrawRequest withdrawRequest, String userId);

    TransferResponse transfer(TransferRequest transferRequest, String senderId, String receiverId);
}
