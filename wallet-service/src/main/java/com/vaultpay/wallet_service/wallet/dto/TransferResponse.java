package com.vaultpay.wallet_service.wallet.dto;

import java.math.BigDecimal;

public record TransferResponse(
        String senderWalletId,
        BigDecimal senderBalance,
        String receiverWalletId,
        BigDecimal receiverBalance,
        BigDecimal transferredAmount
) {}