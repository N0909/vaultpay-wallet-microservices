package com.vaultpay.wallet_service.wallet.dto;

import java.math.BigDecimal;

public record TransferRequest(String receiverId, BigDecimal amount) {
}
