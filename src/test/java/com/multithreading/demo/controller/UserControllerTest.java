package com.multithreading.demo.controller;

import com.multithreading.demo.entity.User;
import com.multithreading.demo.service.UserServiceImpl;
import com.multithreading.demo.utilities.Genders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserServiceImpl userService;
    User user1;
    User user2;
    @BeforeEach
    void initialize(){
        user1=new User();
        user1.setId(1);
        user1.setFirstName("Mildred");
        user1.setLastName("Seiler");
        user1.setEmail("mseiler0@amazon.de");
        user1.setGender(Genders.FEMALE);
        user2=new User();
        user2.setId(2);
        user2.setFirstName("Thorny");
        user2.setLastName("Chadburn");
        user2.setEmail("tchadburn1@kickstarter.com");
        user2.setGender(Genders.MALE);
    }
    @Test
    void getUser1_test() throws Exception {
        System.out.println(user1+" "+user2);
        when(userService.getAllUsers(anyInt(),anyInt(),anyString(),anyString()))
                .thenReturn(CompletableFuture.completedFuture(new PageImpl<>(List.of(user1,user2), PageRequest.of(0,10,Sort.by("id").ascending()),2)));
        RequestBuilder requestBuilder= MockMvcRequestBuilders
                .get("/v1/users/list")
                .param("pageNo","0")
                .param("pageSize","10")
                .param("sortBy","id")
                .param("sortDir","asc")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
                //.andExpect(jsonPath("$[0].id",Matchers.equalTo(1)));
        verify(userService,times(1)).getAllUsers(anyInt(),anyInt(),anyString(),anyString());
    }
}
