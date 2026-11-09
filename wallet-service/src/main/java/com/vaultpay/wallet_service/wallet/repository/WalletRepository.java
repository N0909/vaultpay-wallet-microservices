package com.vaultpay.wallet_service.wallet.repository;

import com.vaultpay.wallet_service.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    Optional<Wallet> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
}
