package com.rashmi.FinanceGo.dto;

import com.rashmi.FinanceGo.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionResponse(Long id, Long accountId, Long categoryId, TransactionType type,
                                  BigDecimal amount, String note, LocalDate date, LocalDateTime createdAt) {
    
}
