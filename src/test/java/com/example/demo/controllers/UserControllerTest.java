package com.example.demo.controllers;

import com.example.demo.DTO.User;
import com.example.demo.exceptions.DuplicateEmailException;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    public static final String UPDATED_EMAIL_COM = "some_updated@email.com";
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String ADDRESS = "Address 1";
    public static final String PHONE_NUMBER = "+380999999999";
    public static final String EMAIL = "email@email.com";
    public static final Date BIRTH_DATE = Date.valueOf("2000-11-01");
    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository.deleteAll();
    }

    @Test
    void createUser() {
        User user = getUser();
        User createdUser = userController.save(user);
        assertNotNull(createdUser.id());
        assertEquals(user.firstName(), createdUser.firstName());
        assertEquals(user.lastName(), createdUser.lastName());
        assertEquals(user.email(), createdUser.email());
        assertEquals(user.address(), createdUser.address());
        assertEquals(user.phoneNumber(), createdUser.phoneNumber());
        assertEquals(user.birthDate(), createdUser.birthDate());
    }

    @Test
    void createUser_whenEmailExists() {
        User user = getUser();
        userController.save(user);
        assertThrows(DuplicateEmailException.class, () -> userController.save(user));
    }

    @Test
    void updateUser() {
        User user = getUser();
        User createdUser = userController.save(user);
        User updatedUser = User.builder()
                .email(UPDATED_EMAIL_COM)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .address(ADDRESS)
                .phoneNumber(PHONE_NUMBER)
                .build();
        User result = userController.update(updatedUser, createdUser.id());
        assertEquals(result.email(), UPDATED_EMAIL_COM);
    }

    @Test
    void deleteUser() {
        User user = getUser();
        User createdUser = userController.save(user);

        userController.delete(createdUser.id());
        assertTrue(userRepository.findById(createdUser.id()).isEmpty());
    }

    @Test
    void deleteAllUsers() {
        User user = getUser();
        User createdUser = userController.save(user);

        userController.deleteAll();
        assertTrue(userRepository.findById(createdUser.id()).isEmpty());
    }

    @Test
    void findOne() {
        User user = getUser();
        User createdUser = userController.save(user);

        User foundUser = userController.findOne(createdUser.id());
        assertEquals(foundUser.firstName(), createdUser.firstName());
        assertEquals(foundUser.lastName(), createdUser.lastName());
        assertEquals(foundUser.email(), createdUser.email());
        assertEquals(foundUser.address(), createdUser.address());
        assertEquals(foundUser.phoneNumber(), createdUser.phoneNumber());
    }

    @Test
    void findAll() {
        User user = getUser();
        User createdUser = userController.save(user);
        User createdUserOutOfRange = User.builder()
                .email(UPDATED_EMAIL_COM)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .address(ADDRESS)
                .phoneNumber(PHONE_NUMBER)
                .birthDate(Date.valueOf("1999-09-02"))
                .build();

        String from = "2000-01-01";
        String to = "2002-01-01";
        List<User> users = userController.all(from, to);
        assertTrue(users.stream().anyMatch(u -> Objects.equals(u.id(), createdUser.id())));
        assertTrue(users.stream().noneMatch(u -> Objects.equals(u.id(), createdUserOutOfRange.id())));

    }

    private static User getUser() {
        return User.builder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .address(ADDRESS)
                .phoneNumber(PHONE_NUMBER)
                .birthDate(BIRTH_DATE)
                .build();
    }
}
