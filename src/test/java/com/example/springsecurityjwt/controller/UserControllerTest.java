package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.dto.SignUpRequest;
import com.example.springsecurityjwt.model.Role;
import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.service.AuthenticationService;
import com.example.springsecurityjwt.service.UserService;
import com.example.springsecurityjwt.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql("classpath:/initDB.sql")
public class UserControllerTest {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        userService.create(new User(1L, "Vadim", "vadim@gmail.com", "Vadim", Role.ADMIN));
        userService.create(new User(2L, "Rasul", "rasul@gmail.com", "rasul", Role.USER));
    }

    @Test
    void testGetUser() throws Exception {

        User user = userService.get(1);

        String token = jwtUtil.generateToken(user);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        mockMvc.perform(get("/users")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .with(authentication(authToken))
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("vadim@gmail.com"));

        verify(userService.userDetailsService(), times(1)).loadUserByUsername(user.getEmail());
    }

    @Test
    void testGetUserNotFound() throws Exception {

        mockMvc.perform(get("/users")
                        .contentType("application/json")
                        .content("{\"email\": \"unknown\"}"))
                .andExpect(status().isNotFound());

        verify(userService.userDetailsService(), times(1)).loadUserByUsername("unknown");
    }

    @Test
    void testSignUp() throws Exception {

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setName("Test User");
        signUpRequest.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");

        when(authenticationService.signup(any(SignUpRequest.class))).thenReturn(user);

        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"name\": \"Test User\", \"password\": \"password\"}"))
                .andExpect(status().isOk());

//        ResponseEntity<User> response = userController.singUp(signUpRequest);
//        assertEquals(user, response.getBody());
    }
}
