package com.fhdo.ui.views.login;

import com.fhdo.ui.service.UserService;
import com.fhdo.ui.util.NotificationUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Route("login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private UserService userService;

    public LoginView() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        EmailField emailField = new EmailField("Email");
        emailField.setPlaceholder("Enter your email");

        PasswordField passwordField = new PasswordField("Password");
        passwordField.setPlaceholder("Enter your password");

        Button loginButton = new Button("Login", event -> authenticate(emailField.getValue(), passwordField.getValue()));

        add(emailField, passwordField, loginButton);
    }

    private void authenticate(String email, String password) {
        try {
            UUID userId = userService.authenticate(email, password);
            VaadinSession.getCurrent().setAttribute("userId", userId);
            VaadinSession.getCurrent().setAttribute("userEmail", email);

            NotificationUtil.showSuccessNotification("Login successful!");
            getUI().ifPresent(ui -> ui.navigate("dashboard"));
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
