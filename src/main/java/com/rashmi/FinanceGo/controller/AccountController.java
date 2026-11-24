package com.rashmi.FinanceGo.controller;

import com.rashmi.FinanceGo.dto.AccountRequest;
import com.rashmi.FinanceGo.dto.AccountResponse;
import com.rashmi.FinanceGo.entity.Account;
import com.rashmi.FinanceGo.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountRequest request,
                                                  Authentication authentication) {
        Account account = accountService.create(authentication.getName(), request);
        AccountResponse response = new AccountResponse(account.getId(),
                account.getName(),account.getOpeningBalance(),
                account.getCreatedAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<AccountResponse>> list(Authentication authentication) {
        List<AccountResponse> result = new ArrayList<>();
        for (Account account : accountService.listForOwner(authentication.getName())) {
            result.add(new AccountResponse(account.getId(), account.getName(),
                    account.getOpeningBalance(), account.getCreatedAt()));
        }
        return ResponseEntity.ok(result);
    }
    private AccountResponse toResponse(Account account) {
        return new AccountResponse(account.getId(), account.getName(),
                account.getOpeningBalance(), account.getCreatedAt());
    }
}
