package com.vaultpay.wallet_service.wallet.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="wallets")
@Data
public class Wallet {
    @Id
    @Column(name="wallet_id")
    private String walletId;
    @Column(name="user_id", nullable = false, unique = true)
    private String userId;
    @Column(name="balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;
    @Column(name="currency", nullable = false, length = 3)
    private WalletCurrency currency;
    @Column(name="status", nullable = false)
    private WalletStatus status;
    @Version
    @Column(name="version", nullable = false)
    private int version;
    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(name="updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
