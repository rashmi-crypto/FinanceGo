package com.rashmi.FinanceGo.repository;

import com.rashmi.FinanceGo.entity.Account;
import com.rashmi.FinanceGo.entity.Transaction;
import com.rashmi.FinanceGo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByOwner(User owner);
    Optional<Transaction> findByIdAndOwner(Long id, User owner);
    List<Transaction> findByOwnerAndDateBetween(User owner, LocalDate from, LocalDate to);
}
