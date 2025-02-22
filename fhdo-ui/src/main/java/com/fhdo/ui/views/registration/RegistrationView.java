package com.fhdo.ui.views.registration;

import com.fhdo.ui.dto.UserDto;
import com.fhdo.ui.service.UserService;
import com.fhdo.ui.util.NotificationUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

@Route("registration")
public class RegistrationView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private UserService userService;

    public RegistrationView() {
        TextField nameField = new TextField("Name");
        EmailField emailField = new EmailField("Email");
        PasswordField passwordField = new PasswordField("Password");
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");

        Button registerButton = new Button("Register", event -> {
            if (passwordField.getValue().equals(confirmPasswordField.getValue())) {
                registerUser(nameField.getValue(), emailField.getValue(), passwordField.getValue());
            } else {
                NotificationUtil.showErrorNotification("Passwords do not match");
            }
        });

        add(nameField, emailField, passwordField, confirmPasswordField, registerButton);
    }

    private void registerUser(String name, String email, String password) {
        try {
            UserDto user = new UserDto(name, email, password);
            userService.registerUser(user);
            NotificationUtil.showSuccessNotification("Registration successful!");
            getUI().ifPresent(ui -> ui.navigate("login"));
        } catch (RuntimeException e) {
            NotificationUtil.showErrorNotification(e.getMessage());
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Check if the user is already logged in
        String userEmail = (String) VaadinSession.getCurrent().getAttribute("userEmail");
        if (userEmail != null) {
            event.forwardTo("dashboard"); // Redirect to dashboard if logged in
        }
    }
}