package com.example.demo;

import com.example.demo.DTO.User;
import com.example.demo.Entities.DbUser;
import com.example.demo.converters.UserConverter;
import com.example.demo.converters.UserMapper;
import com.example.demo.exceptions.DatesAreNullException;
import com.example.demo.exceptions.DatesAreWrongException;
import com.example.demo.exceptions.DuplicateEmailException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    public static final long USER_ID = 1L;
    public static final long USER_ID_2 = 2L;
    public static final String EMAIL = "test@example.com";
    public static final String EMAIL_2 = "updated@example.com";
    @Mock
    UserMapper userConverter;
    @Mock
    UserRepository repository;
    @InjectMocks
    UserService userService;

    @Test
    void testFindUsersBetweenDates() {
        DbUser user1 = createDbUser();
        DbUser user2 = createSecondDbUser(USER_ID_2, EMAIL_2);
        List<DbUser> users = List.of(user1, user2);
        User userDto1 = createUser();
        User userDto2 = createSecondUser(USER_ID_2, EMAIL_2);

        LocalDate from = LocalDate.of(2000, 1, 1);
        LocalDate to = LocalDate.of(2002, 1, 1);

        when(repository
                .findByBirthDateBetween(Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant())))
                .thenReturn(users);
        when(userConverter.fromDbToDto(user1)).thenReturn(userDto1);
        when(userConverter.fromDbToDto(user2)).thenReturn(userDto2);

        List<User> result = userService.findUsersBetweenDates(Date.from(from.atStartOfDay(ZoneId.systemDefault())
                .toInstant()), Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Optional<User> firstUser = result.stream().filter(user -> Objects.equals(user.id(), userDto1.id())).findFirst();
        Optional<User> secondUser = result.stream().filter(user -> Objects.equals(user.id(), userDto2.id())).findFirst();

        assertTrue(firstUser.isPresent());
        assertTrue(secondUser.isPresent());
        assertEquals(firstUser.get(), userDto1);
        assertEquals(secondUser.get(), userDto2);
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
        DbUser updatedUser = createSecondDbUser(USER_ID_2, EMAIL_2);


        when(repository.findById(USER_ID)).thenReturn(Optional.of(existingUser));
        when(repository.save(updatedUser)).thenReturn(updatedUser);
        when(userConverter.fromDtoToDb(updatedUserDto)).thenReturn(updatedUser);
        when(userConverter.fromDbToDto(updatedUser)).thenReturn(updatedUserDto);

        User result = userService.update(updatedUserDto, USER_ID);

        verify(repository).findById(USER_ID);
        verify(repository).save(updatedUser);
        assertEquals(updatedUserDto, result);
    }


    @Test
    void testUpdateUserWithInvalidId() {
        User user = createUser();
        long userId = 3L;
        when(repository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.update(user, userId));
        verify(repository).findById(userId);
        verify(repository, never()).save(any(DbUser.class));
    }

    @Test
    void testFindById() {
        DbUser foundedUser = createDbUser();
        User foundUserDto = createUser();

        when(repository.findById(USER_ID)).thenReturn(Optional.of(foundedUser));
        when(userConverter.fromDbToDto(foundedUser)).thenReturn(foundUserDto);

        User result = userService.findById(USER_ID);

        verify(repository).findById(USER_ID);
        assertEquals(foundUserDto, result);
    }

    @Test
    void testFindAll() {
        DbUser user1 = createDbUser();
        DbUser user2 = createSecondDbUser(USER_ID_2, EMAIL_2);
        List<DbUser> users = List.of(user1, user2);
        User userDto1 = createUser();
        User userDto2 = createSecondUser(USER_ID_2, EMAIL_2);

        when(repository.findAll()).thenReturn(users);
        when(userConverter.fromDbToDto(user1)).thenReturn(userDto1);
        when(userConverter.fromDbToDto(user2)).thenReturn(userDto2);
        List<User> result = userService.findAll(null, null);


        Optional<User> firstUser = result.stream().filter(user -> Objects.equals(user.id(), userDto1.id())).findFirst();
        Optional<User> secondUser = result.stream().filter(user -> Objects.equals(user.id(), userDto2.id())).findFirst();

        assertTrue(firstUser.isPresent());
        assertTrue(secondUser.isPresent());
        assertEquals(firstUser.get(), userDto1);
        assertEquals(secondUser.get(), userDto2);
    }


    @Test
    void testDeleteById() {
        userService.deleteById(USER_ID);

        verify(repository).deleteById(USER_ID);
    }

    @Test
    void testDeleteAll() {
        userService.deleteAll();

        verify(repository).deleteAll();
    }

    @Test
    void testSaveUser() {
        DbUser newUser = createDbUser();
        User user = createUser();
        when(repository.save(newUser)).thenReturn(newUser);
        when(userConverter.fromDtoToDb(user)).thenReturn(newUser);
        when(userConverter.fromDbToDto(newUser)).thenReturn(user);

        User savedUser = userService.save(user);

        verify(repository).save(newUser);
        assertEquals(user, savedUser);
    }

    @Test
    void testSaveUserWithAlreadyExistingEmail() {
        User user = createUser();
        DbUser dbUser = createDbUser();
        when(repository.findByEmail(user.email())).thenReturn(Optional.of(dbUser));

        assertThrows(DuplicateEmailException.class, ()-> userService.save(user));
    }

    private static User createUser() {
        return createSecondUser(USER_ID, EMAIL);
    }

    private static User createSecondUser(long id, String email) {
        return User.builder()
                .id(id)
                .email(email)
                .firstName("John")
                .lastName("Doe")
                .address("Address 1")
                .phoneNumber("+380999999999")
                .build();
    }

    private static DbUser createDbUser() {
        return createSecondDbUser(USER_ID, EMAIL);
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

}
