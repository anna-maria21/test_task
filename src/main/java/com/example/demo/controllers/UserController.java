package com.example.demo.controllers;

import com.example.demo.DTO.User;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping("")
    public List<User> all(@RequestParam(required = false) String from, @RequestParam(required = false) String to) {
        return service.findAll(from, to);
    }


    @GetMapping("/{id}")
    User findOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("")
    User save(@Valid @RequestBody User newUser) {
        return service.save(newUser);
    }


    @PutMapping("/{id}")
    User update(@Valid @RequestBody User newUser, @PathVariable Long id) {
        return service.update(newUser, id);
    }


    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        service.deleteById(id);
    }


    @DeleteMapping("")
    void deleteAll() {
        service.deleteAll();
    }


}
