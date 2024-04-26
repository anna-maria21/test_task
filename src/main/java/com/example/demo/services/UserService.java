package com.example.demo.services;

import com.example.demo.Entities.User;
import com.example.demo.exceptions.DatesAreNullException;
import com.example.demo.exceptions.DatesAreWrongException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;


@Service
public class UserService {

    private final UserRepository repository;
    //круто, що юзаєш аноташку Value, мені таке подобажться, але я
    // не розумію звідки береться minAge? бо нема бачу application.properties - https://www.baeldung.com/properties-with-spring
    @Value("${user.minAge}")
    private int minAge;

    public UserService(UserRepository repository, @Value("${user.minAge}") int minAge) {
        this.repository = repository;
        this.minAge = minAge;
    }

    //замінити на ломбок - @AllArgsContructor на рівні класу *
//    public UserService(UserRepository repository) {
//        this.repository = repository;
//    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public User save(User newUser) {
        return repository.save(newUser);
    }


    //гарно також було б винести в аноташкку свою власну - https://www.baeldung.com/spring-mvc-custom-validator)) *?
    public boolean validateBirthDate(Date birthDate) {
        LocalDate today = LocalDate.now();
        LocalDate birthDateLocal = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(birthDateLocal, today);
        return period.getYears() >= minAge;
    }

    public Long deleteById(Long id) {
        repository.deleteById(id);
        return id;
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    // просто назва апдейт буде краща *
    public User update(User newUser, Long id) {
        // Мені подобається, що через опшинал і те що є власний ексепшн - це дуже гарно))) *
        return repository.findById(id)
                //в тебе тут повний, а не частковий апдейт -просто зберігай отриману ентітію
//                    user.setFirstName(newUser.getFirstName());
//                    user.setLastName(newUser.getLastName());
//                    user.setEmail(newUser.getEmail());
//                    user.setBirthDate(newUser.getBirthDate());
//                    user.setAddress(newUser.getAddress());
//                    user.setPhoneNumber(newUser.getPhoneNumber());
                .map(repository::save)
                .orElseThrow(() -> new UserNotFoundException(id));

    }

    public ResponseEntity<?> getResponseEntity(@RequestBody @Valid User newUser, BindingResult result) {
        if (!validateBirthDate(newUser.getBirthDate())) {
            result.rejectValue("birthDate", "invalid", "User must be at least " + minAge + " years old");
        }
        if (result.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result);
        }
        newUser.setFirstName(newUser.capitalize(newUser.getFirstName()));
        newUser.setLastName(newUser.capitalize(newUser.getLastName()));
        return null;
    }

    public List<User> findUsersBetweenDates(Date from, Date to) {
//        //краще кидати ексепшени власні і обробляти в контролер едвайс - ніж кастомізувати ResponseEntity *
        if (from == null || to == null) {
            throw new DatesAreNullException();
        }
//
//        //краще кидати ексепшени власні і обробляти в контролер едвайс - ніж кастомізувати ResponseEntity *
        if (from.after(to)) {
            throw new DatesAreWrongException(from, to);
        }

        List<User> users = repository.findByBirthDateBetween(from, to);
        return users;
    }
}
