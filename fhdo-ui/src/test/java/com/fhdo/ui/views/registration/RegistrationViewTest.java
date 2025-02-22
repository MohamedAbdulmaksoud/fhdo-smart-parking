package com.fhdo.ui.views.registration;

import com.fhdo.ui.dto.UserDto;
import com.fhdo.ui.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RegistrationViewTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Test
    public void testSuccessfulRegistration() throws Exception {
        String name = "John Doe";
        String email = "john@example.com";
        String password = "password123";

        Mockito.doNothing().when(userService).registerUser(new UserDto(name, email, password));

        mockMvc.perform(post("/registration")
                        .param("name", name)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string("Registration successful!"));
    }

    @Test
    public void testFailedRegistration() throws Exception {
        String name = "John Doe";
        String email = "john@example.com";
        String password = "password123";

        Mockito.doThrow(new RuntimeException("User already exists"))
                .when(userService).registerUser(new UserDto(name, email, password));

        mockMvc.perform(post("/registration")
                        .param("name", name)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("User already exists"));
    }
}