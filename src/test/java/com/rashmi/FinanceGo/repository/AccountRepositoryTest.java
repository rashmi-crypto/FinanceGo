package com.rashmi.FinanceGo.repository;

import com.rashmi.FinanceGo.entity.Account;
import com.rashmi.FinanceGo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Test
    void findByIdAndOwner_returnsAccount_forOwner()
    {
        User owner = new User();
        owner.setEmail("test@example.com");
        owner.setPasswordHash("hash");
        owner.setFullName("Test User");
        userRepository.save(owner);
        Account account = new Account();
        account.setOwner(owner);
        account.setName("Wallet");
        account.setOpeningBalance(new BigDecimal("100.00"));
        Account savedAccount = accountRepository.save(account);
        Optional<Account> result =
                accountRepository.findByIdAndOwner(savedAccount.getId(), owner);
        assertTrue(result.isPresent());
        assertEquals("Wallet",result.get().getName());
    }
    @Test
    void findByIdAndOwner_returnsEmpty_forOtherUser()
    {
        User owner = new User();
        owner.setEmail("owner@example.com");
        owner.setPasswordHash("passhash");
        owner.setFullName("Owner Test");
        userRepository.save(owner);
        User other = new User();
        other.setEmail("other@example.com");
        other.setPasswordHash("otherhash");
        other.setFullName("Other Test");
        userRepository.save(other);
        Account account = new Account();
        account.setOwner(owner);
        account.setName("Wallet");
        account.setOpeningBalance(new BigDecimal("100.00"));
        Account savedAccount = accountRepository.save(account);
        Optional<Account> result =
                accountRepository.findByIdAndOwner(savedAccount.getId(), other);
        assertTrue(result.isEmpty());
    }
}