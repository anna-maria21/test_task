package com.example.demo;

import com.example.demo.Entities.User;
import com.example.demo.exceptions.DatesAreNullException;
import com.example.demo.exceptions.DatesAreWrongException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserService userService;


    @Test
    void testFindUsersBetweenDates() {

        List<User> users = new ArrayList<>();
        LocalDate birthDateUser1 = LocalDate.of(2000,5,10);
        LocalDate birthDateUser2 = LocalDate.of(1999,5,10);
        User user1 = new User(1L, "ertyu@erty.com", "Qwerty", "QWerty", Date.from(birthDateUser1.atStartOfDay(ZoneId.systemDefault()).toInstant()), "", "");
        User user2 = new User(1L, "ertyasdgsfu@erty.com", "Qwerdty", "QWerdty", Date.from(birthDateUser2.atStartOfDay(ZoneId.systemDefault()).toInstant()), "", "");
        users.add(user1);
        users.add(user2);
        LocalDate from = LocalDate.of(2000,1,1);
        LocalDate to = LocalDate.of(2002,1,1);

        // Mock repository behavior
        when(repository
                .findByBirthDateBetween(Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant())))
                .thenReturn(users);
        List<User> result = userService.findUsersBetweenDates(Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        assert result.contains(user1);
        assert !result.contains(user2);
    }

    @Test
    void testFindUsersBetweenDatesIfDatesAreNull() {
        assertThrows(DatesAreNullException.class, () -> userService.findUsersBetweenDates(null, null));
    }

    @Test
    void testFindUsersBetweenDatesIfDatesAreWrong() {
        LocalDate from = LocalDate.of(2000,1,1);
        LocalDate to = LocalDate.of(2002,1,1);
        assertThrows(DatesAreWrongException.class, () -> userService.findUsersBetweenDates(Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant())));
    }

}
