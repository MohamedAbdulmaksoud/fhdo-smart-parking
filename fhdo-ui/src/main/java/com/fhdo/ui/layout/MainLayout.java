package com.fhdo.ui.layout;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;

public class MainLayout extends VerticalLayout implements RouterLayout {

    private boolean isUserLoggedIn = false; // Replace with actual login check
    private String loggedInUserName = "John Doe"; // Replace with actual logged-in user's name

    public MainLayout() {
        // Header Layout
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setSpacing(true);
        header.getStyle().set("padding", "10px").set("border-bottom", "1px solid #e0e0e0");

        // Logo
        Image logo = new Image("images/logo.webp", "Smart Parking Logo");
        logo.setHeight("40px");

        // Spacer to push buttons to the right
        Div spacer = new Div();
        spacer.setWidthFull();

        // Right-side content (buttons or user menu)
        HorizontalLayout rightContent = new HorizontalLayout();
        if (isUserLoggedIn) {
            // Logged-in user menu
            Avatar userAvatar = new Avatar(loggedInUserName);
            userAvatar.setHeight("40px");
            userAvatar.setWidth("40px");

            ContextMenu userMenu = new ContextMenu(userAvatar);
            userMenu.setOpenOnClick(true);
            userMenu.addItem("Settings", event -> navigateToSettings());
            userMenu.addItem("Log out", event -> logoutUser());

            Span userName = new Span(loggedInUserName);
            userName.getStyle().set("font-size", "1em").set("margin-right", "10px");

            rightContent.add(userName, userAvatar);
        } else {
            // Login and Sign Up buttons
            Button loginButton = new Button("Login", e -> navigateToLogin());
            Button signupButton = new Button("Sign Up", e -> navigateToRegistration());
            signupButton.getStyle().set("background-color", "#0070f3").set("color", "white");

            rightContent.add(loginButton, signupButton);
        }

        header.add(logo, spacer, rightContent);
        header.setAlignItems(Alignment.CENTER);

        // Add the header to the layout
        add(header);
    }

    private void navigateToLogin() {
        // Navigate to LoginView
        getUI().ifPresent(ui -> ui.navigate("login"));
    }

    private void navigateToRegistration() {
        // Navigate to RegistrationView
        getUI().ifPresent(ui -> ui.navigate("registration"));
    }

    private void navigateToSettings() {
        // Navigate to user settings view
        getUI().ifPresent(ui -> ui.navigate("settings"));
    }

    private void logoutUser() {
        // Handle logout logic
        isUserLoggedIn = false;
        Notification.show("Logged out successfully", 3000, Notification.Position.MIDDLE);
        getUI().ifPresent(ui -> ui.navigate(""));
    }
}
