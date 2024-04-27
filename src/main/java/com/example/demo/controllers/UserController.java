package com.example.demo.controllers;

import com.example.demo.DTO.User;
import com.example.demo.exceptions.ValidationErrorException;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;


    // ??????
    @GetMapping("")
    List<User> all(@RequestParam(required = false) String from, @RequestParam(required = false) String to) {
        return service.findAll(from, to);
    }


    @GetMapping("/{id}")
    User findOne(@PathVariable Long id) {
        return service.findById(id);
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
//    @PostMapping("/")
//    ResponseEntity<?> save(@Valid @RequestBody User newUser, BindingResult result) throws JsonProcessingException {
//        ResponseEntity<?> errors = service.getResponseEntity(newUser, result);
//        if (errors != null) return errors;
//        service.save(newUser);
//        ClientMessage msg = new ClientMessage("User created successfully");
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(objectMapper.writeValueAsString(msg));
//    }

    @PostMapping("")
    User save(@Valid @RequestBody User newUser) {
        return service.save(newUser);
    }


    @PutMapping("/{id}")
    User update(@Valid @RequestBody User newUser, @PathVariable Long id, BindingResult result) {
        if (result.hasErrors()) throw new ValidationErrorException(result);
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
