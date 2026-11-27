package com.rashmi.FinanceGo.service;

import com.rashmi.FinanceGo.dto.TransactionRequest;
import com.rashmi.FinanceGo.entity.*;
import com.rashmi.FinanceGo.exception.ResourceNotFoundException;
import com.rashmi.FinanceGo.repository.AccountRepository;
import com.rashmi.FinanceGo.repository.CategoryRepository;
import com.rashmi.FinanceGo.repository.TransactionRepository;
import com.rashmi.FinanceGo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }
    public Transaction create(String ownerEmail, TransactionRequest request) {
        User owner = userRepository.findByEmail(ownerEmail).orElseThrow();
        Account account = accountRepository.findByIdAndOwner(request.accountId(), owner)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        Category category = categoryRepository.findByIdAndOwner(request.categoryId(),owner)
                .orElseThrow(()->new ResourceNotFoundException("Category not found"));
        Transaction t = new Transaction();
        t.setOwner(owner);
        t.setAccount(account);
        t.setCategory(category);
        t.setType(request.type());
        t.setAmount(request.amount());
        t.setNote(request.note());
        t.setDate(request.date());
        return transactionRepository.save(t);
    }
    public List<Transaction> list(String ownerEmail, LocalDate from, LocalDate to) {
        User owner = userRepository.findByEmail(ownerEmail).orElseThrow();
        if(from!=null && to!=null)
        {
            return transactionRepository.findByOwnerAndDateBetween(owner,from,to);
        }
        return transactionRepository.findByOwner(owner);
    }
}
