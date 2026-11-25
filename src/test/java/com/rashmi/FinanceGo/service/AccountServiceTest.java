package com.rashmi.FinanceGo.service;

import com.rashmi.FinanceGo.dto.AccountRequest;
import com.rashmi.FinanceGo.entity.Account;
import com.rashmi.FinanceGo.entity.User;
import com.rashmi.FinanceGo.repository.AccountRepository;
import com.rashmi.FinanceGo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountService accountService;
    @Test
        void create_savesAccountForOwner(){
            User user = new User();
            user.setEmail("a@example.com");
            user.setPasswordHash("stored-hash");
            when(userRepository.findByEmail("a@example.com")).thenReturn(Optional.of(user));
            when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));
        AccountRequest request = new AccountRequest("Wallet", new BigDecimal("100.00"));
        Account result = accountService.create("a@example.com", request);
            assertEquals("Wallet",result.getName());
            assertEquals(new BigDecimal("100.00"), result.getOpeningBalance());
            assertSame(user,result.getOwner());
        }
        @Test
        void create_throws_whenOwnerMissing()
        {
            AccountRequest request = new AccountRequest("Wallet", new BigDecimal("100.00"));
            assertThrows(NoSuchElementException.class,() -> accountService.create("a@example.com",request));
            verifyNoInteractions(accountRepository);
        }
        @Test
    void listForOwner_returnsOnlyThatOwnersAccounts()
        {
            User user = new User();
            user.setEmail("a@example.com");
            user.setPasswordHash("passhash");
            when(userRepository.findByEmail("a@example.com")).thenReturn(Optional.of(user));
            Account account = new Account();
            account.setName("Wallet");
            when(accountRepository.findByOwner(user)).thenReturn(List.of(account));
            List<Account> result = accountService.listForOwner("a@example.com");
            assertEquals(1,result.size());
            assertSame(account,result.get(0));
        }
}
