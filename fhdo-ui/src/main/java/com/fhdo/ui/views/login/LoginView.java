package com.fhdo.ui.views.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginView extends VerticalLayout {

    public LoginView() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        // Email Field
        EmailField emailField = new EmailField("Email");
        emailField.setPlaceholder("Enter your email");

        // Password Field (Initially Hidden)
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setPlaceholder("Enter your password");
        passwordField.setVisible(false);

        // Button
        Button nextButton = new Button("Next");

        nextButton.addClickListener(event -> {
            if (!isValidEmail(emailField.getValue())) {
                Notification.show("Email not found. Redirecting to registration...", 3000, Notification.Position.MIDDLE);
                getUI().ifPresent(ui -> ui.navigate("registration"));
            } else if (!passwordField.isVisible()) {
                passwordField.setVisible(true);
                nextButton.setText("Login");
            } else {
                authenticate(emailField.getValue(), passwordField.getValue());
            }
        });

        add(emailField, passwordField, nextButton);
    }

    private boolean isValidEmail(String email) {
        // Mock logic to check email
        return "user@example.com".equalsIgnoreCase(email);
    }

    private void authenticate(String email, String password) {
        // Mock authentication logic
        if ("password123".equals(password)) {
            Notification.show("Login successful!", 3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate("dashboard"));
        } else {
            Notification.show("Invalid password. Please try again.", 3000, Notification.Position.MIDDLE);
        }
    }
}
