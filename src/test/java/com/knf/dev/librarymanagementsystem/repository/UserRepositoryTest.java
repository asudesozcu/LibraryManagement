package com.knf.dev.librarymanagementsystem.repository;

import com.knf.dev.librarymanagementsystem.entity.Role;
import com.knf.dev.librarymanagementsystem.entity.User;
import com.knf.dev.librarymanagementsystem.config.TestSecurityConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail_ShouldReturnUser() {
        // GIVEN
        Role role = new Role();
        role.setName("ROLE_USER");

        User user = new User(
                "Ali",
                "Yılmaz",
                "ali@example.com",
                "12345", // hashlenmemiş şifre, burada test için sorun değil
                Collections.singleton(role)
        );

        userRepository.save(user);

        // WHEN
        User found = userRepository.findByEmail("ali@example.com");

        // THEN
        assertNotNull(found);
        assertEquals("Ali", found.getFirstName());
        assertEquals("Yılmaz", found.getLastName());
        assertEquals("ali@example.com", found.getEmail());
        assertEquals("12345", found.getPassword());
        assertEquals(1, found.getRoles().size());
    }

    @Test
    void testFindByEmail_ShouldReturnNull_IfNotFound() {
        User found = userRepository.findByEmail("notfound@example.com");
        assertNull(found);
    }
}
