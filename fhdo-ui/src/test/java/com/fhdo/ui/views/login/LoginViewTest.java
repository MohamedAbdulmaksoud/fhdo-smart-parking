package com.fhdo.ui.views.login;

import com.fhdo.ui.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoginViewTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginView loginView;

    @Test
    public void testSuccessfulLogin() throws Exception {
        String email = "john@example.com";
        String password = "password123";

        Mockito.doNothing().when(userService).authenticate(email, password);

        mockMvc = MockMvcBuilders.standaloneSetup(loginView).build();

        mockMvc.perform(post("/login")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful!"));
    }

    @Test
    public void testFailedLogin() throws Exception {
        String email = "wrong@example.com";
        String password = "wrongpassword";

        Mockito.doThrow(new RuntimeException("Invalid credentials"))
                .when(userService).authenticate(email, password);

        mockMvc = MockMvcBuilders.standaloneSetup(loginView).build();

        mockMvc.perform(post("/login")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Invalid credentials"));
    }

}