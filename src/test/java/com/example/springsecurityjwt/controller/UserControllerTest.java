package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.dto.SignUpRequest;
import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.service.AuthenticationService;
import com.example.springsecurityjwt.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql("classpath:/initDB.sql")
@Sql("classpath:/populateDB.sql")
public class UserControllerTest {

    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private UserService userService;
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetUser() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        User user = new User();
        user.setName("testuser");

        UserDetails userDetails = mock(UserDetails.class);
        when(userService.userDetailsService()).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        mockMvc.perform(get("/users")
                        .contentType("application/json")
                        .content("{\"name\": \"testuser\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testuser"));

        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
    }

    @Test
    void testGetUserNotFound() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        when(userService.userDetailsService()).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(get("/users")
                        .contentType("application/json")
                        .content("{\"name\": \"unknown\"}"))
                .andExpect(status().isNotFound());

        verify(userDetailsService, times(1)).loadUserByUsername("unknown");
    }

    @Test
    void testSignUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

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

        ResponseEntity<User> response = userController.singUp(signUpRequest);
        assertEquals(user, response.getBody());
    }
}
