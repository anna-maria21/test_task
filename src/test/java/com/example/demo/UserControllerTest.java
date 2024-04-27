//package com.example.demo;
//
//import com.example.demo.controllers.UserController;
//import com.example.demo.services.UserService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith({SpringExtension.class, MockitoExtension.class})
//@WebMvcTest(UserController.class)
//class UserControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private UserService service;
//
//
//    @Test
//    public void givenEmployees_whenGetEmployees_thenReturnJsonArray()
//            throws Exception {
//        User user1 = new User(1L, "ertyu@erty.com", "Qwerty", "QWerty", null, "", "");
//
//        List<User> allUsers = List.of(user1);
//
//        given(service.findAll(null, null)).willReturn(allUsers);
//
//        mvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1));
//    }
//}
