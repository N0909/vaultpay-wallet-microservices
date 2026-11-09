package com.vaultpay.wallet_service.wallet.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="wallets")
@Data
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "wallet_id")
    private UUID walletId;
    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;
    @Column(name="balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    @Column(name="currency", nullable = false, length = 3)
    private WalletCurrency currency;
    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private WalletStatus status;
    @Version
    @Column(name="version", nullable = false)
    private Long version;
    @Column(name="created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name="updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
