package com.vaultpay.wallet_service.wallet.dto;

import com.vaultpay.wallet_service.wallet.entity.WalletCurrency;
import com.vaultpay.wallet_service.wallet.entity.WalletStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WalletResponse(java.util.UUID walletId, BigDecimal balance, WalletCurrency currency, WalletStatus status, LocalDateTime createdAt) {
}
