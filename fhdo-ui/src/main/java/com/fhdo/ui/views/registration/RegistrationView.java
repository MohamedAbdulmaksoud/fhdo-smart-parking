package com.fhdo.ui.views.registration;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("registration")
public class RegistrationView extends VerticalLayout {
    public RegistrationView() {
        TextField nameField = new TextField("Name");
        EmailField emailField = new EmailField("Email");
        PasswordField passwordField = new PasswordField("Password");
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");

        Button registerButton = new Button("Register", event -> {
            if (passwordField.getValue().equals(confirmPasswordField.getValue())) {
                boolean success = mockRegistration(
                        nameField.getValue(),
                        emailField.getValue(),
                        passwordField.getValue()
                );
                if (success) {
                    Notification.show("Registration successful!", 3000, Notification.Position.MIDDLE);
                    getUI().ifPresent(ui -> ui.navigate("login"));
                } else {
                    Notification.show("Registration failed. Try again.", 3000, Notification.Position.MIDDLE);
                }
            } else {
                Notification.show("Passwords do not match", 3000, Notification.Position.MIDDLE);
            }
        });

        add(nameField, emailField, passwordField, confirmPasswordField, registerButton);
    }

    private boolean mockRegistration(String name, String email, String password) {
        // Mock logic for successful registration
        return email.contains("@") && !name.isEmpty() && !password.isEmpty();
    }
}
