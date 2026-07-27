package com.vaultpay.wallet_service.wallet.dto;

import java.math.BigDecimal;

public record WithdrawRequest(BigDecimal amount) {
}
