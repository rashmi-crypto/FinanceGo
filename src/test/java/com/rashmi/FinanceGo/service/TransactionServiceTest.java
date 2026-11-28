package com.rashmi.FinanceGo.service;

import com.rashmi.FinanceGo.dto.TransactionRequest;
import com.rashmi.FinanceGo.entity.*;
import com.rashmi.FinanceGo.exception.ResourceNotFoundException;
import com.rashmi.FinanceGo.repository.AccountRepository;
import com.rashmi.FinanceGo.repository.CategoryRepository;
import com.rashmi.FinanceGo.repository.TransactionRepository;
import com.rashmi.FinanceGo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionService transactionService;
    private TransactionRequest request(TransactionType type) {
        return new TransactionRequest(1L, 2L, type, new BigDecimal("330.00"),
                "Weekend Dinner", LocalDate.of(2026, 10, 29));
    }
    @Test
    void create_throws_whenAccountNotOwned()
    {
        User user = new User();
        user.setEmail("a@example.com");
        user.setPasswordHash("stored-hash");
        user.setFullName("User Test");
        when(userRepository.findByEmail("a@example.com")).thenReturn(Optional.of(user));
        when(accountRepository.findByIdAndOwner(1L,user)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, ()->transactionService.create("a@example.com",request(TransactionType.EXPENSE)));
        verifyNoInteractions(transactionRepository);
    }
    @Test
    void create_throws_whenCategoryNotOwned()
    {
        User user = new User();
        user.setEmail("a@example.com");
        user.setPasswordHash("stored-hash");
        user.setFullName("User Test");
        when(userRepository.findByEmail("a@example.com")).thenReturn(Optional.of(user));
        when(accountRepository.findByIdAndOwner(1L, user)).thenReturn(Optional.of(new Account()));
        when(categoryRepository.findByIdAndOwner(2L,user)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.create("a@example.com", request(TransactionType.EXPENSE)));
        verify(transactionRepository, never()).save(any());
    }
    @Test
    void create_usesTypeFromRequest() {
        User user = new User();
        user.setEmail("a@example.com");
        user.setPasswordHash("stored-hash");
        user.setFullName("User Test");
        when(userRepository.findByEmail("a@example.com")).thenReturn(Optional.of(user));
        when(accountRepository.findByIdAndOwner(1L, user)).thenReturn(Optional.of(new Account()));
        when(categoryRepository.findByIdAndOwner(2L, user)).thenReturn(Optional.of(new Category()));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));
        Transaction result = transactionService.create("a@example.com", request(TransactionType.INCOME));
        assertEquals(TransactionType.INCOME,result.getType());
        assertEquals(0, new BigDecimal("330").compareTo(result.getAmount()));
        assertSame(user,result.getOwner());
    }
    @Test
    void list_usesDateRange_whenBothDatesGiven() {
        User user = new User();
        user.setEmail("a@example.com");
        user.setPasswordHash("stored-hash");
        user.setFullName("User Test");
        LocalDate from = LocalDate.of(2026, 11, 1);
        LocalDate to = LocalDate.of(2026, 11, 30);
        when(userRepository.findByEmail("a@example.com")).thenReturn(Optional.of(user));
        transactionService.list("a@example.com", from, to);
        verify(transactionRepository).findByOwnerAndDateBetween(user,from,to);
        verify(transactionRepository,never()).findByOwner(any());
    }
}
