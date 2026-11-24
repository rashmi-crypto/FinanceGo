package com.rashmi.FinanceGo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(Long id, String name, BigDecimal openingBalance, LocalDateTime createdAt) {

}
