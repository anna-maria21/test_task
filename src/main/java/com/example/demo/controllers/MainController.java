package com.example.demo.controllers;

import com.example.demo.DTO.DatesDTO;
import com.example.demo.Entities.User;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainController {

    private final UserService service;

    MainController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    List<User> all() {
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    User findOne(@PathVariable Long id) {
        return service.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PostMapping("/find")
    ResponseEntity<Object> findBetweenDates(@RequestBody DatesDTO datesDTO) {
        return service.findUsersBetweenDates(datesDTO.date1, datesDTO.date2);
    }

    @PostMapping("/create")
    public ResponseEntity<?> save(@Valid @RequestBody User newUser, BindingResult result) {
        ResponseEntity<?> errors = service.getResponseEntity(newUser, result);
        if (errors != null) return errors;
        service.save(newUser);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("User created successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody User newUser, @PathVariable Long id, BindingResult result) {
        ResponseEntity<?> errors = service.getResponseEntity(newUser, result);
        if (errors != null) return errors;
        service.updateUser(newUser, id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("User updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("User deleted successfully");
    }

    @DeleteMapping("/delete/all")
    ResponseEntity<?> deleteEmployee() {
        service.deleteAll();
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("All users deleted successfully");
    }


}
