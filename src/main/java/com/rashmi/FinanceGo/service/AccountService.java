package com.rashmi.FinanceGo.service;

import com.rashmi.FinanceGo.dto.AccountRequest;
import com.rashmi.FinanceGo.entity.Account;
import com.rashmi.FinanceGo.entity.User;
import com.rashmi.FinanceGo.repository.AccountRepository;
import com.rashmi.FinanceGo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account create(String ownerEmail, AccountRequest request) {
        User owner = userRepository.findByEmail(ownerEmail).orElseThrow();
        Account account = new Account();
        account.setOwner(owner);
        account.setName(request.name());
        account.setOpeningBalance(request.openingBalance());
        return accountRepository.save(account);
    }

    public List<Account> listForOwner(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail).orElseThrow();
        return accountRepository.findByOwner(owner);

    }
}
