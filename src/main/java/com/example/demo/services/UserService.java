package com.example.demo.services;

import com.example.demo.Entities.User;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserService {

    private final UserRepository repository;
    private final ValidatorService validator;

    public UserService(UserRepository repository){
        this.repository = repository;
        this.validator = new ValidatorService();
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public User save(User newUser) {
        return repository.save(newUser);
    }

    @Value("${user.minAge}")
    private int minAge;

    public boolean validateBirthDate(Date birthDate) {
        LocalDate today = LocalDate.now();
        LocalDate birthDateLocal = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(birthDateLocal, today);
        return period.getYears() >= minAge;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public User updateUser(User newUser, Long id) {
        return repository.findById(id)
                .map(user -> {
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    user.setEmail(newUser.getEmail());
                    user.setBirthDate(newUser.getBirthDate());
                    user.setAddress(newUser.getAddress());
                    user.setPhoneNumber(newUser.getPhoneNumber());
                    return repository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id));

    }

    public ResponseEntity<?> getResponseEntity(@RequestBody @Valid User newUser, BindingResult result) {
        if (!validateBirthDate(newUser.getBirthDate())) {
            result.rejectValue("birthDate", "invalid", "User must be at least " + minAge + " years old");
        }
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        newUser.setFirstName(newUser.capitalize(newUser.getFirstName()));
        newUser.setLastName(newUser.capitalize(newUser.getLastName()));
        return null;
    }

    public ResponseEntity<Object> findUsersBetweenDates(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return ResponseEntity.badRequest().body("Date1 and Date2 cannot be null");
        }

        if (date1.after(date2)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "{\"date1\": " + date1 + "," + "\"date2\": " + date2 + "," +
                            "\"error\": \"Date1 must be before Date2\"}"
            );
        }

        List<User> users = repository.findByBirthDateBetween(date1, date2);
        return ResponseEntity.ok(users);
    }
}
