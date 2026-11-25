package com.rashmi.FinanceGo.repository;

import com.rashmi.FinanceGo.entity.Category;
import com.rashmi.FinanceGo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findByOwner(User owner);
    Optional<Category> findByIdAndOwner(Long id, User owner);
}