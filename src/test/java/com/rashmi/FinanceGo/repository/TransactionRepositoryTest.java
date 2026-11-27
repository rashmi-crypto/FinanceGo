package com.rashmi.FinanceGo.repository;

import com.rashmi.FinanceGo.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TransactionRepository transactionRepository;
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
        other.setPasswordHash("otherpass");
        other.setFullName("Other Test");
        userRepository.save(other);
        Account account = new Account();
        account.setOwner(owner);
        account.setName("Wallet");
        account.setOpeningBalance(new BigDecimal("100.00"));
        accountRepository.save(account);
        Category category = new Category();
        category.setType(CategoryType.PERSONAL);
        category.setName("Food");
        category.setOwner(owner);
        categoryRepository.save(category);
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(new BigDecimal(350));
        transaction.setCategory(category);
        transaction.setType(TransactionType.EXPENSE);
        transaction.setDate(LocalDate.of(2026, 11, 10));
        transaction.setOwner(owner);
        Transaction savedTransaction = transactionRepository.save(transaction);
        Optional<Transaction> result =
                transactionRepository.findByIdAndOwner(savedTransaction.getId(), other);
        assertTrue(result.isEmpty());
    }
    @Test
    void findByOwnerAndDateBetween_returnsOnlyTransactionsInRange()
    {
        User owner = new User();
        owner.setEmail("owner@example.com");
        owner.setPasswordHash("passhash");
        owner.setFullName("Owner Test");
        userRepository.save(owner);
        Account account = new Account();
        account.setOwner(owner);
        account.setName("Wallet");
        account.setOpeningBalance(new BigDecimal("100.00"));
        accountRepository.save(account);
        Category category = new Category();
        category.setType(CategoryType.PERSONAL);
        category.setName("Food");
        category.setOwner(owner);
        categoryRepository.save(category);
        Transaction t1 = new Transaction();
        t1.setAccount(account);
        t1.setAmount(new BigDecimal(350));
        t1.setCategory(category);
        t1.setType(TransactionType.EXPENSE);
        t1.setDate(LocalDate.of(2026, 11, 10));
        t1.setOwner(owner);
        transactionRepository.save(t1);
        Transaction t2 = new Transaction();
        t2.setAccount(account);
        t2.setAmount(new BigDecimal("350"));
        t2.setCategory(category);
        t2.setType(TransactionType.EXPENSE);
        t2.setDate(LocalDate.of(2026, 10, 1));
        t2.setOwner(owner);
        transactionRepository.save(t2);
        List<Transaction> result = transactionRepository.findByOwnerAndDateBetween(
                owner, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));
        assertEquals(1, result.size());
        assertEquals(LocalDate.of(2026, 11, 10), result.get(0).getDate());
    }
}
