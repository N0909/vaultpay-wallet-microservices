package com.vaultpay.wallet_service.wallet.service;

import com.vaultpay.wallet_service.exception.wallet.*;
import com.vaultpay.wallet_service.wallet.dto.DepositRequest;
import com.vaultpay.wallet_service.wallet.dto.WalletResponse;
import com.vaultpay.wallet_service.wallet.dto.WithdrawRequest;
import com.vaultpay.wallet_service.wallet.entity.Wallet;
import com.vaultpay.wallet_service.wallet.entity.WalletCurrency;
import com.vaultpay.wallet_service.wallet.entity.WalletStatus;
import com.vaultpay.wallet_service.wallet.repository.WalletRepository;
import com.vaultpay.wallet_service.wallet.service.interfaces.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;
    @InjectMocks
    private WalletService walletService;

    @Test
    void shouldCreateWalletSuccessfully(){
        String userId = UUID.randomUUID().toString();
        String walletId = UUID.randomUUID().toString();

        Wallet savedWallet = new Wallet();
        savedWallet.setWalletId(walletId);
        savedWallet.setUserId(userId);
        savedWallet.setBalance(BigDecimal.ZERO);
        savedWallet.setCurrency(WalletCurrency.INR);
        savedWallet.setStatus(WalletStatus.ACTIVE);
        savedWallet.setCreatedAt(LocalDateTime.now());

        when(walletRepository.existsByUserId(userId))
                .thenReturn(false);

        when(walletRepository.save(any())).thenReturn(savedWallet);

        WalletResponse walletResponse = walletService.createWallet(userId);

        assertNotNull(walletResponse);
        assertEquals(savedWallet.getWalletId(), walletResponse.walletId());
        assertEquals(savedWallet.getBalance(), walletResponse.balance());
        assertEquals(savedWallet.getCurrency(), walletResponse.currency());
        assertEquals(savedWallet.getStatus(), walletResponse.status());
        assertEquals(savedWallet.getCreatedAt(), walletResponse.createdAt());

        verify(walletRepository).existsByUserId(userId);
        verify(walletRepository).save(any(Wallet.class));
    }

    @Test
    void shouldThrowWalletAlreadyExistsExceptionWhenWalletAlreadyExists(){
        String userId = UUID.randomUUID().toString();

        when(walletRepository.existsByUserId(userId)).thenReturn(true);

        WalletAlreadyExistException exception = assertThrows(
                WalletAlreadyExistException.class,
                ()->walletService.createWallet(userId)
        );

        assertEquals("Wallet already exists.", exception.getMessage());

        verify(walletRepository).existsByUserId(userId);
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void shouldFetchWalletSuccessfully() {

        String userId = UUID.randomUUID().toString();
        String walletId = UUID.randomUUID().toString();

        Wallet existingWallet = new Wallet();
        existingWallet.setWalletId(walletId);
        existingWallet.setUserId(userId);
        existingWallet.setBalance(BigDecimal.ZERO);
        existingWallet.setCurrency(WalletCurrency.INR);
        existingWallet.setStatus(WalletStatus.ACTIVE);
        existingWallet.setCreatedAt(LocalDateTime.now());

        when(walletRepository.findByUserId(userId))
                .thenReturn(Optional.of(existingWallet));

        WalletResponse walletResponse = walletService.getMyWallet(userId);

        assertNotNull(walletResponse);
        assertEquals(existingWallet.getWalletId(), walletResponse.walletId());
        assertEquals(existingWallet.getBalance(), walletResponse.balance());
        assertEquals(existingWallet.getCurrency(), walletResponse.currency());
        assertEquals(existingWallet.getStatus(), walletResponse.status());
        assertEquals(existingWallet.getCreatedAt(), walletResponse.createdAt());

        verify(walletRepository).findByUserId(userId);
    }

    @Test
    void shouldThrowWalletNotFoundExceptionWhenWalletDoesNotExist(){
        String userId = UUID.randomUUID().toString();

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.empty());

        WalletNotFoundException exception = assertThrows(
                WalletNotFoundException.class ,() -> walletService.getMyWallet(userId)
        );

        assertEquals("Wallet Not Found.", exception.getMessage());

        verify(walletRepository).findByUserId(userId);
    }

    @Test
    void shouldDepositSuccessfully(){
        String userId = UUID.randomUUID().toString();
        String walletId = UUID.randomUUID().toString();

        DepositRequest depositRequest = new DepositRequest(BigDecimal.valueOf(10.0d));

        Wallet existingWallet = new Wallet();
        existingWallet.setWalletId(walletId);
        existingWallet.setUserId(userId);
        existingWallet.setBalance(BigDecimal.ZERO);
        existingWallet.setCurrency(WalletCurrency.INR);
        existingWallet.setStatus(WalletStatus.ACTIVE);
        existingWallet.setCreatedAt(LocalDateTime.now());

        BigDecimal expectedBalance = existingWallet.getBalance().add(depositRequest.depositAmount());

        when(walletRepository.findByUserId(userId))
                .thenReturn(Optional.of(existingWallet));
        when(walletRepository.save(existingWallet)).thenReturn(existingWallet);

        WalletResponse walletResponse = walletService.deposit(depositRequest, userId);

        assertNotNull(walletResponse);
        assertEquals(existingWallet.getWalletId(), walletResponse.walletId());
        assertEquals(expectedBalance, walletResponse.balance());
        assertEquals(existingWallet.getCurrency(), walletResponse.currency());
        assertEquals(existingWallet.getStatus(), walletResponse.status());
        assertEquals(existingWallet.getCreatedAt(), walletResponse.createdAt());

        verify(walletRepository).findByUserId(userId);
        verify(walletRepository).save(existingWallet);
    }
    @Test
    void shouldThrowInvalidBalanceExceptionWhenDepositAmountIsZero(){
        String userId = UUID.randomUUID().toString();

        DepositRequest depositRequest = new DepositRequest(BigDecimal.valueOf(0.0d));

        InvalidAmountException exception = assertThrows(InvalidAmountException.class , ()->{
            walletService.deposit(depositRequest, userId);
        });

        assertEquals("Deposit amount must be greater than zero.", exception.getMessage());

        verify(walletRepository, never()).findByUserId(anyString());
        verify(walletRepository, never()).save(any(Wallet.class));
    }
    @Test
    void shouldThrowInvalidBalanceExceptionWhenDepositAmountIsLessThanZero(){
        String userId = UUID.randomUUID().toString();

        DepositRequest depositRequest = new DepositRequest(BigDecimal.valueOf(-1.0d));

        InvalidAmountException exception = assertThrows(InvalidAmountException.class , ()->{
            walletService.deposit(depositRequest, userId);
        });

        assertEquals("Deposit amount must be greater than zero.", exception.getMessage());

        verify(walletRepository, never()).findByUserId(anyString());
        verify(walletRepository, never()).save(any(Wallet.class));
    }
    @Test
    void shouldThrowWalletClosedExceptionWhenWalletClosed(){
        String userId = UUID.randomUUID().toString();
        String walletId = UUID.randomUUID().toString();

        DepositRequest depositRequest = new DepositRequest(BigDecimal.valueOf(10.0d));

        Wallet existingWallet = new Wallet();
        existingWallet.setWalletId(walletId);
        existingWallet.setUserId(userId);
        existingWallet.setBalance(BigDecimal.ZERO);
        existingWallet.setCurrency(WalletCurrency.INR);
        existingWallet.setStatus(WalletStatus.CLOSED);
        existingWallet.setCreatedAt(LocalDateTime.now());

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(existingWallet));

        WalletClosedException exception = assertThrows(WalletClosedException.class ,
                ()->walletService.deposit(depositRequest, userId)
        );

        assertEquals("Wallet is closed", exception.getMessage());

        verify(walletRepository).findByUserId(userId);
        verify(walletRepository,never()).save(any(Wallet.class));
    }
    @Test
    void shouldThrowWalletFrozenExceptionForFrozenWallet(){
        String userId = UUID.randomUUID().toString();
        String walletId = UUID.randomUUID().toString();

        DepositRequest depositRequest = new DepositRequest(BigDecimal.valueOf(10.0d));

        Wallet existingWallet = new Wallet();
        existingWallet.setWalletId(walletId);
        existingWallet.setUserId(userId);
        existingWallet.setBalance(BigDecimal.ZERO);
        existingWallet.setCurrency(WalletCurrency.INR);
        existingWallet.setStatus(WalletStatus.FROZEN);
        existingWallet.setCreatedAt(LocalDateTime.now());

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(existingWallet));

        WalletFrozenException exception = assertThrows(WalletFrozenException.class ,
                ()->walletService.deposit(depositRequest, userId)
        );

        assertEquals("Wallet is frozen", exception.getMessage());

        verify(walletRepository).findByUserId(userId);
        verify(walletRepository,never()).save(any(Wallet.class));
    }
    // Unit Test for Withdraw
    @Test
    void shouldWithdrawSuccessfully() {
        String userId = UUID.randomUUID().toString();
        String walletId = UUID.randomUUID().toString();

        WithdrawRequest withdrawRequest =
                new WithdrawRequest(BigDecimal.valueOf(10.0));

        Wallet existingWallet = new Wallet();
        existingWallet.setWalletId(walletId);
        existingWallet.setUserId(userId);
        existingWallet.setBalance(BigDecimal.valueOf(100.0));
        existingWallet.setCurrency(WalletCurrency.INR);
        existingWallet.setStatus(WalletStatus.ACTIVE);
        existingWallet.setCreatedAt(LocalDateTime.now());

        BigDecimal expectedBalance = existingWallet.getBalance()
                .subtract(withdrawRequest.amount());

        when(walletRepository.findByUserId(userId))
                .thenReturn(Optional.of(existingWallet));

        when(walletRepository.save(any(Wallet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        WalletResponse walletResponse =
                walletService.withdraw(withdrawRequest, userId);

        assertEquals(existingWallet.getWalletId(), walletResponse.walletId());
        assertEquals(expectedBalance, walletResponse.balance());
        assertEquals(existingWallet.getCurrency(), walletResponse.currency());
        assertEquals(existingWallet.getStatus(), walletResponse.status());
        assertEquals(existingWallet.getCreatedAt(), walletResponse.createdAt());

        assertEquals(expectedBalance, existingWallet.getBalance());

        verify(walletRepository).findByUserId(userId);
        verify(walletRepository).save(existingWallet);
    }
    @Test
    void shouldThrowInvalidAmountExceptionWhenWithdrawAmountIsZero(){
        String userId = UUID.randomUUID().toString();

        WithdrawRequest withdrawRequest = new WithdrawRequest(BigDecimal.valueOf(0.0d));

        InvalidAmountException exception = assertThrows(InvalidAmountException.class,
                ()->walletService.withdraw(withdrawRequest, userId)
        );

        assertEquals("Withdraw amount must be greater than zero.", exception.getMessage());

        verify(walletRepository, never()).findByUserId(anyString());
        verify(walletRepository, never()).save(any(Wallet.class));
    }
    @Test
    void shouldThrowInvalidAmountExceptionWhenWithdrawAmountIsLessThanZero(){
        String userId = UUID.randomUUID().toString();

        WithdrawRequest withdrawRequest = new WithdrawRequest(BigDecimal.valueOf(-10.0d));

        InvalidAmountException exception = assertThrows(InvalidAmountException.class,
                ()->walletService.withdraw(withdrawRequest, userId)
        );

        assertEquals("Withdraw amount must be greater than zero.", exception.getMessage());

        verify(walletRepository,never()).findByUserId(anyString());
        verify(walletRepository, never()).save(any(Wallet.class));
    }
    @Test
    void shouldThrowWalletClosedExceptionWhenWalletStatusIsClosedOnWithdraw(){
        String userId = UUID.randomUUID().toString();
        String walletId = UUID.randomUUID().toString();

        WithdrawRequest withdrawRequest = new WithdrawRequest(BigDecimal.valueOf(10.0d));

        Wallet existingWallet = new Wallet();
        existingWallet.setWalletId(walletId);
        existingWallet.setUserId(userId);
        existingWallet.setBalance(BigDecimal.ZERO);
        existingWallet.setCurrency(WalletCurrency.INR);
        existingWallet.setStatus(WalletStatus.CLOSED);
        existingWallet.setCreatedAt(LocalDateTime.now());

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(existingWallet));

        WalletClosedException exception = assertThrows(WalletClosedException.class ,
                ()->walletService.withdraw(withdrawRequest, userId)
        );

        assertEquals("Wallet is closed", exception.getMessage());

        verify(walletRepository).findByUserId(userId);
        verify(walletRepository,never()).save(any(Wallet.class));
    }
    @Test
    void shouldThrowWalletFrozenExceptionWhenWalletStatusIsFrozenOnWithdraw(){
        String userId = UUID.randomUUID().toString();
        String walletId = UUID.randomUUID().toString();

        WithdrawRequest withdrawRequest = new WithdrawRequest(BigDecimal.valueOf(10.0d));

        Wallet existingWallet = new Wallet();
        existingWallet.setWalletId(walletId);
        existingWallet.setUserId(userId);
        existingWallet.setBalance(BigDecimal.ZERO);
        existingWallet.setCurrency(WalletCurrency.INR);
        existingWallet.setStatus(WalletStatus.FROZEN);
        existingWallet.setCreatedAt(LocalDateTime.now());

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(existingWallet));

        WalletFrozenException exception = assertThrows(WalletFrozenException.class ,
                ()->walletService.withdraw(withdrawRequest, userId)
        );

        assertEquals("Wallet is frozen", exception.getMessage());

        verify(walletRepository).findByUserId(userId);
        verify(walletRepository,never()).save(any(Wallet.class));
    }
    @Test
    void shouldThrowInvalidBalanceExceptionWhenBalanceIsLessThanWithdrawRequestAmount(){
        String userId = UUID.randomUUID().toString();
        String walletId = UUID.randomUUID().toString();

        WithdrawRequest withdrawRequest = new WithdrawRequest(BigDecimal.valueOf(10.0d));

        Wallet existingWallet = new Wallet();
        existingWallet.setWalletId(walletId);
        existingWallet.setUserId(userId);
        existingWallet.setBalance(BigDecimal.ZERO);
        existingWallet.setCurrency(WalletCurrency.INR);
        existingWallet.setStatus(WalletStatus.ACTIVE);
        existingWallet.setCreatedAt(LocalDateTime.now());

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(existingWallet));

        InvalidAmountException exception = assertThrows(InvalidAmountException.class, ()->walletService.withdraw(withdrawRequest, userId));

        assertEquals("Insufficient Balance.", exception.getMessage());

        verify(walletRepository).findByUserId(userId);
        verify(walletRepository, never()).save(any(Wallet.class));
    }

}
