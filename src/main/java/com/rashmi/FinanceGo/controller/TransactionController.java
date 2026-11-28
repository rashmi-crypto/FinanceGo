package com.rashmi.FinanceGo.controller;

import com.rashmi.FinanceGo.dto.AccountResponse;
import com.rashmi.FinanceGo.dto.TransactionRequest;
import com.rashmi.FinanceGo.dto.TransactionResponse;
import com.rashmi.FinanceGo.entity.Account;
import com.rashmi.FinanceGo.entity.Transaction;
import com.rashmi.FinanceGo.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request,
                                                      Authentication authentication) {
        Transaction transaction = transactionService.create(authentication.getName(), request);
        TransactionResponse transactionResponse = new TransactionResponse(transaction.getId(),transaction.getAccount().getId(),
                transaction.getCategory().getId(),transaction.getType(),transaction.getAmount(),
                transaction.getNote(),transaction.getDate(),transaction.getCreatedAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication authentication) {
        List<TransactionResponse> result = new ArrayList<>();
        for (Transaction transaction : transactionService.list(authentication.getName(),from,to)) {
            result.add(new TransactionResponse(transaction.getId(),transaction.getAccount().getId(),transaction.getCategory().getId(),
                    transaction.getType(),transaction.getAmount(),transaction.getNote(),transaction.getDate(),
                    transaction.getCreatedAt()));
        }
        return ResponseEntity.ok(result);
    }

    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(t.getId(),t.getAccount().getId(),
                t.getCategory().getId(),t.getType(),t.getAmount(),
                t.getNote(),t.getDate(),t.getCreatedAt());
    }
}
