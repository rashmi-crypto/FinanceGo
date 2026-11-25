package com.rashmi.FinanceGo.repository;

import com.rashmi.FinanceGo.entity.Account;
import com.rashmi.FinanceGo.entity.Category;
import com.rashmi.FinanceGo.entity.CategoryType;
import com.rashmi.FinanceGo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Test
    void findByIdAndOwner_returnsEmpty_forOtherUser(){
        User owner = new User();
        owner.setEmail("o@example.com");
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
        Category category = new Category();
        category.setType(CategoryType.PERSONAL);
        category.setName("Travel");
        category.setOwner(owner);
        Category savedCategory=categoryRepository.save(category);
        Optional<Category> result =
                categoryRepository.findByIdAndOwner(savedCategory.getId(), other);
        assertTrue(result.isEmpty());
    }
    @Test
    void save_duplicateNameForSameOwner_throws(){
        User owner = new User();
        owner.setEmail("ab@example.com");
        owner.setPasswordHash("passhash");
        owner.setFullName("Owner Test");
        userRepository.save(owner);
        Category first =new Category();
        first.setOwner(owner);
        first.setName("Travel");
        first.setType(CategoryType.PERSONAL);
        categoryRepository.save(first);
        Category second = new Category();
        second.setOwner(owner);
        second.setName("Travel");
        second.setType(CategoryType.PERSONAL);
        assertThrows(DataIntegrityViolationException.class,
                () -> categoryRepository.saveAndFlush(second));
    }
}
