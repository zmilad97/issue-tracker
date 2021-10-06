package com.github.zmilad97.bugtracker.repository;

import com.github.zmilad97.bugtracker.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setActive(true);
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setUsername("test");
        user.setEmail("test@test.com");
        user.setPassword("testPassword");
        userRepository.save(user);

        User user2 = new User();
        user2.setActive(true);
        user2.setFirstName("Firs2tName");
        user2.setLastName("LastNa2me");
        user2.setUsername("test2");
        user2.setEmail("test2@test2.com");
        user2.setPassword("test2Password");
        userRepository.save(user2);

        User user3 = new User();
        user3.setActive(true);
        user3.setFirstName("First3Name");
        user3.setLastName("Last3Name");
        user3.setUsername("test3");
        user3.setEmail("test3@test3.com");
        user3.setPassword("test3Password");
        userRepository.save(user3);


    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();

    }

    @Test
    void findUserById() {
        User user = userRepository.findAll().get(0);
        User found = userRepository.findUserById(user.getId());
        assertEquals(user,found);
    }

    @Test
    void findByUsername() {
        User user = userRepository.findAll().get(0);
        User found = userRepository.findByUsername(user.getUsername());
        assertEquals(user,found);

    }

    @Test
    void findByEmail() {
        User user = userRepository.findAll().get(0);
        User found = userRepository.findByEmail(user.getEmail());
        assertEquals(user,found);
    }
}