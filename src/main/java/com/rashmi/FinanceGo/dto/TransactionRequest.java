package com.rashmi.FinanceGo.dto;

import com.rashmi.FinanceGo.entity.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(@NotNull Long accountId,
                                 @NotNull Long categoryId,
                                 @NotNull TransactionType type,
                                 @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
                                 String note,
                                 @NotNull LocalDate date) {
    
}
