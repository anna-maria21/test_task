package com.example.demo.controllers;

import com.example.demo.DTO.ClientMessage;
import com.example.demo.DTO.DatesDTO;
import com.example.demo.Entities.User;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
//має бути названий UserController *
//гарна практика додати сюди @RequestMapping("/users") і ми матимемоцей шлях в усіх методах цього класу *
public class UserController {
    private final UserService service;
    private ObjectMapper objectMapper;

    //замінити на ломбок - @AllArgsContructor на рівні класу *
//    UserController(UserService service) {
//        this.service = service;
//    }

    //якщо додати це на рівні класу @RequestMapping("/users"), то це не треба повторювати
    @GetMapping("/")
    List<User> all(@RequestParam(required = false) DatesDTO datesDTO) {
        if (datesDTO.from != null) {
            return service.findUsersBetweenDates(datesDTO.from, datesDTO.to);
        }
        return service.findAll();
    }


    @GetMapping("/{id}")
    User findOne(@PathVariable Long id) {
        return service.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    //find назва не можу бути в ПОСТ ендпоінта - просто /users
    //обʼєднай з першим ендпоінтом і передавай це як опшинал not required request params *
    //І краще не називати date1 та date2 -> from та to значно ліпше і легше читається *
    //Object краще не повертати -> просто List<User> *
//    @PostMapping("/find")
//    ResponseEntity<Object> findBetweenDates(@RequestBody DatesDTO datesDTO) {
//        return service.findUsersBetweenDates(datesDTO.from, datesDTO.to);
//    }

    //ПОСТ і так містить в собі інформацію про створення - *
    //Хочеться більше консистентності - якщо до того повертали просто юзера то треба і тут(не треба ResponseEntity) + знаки питання складно читати
    //І ще хочеться консистентності в модифікаторах доступу - або всюди package,або всюди public *
    @PostMapping("/")
    ResponseEntity<?> save(@Valid @RequestBody User newUser, BindingResult result) throws JsonProcessingException {
        ResponseEntity<?> errors = service.getResponseEntity(newUser, result);
        if (errors != null) return errors;
        service.save(newUser);
        ClientMessage msg = new ClientMessage("User created successfully");
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(msg));
    }

    //ПУТ і так містить в собі інформацію про те що це апдейт - просто /users/{id}
    //Хочеться більше консистентності - якщо до того повертали просто юзера то треба і тут(не треба ResponseEntity) + знаки питання складно читати
    //І ще хочеться консистентності в модифікаторах доступу - або всюди package,або всюди public *
    @PutMapping("/{id}")
    ResponseEntity<?> update(@Valid @RequestBody User newUser, @PathVariable Long id, BindingResult result) {
        //я сходу не згадаю, але тут також можна красивше - я подивлюсь ввечері або до тебе завітаю))
        ResponseEntity<?> errors = service.getResponseEntity(newUser, result);
        if (errors != null) return errors;
        service.update(newUser, id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"message:\" \"User updated successfully\"}");
    }

    //ДЕЛІТ і так містить в собі інформацію про те що це видалення - просто /users/{id}
    //Не треба ResponseEntity - просто айдішка видаленої ентіті *
    @DeleteMapping("/{id}")
    Long deleteEmployee(@PathVariable Long id) {
        return service.deleteById(id);
    }

    //ДЕЛІТ і так містить в собі інформацію про те що це видалення - просто /users *
    //Не треба ResponseEntity - просто айдішка видаленої ентіті ?
    @DeleteMapping("/")
    ResponseEntity<?> deleteEmployee() {
        service.deleteAll();
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("All users deleted successfully");
    }


}
