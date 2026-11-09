package com.vaultpay.wallet_service.wallet.dto;

import com.vaultpay.wallet_service.wallet.entity.WalletCurrency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransferResponse(
        UUID transactionId,
        BigDecimal transferredAmount,
        WalletCurrency currency,
        Instant timestamp
) {}