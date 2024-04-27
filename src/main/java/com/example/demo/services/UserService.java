package com.example.demo.services;

import com.example.demo.DTO.User;
import com.example.demo.Entities.DbUser;
import com.example.demo.converters.UserConverter;
import com.example.demo.exceptions.DatesAreNullException;
import com.example.demo.exceptions.DatesAreWrongException;
import com.example.demo.exceptions.DuplicateEmailException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final UserConverter userConverter;

    @Value("${user.minAge}")
    private static int minAge;

    @SneakyThrows
    public List<User> findAll(String fromDate, String toDate) {
        if (fromDate != null & toDate != null) {
            Date from = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            Date to = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            return findUsersBetweenDates(from, to);
        }
        return repository.findAll()
                .stream()
                .map(userConverter::fromDbToDto)
                .toList();
    }

    public User findById(Long id) {
        return repository.findById(id)
                .map(userConverter::fromDbToDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User save(User newUser) {
//        repository.findByEmail(newUser.email())
//                .orElseThrow(() -> new DuplicateEmailException(newUser.email()));
        log.info("User saved successfully: {}", newUser);
        return userConverter.fromDbToDto(repository.save(userConverter.fromDtoToDb(newUser)));
    }

    public void deleteById(Long id) {
        log.info("User deleted successfully: {}", id);
        repository.deleteById(id);
    }

    public void deleteAll() {
        log.info("All users deleted successfully");
        repository.deleteAll();
    }

    public User update(User newUser, Long id) {
        DbUser existingUser = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        DbUser updatedUser = userConverter.fromDtoToDb(newUser);
        updatedUser.setId(existingUser.getId());

        log.info("User updated successfully: {}", updatedUser);
        return userConverter.fromDbToDto(repository.save(updatedUser));
    }

    public List<User> findUsersBetweenDates(Date from, Date to) {
        if (from == null || to == null) {
            throw new DatesAreNullException();
        }

        if (from.after(to)) {
            throw new DatesAreWrongException(from, to);
        }

        return repository.findByBirthDateBetween(from, to)
                .stream()
                .map(userConverter::fromDbToDto)
                .toList();
    }
}
