package com.rashmi.FinanceGo.dto;

import java.time.LocalDateTime;

public record UserRequest(Long id, String email, String fullName, LocalDateTime createdAt) {
    
}
