package com.example.demo;

import com.example.demo.DTO.User;
import com.example.demo.Entities.DbUser;
import com.example.demo.converters.UserConverter;
import com.example.demo.exceptions.DatesAreNullException;
import com.example.demo.exceptions.DatesAreWrongException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserConverter userConverter;
    @Mock
    UserRepository repository;
    @InjectMocks
    UserService userService;

    @Test
    void testFindUsersBetweenDates() {
        DbUser user1 = createDbUser();
        DbUser user2 = createSecondDbUser(2L, "updated@example.com");
        List<DbUser> users = List.of(user1, user2);
        LocalDate from = LocalDate.of(2000, 1, 1);
        LocalDate to = LocalDate.of(2002, 1, 1);

        when(repository
                .findByBirthDateBetween(Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant())))
                .thenReturn(users);

        List<User> result = userService.findUsersBetweenDates(Date.from(from.atStartOfDay(ZoneId.systemDefault())
                .toInstant()), Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

    @Test
    void testFindUsersBetweenDatesIfDatesAreNull() {
        assertThrows(DatesAreNullException.class, () -> userService.findUsersBetweenDates(null, null));
    }

    @Test
    void testFindUsersBetweenDatesIfDatesAreWrong() {
        LocalDate from = LocalDate.of(2000, 1, 1);
        LocalDate to = LocalDate.of(2002, 1, 1);
        assertThrows(DatesAreWrongException.class, () -> userService.findUsersBetweenDates(Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant())));
    }

    @Test
    void testUpdateUserWithValidId() {
        User updatedUserDto = createUser();

        DbUser existingUser = createDbUser();
        DbUser updatedUser = createSecondDbUser(2L, "updated@example.com");


        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(repository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.update(updatedUserDto, 1L);

        verify(repository).findById(1L);
        verify(repository).save(updatedUser);
        assertEquals(updatedUser, result);
    }

    private static DbUser createSecondDbUser(long id, String email) {
        return DbUser.builder()
                .id(id)
                .email(email)
                .firstName("John")
                .lastName("Doe")
                .address("Address 1")
                .phoneNumber("+380999999999")
                .birthDate(Date.from(LocalDate.of(2001, 5, 10).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();
    }

//    @Test
//    void testUpdateUserWithInvalidId() {
//        when(repository.findById(3L)).thenReturn(Optional.empty());
//        assertThrows(UserNotFoundException.class, () -> userService.update(new User(), 1L));
//        verify(repository).findById(3L);
//        verify(repository, never()).save(any(User.class));
//    }

    @Test
    void testFindById() {
        DbUser foundedUser = createDbUser();

        when(repository.findById(1L)).thenReturn(Optional.of(foundedUser));
        User result = userService.findById(1L);

        verify(repository).findById(1L);
        assertEquals(foundedUser, result);
    }

    @Test
    void testFindAll() {
        DbUser user1 = createDbUser();
        DbUser user2 = createSecondDbUser(2L, "updated@example.com");
        List<DbUser> users = List.of(user1, user2);

        when(repository.findAll()).thenReturn(users);
        List<User> result = userService.findAll(null, null);
        assert result.contains(user1);
        assert result.contains(user2);
    }


    @Test
    void testDeleteById() {
        Long userId = 1L;
        userService.deleteById(userId);

        verify(repository).deleteById(userId);
    }

    @Test
    void testDeleteAll() {
        userService.deleteAll();

        verify(repository).deleteAll();
//        assertEquals(userId, result);
    }

    @Test
    void testSaveUser() {
        DbUser newUser = createDbUser();
        User user = createUser();
        when(repository.save(newUser)).thenReturn(newUser);

        User savedUser = userService.save(user);

        verify(repository).save(newUser);
        assertEquals(user, savedUser);
    }

    private static User createUser() {
        return User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .address("Address 1")
                .phoneNumber("+380999999999")
                .build();
    }

    private static DbUser createDbUser() {
        return createSecondDbUser(1L, "test@example.com");
    }

}
