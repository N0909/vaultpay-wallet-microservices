package com.vaultpay.wallet_service.wallet.service.implementation;

import com.vaultpay.wallet_service.exception.wallet.*;
import com.vaultpay.wallet_service.wallet.dto.*;
import com.vaultpay.wallet_service.wallet.entity.*;
import com.vaultpay.wallet_service.wallet.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.vaultpay.wallet_service.wallet.service.interfaces.WalletService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static tools.jackson.databind.type.LogicalType.DateTime;

@Service
public class WalletServiceImplementation implements WalletService {
    private final WalletRepository walletRepository;
    private final WalletService walletService;

    public WalletServiceImplementation(WalletRepository walletRepository, WalletService walletService){
        this.walletRepository = walletRepository;
        this.walletService = walletService;
    }

    @Transactional
    public WalletResponse createWallet(String userId){

        boolean existWallet = walletRepository.existsByUserId(userId);

        if (existWallet){
            throw new WalletAlreadyExistException("Wallet already exists.");
        }

        Wallet wallet = new Wallet();

        wallet.setUserId(userId);
        wallet.setCurrency(WalletCurrency.INR);
        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setBalance(BigDecimal.ZERO);

        try {
            Wallet createdWallet = walletRepository.save(wallet);

            return new WalletResponse(createdWallet.getWalletId(),
                                      createdWallet.getBalance(),
                                      createdWallet.getCurrency(),
                                      createdWallet.getStatus(),
                                      createdWallet.getCreatedAt()
                                    );

        }catch(DataIntegrityViolationException ex){
            throw new WalletAlreadyExistException("Wallet already exists.");
        }
    }

    public WalletResponse getMyWallet(String userId){
        Wallet fetchedWallet = walletRepository
                                .findByUserId(userId).orElseThrow(
                                        ()->new WalletNotFoundException("Wallet not found.")
                                );

        return new WalletResponse(
                fetchedWallet.getWalletId(),
                fetchedWallet.getBalance(),
                fetchedWallet.getCurrency(),
                fetchedWallet.getStatus(),
                fetchedWallet.getCreatedAt()
        );
    }
    @Transactional
    public WalletResponse deposit(DepositRequest depositRequest, String userId){
        if (depositRequest.depositAmount().compareTo(BigDecimal.ZERO)<=0){
            throw new InvalidAmountException("Deposit amount must be greater than zero.");
        }

        Wallet userWallet = walletRepository.findByUserId(userId).orElseThrow(
                ()->new WalletNotFoundException("Wallet not found.")
        );

        if (userWallet.getStatus()==WalletStatus.CLOSED){
            throw new WalletClosedException("Wallet is closed");
        }

        if (userWallet.getStatus()==WalletStatus.FROZEN){
            throw new WalletFrozenException("Wallet is frozen");
        }

        BigDecimal updatedBalance =  userWallet.getBalance().add(depositRequest.depositAmount());
        userWallet.setBalance(updatedBalance);
        userWallet.setUpdatedAt(LocalDateTime.now());

        Wallet updatedUserWallet = walletRepository.save(userWallet);

        return new WalletResponse(
                updatedUserWallet.getWalletId(),
                updatedUserWallet.getBalance(),
                updatedUserWallet.getCurrency(),
                updatedUserWallet.getStatus(),
                updatedUserWallet.getCreatedAt()
        );
    }
    @Transactional
    public WalletResponse withdraw(WithdrawRequest withdrawRequest, String userId){
        if (withdrawRequest.amount().compareTo(BigDecimal.ZERO)<=0){
            throw new InvalidAmountException("Withdraw amount must be greater than zero.");
        }
        Wallet userWallet = walletRepository
                            .findByUserId(userId).orElseThrow(
                                    ()->new WalletNotFoundException("Wallet not found.")
                            );

        if (userWallet.getStatus()==WalletStatus.CLOSED){
            throw new WalletClosedException("Wallet is closed");
        }

        if (userWallet.getStatus()==WalletStatus.FROZEN){
            throw new WalletFrozenException("Wallet is frozen");
        }

        if(userWallet.getBalance().compareTo(withdrawRequest.amount())<0){
            throw new InvalidAmountException("Insufficient Balance.");
        }

        BigDecimal updatedBalance = userWallet.getBalance().subtract(withdrawRequest.amount());
        userWallet.setBalance(updatedBalance);
        userWallet.setUpdatedAt(LocalDateTime.now());

        Wallet updatedUserWallet = walletRepository.save(userWallet);

        return new WalletResponse(
                updatedUserWallet.getWalletId(),
                updatedUserWallet.getBalance(),
                updatedUserWallet.getCurrency(),
                updatedUserWallet.getStatus(),
                updatedUserWallet.getCreatedAt()
        );
    }
}
