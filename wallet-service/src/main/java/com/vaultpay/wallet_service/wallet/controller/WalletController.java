package com.vaultpay.wallet_service.wallet.controller;

import com.vaultpay.wallet_service.security.principal.WalletUserPrincipal;
import com.vaultpay.wallet_service.wallet.dto.*;
import com.vaultpay.wallet_service.wallet.service.interfaces.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@PreAuthorize("isAuthenticated()")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService){
        this.walletService = walletService;
    }

    @PostMapping("/create-wallet")
    public ResponseEntity<WalletResponse> createWallet(
            @AuthenticationPrincipal WalletUserPrincipal principal) {

        WalletResponse walletResponse =
                walletService.createWallet(principal.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(walletResponse);
    }

    @GetMapping("/get-my-wallet")
    public ResponseEntity<WalletResponse> getMyWallet(
            @AuthenticationPrincipal WalletUserPrincipal principal) {
        WalletResponse walletResponse =
                walletService.getMyWallet(principal.getUserId());

        return ResponseEntity.status(HttpStatus.OK).body(walletResponse);
    }

    @PostMapping("/deposit")
    public ResponseEntity<WalletResponse> deposit(@RequestBody DepositRequest depositRequest, @AuthenticationPrincipal WalletUserPrincipal principal) {
        WalletResponse walletResponse = walletService.deposit(depositRequest, principal.getUserId());

        return ResponseEntity.status(HttpStatus.OK).body(walletResponse);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WalletResponse> withdraw(@RequestBody WithdrawRequest withdrawRequest, @AuthenticationPrincipal WalletUserPrincipal principal) {
        WalletResponse walletResponse = walletService.withdraw(withdrawRequest, principal.getUserId());

        return ResponseEntity.status(HttpStatus.OK).body(walletResponse);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest transferRequest, @AuthenticationPrincipal WalletUserPrincipal principal) {
        TransferResponse transferResponse = walletService.transfer(transferRequest, principal.getUserId());

        return ResponseEntity.status(HttpStatus.OK).body(transferResponse);
    }
}
