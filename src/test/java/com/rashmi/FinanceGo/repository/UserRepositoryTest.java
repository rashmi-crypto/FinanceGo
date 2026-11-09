package com.rashmi.FinanceGo.repository;

import com.rashmi.FinanceGo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    void save_setsCreatedAt() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPasswordHash("hash");
        user.setFullName("Test User");

        User saved = userRepository.save(user);
        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
    }
    @Test
    void findByEmail_Found() {
                // Arrange
        User user = new User();
        user.setEmail("found@example.com");
        user.setPasswordHash("hash");
        user.setFullName("Found User");
        userRepository.save(user);

                // Act
        Optional<User> result = userRepository.findByEmail("found@example.com");

                // Assert
        assertTrue(result.isPresent());
        assertEquals("Found User", result.get().getFullName());
    }
    @Test
    void findByEmail_notFound()
    {
        Optional<User> result = userRepository.findByEmail("notfound@example.com");
        assertTrue(result.isEmpty());
    }
    @Test
    void save_duplicateEmail_throws()
    {
        User user1 = new User();
        user1.setEmail("duplicate@example.com");
        user1.setPasswordHash("hash");
        user1.setFullName("User One");
        userRepository.save(user1);
        User user2 = new User();
        user2.setEmail("duplicate@example.com");
        user2.setPasswordHash("hash");
        user2.setFullName("User Two");
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user2);
            // saveAndFlush a second user with the SAME email
        });
    }
}