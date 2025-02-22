package com.fhdo.ui.layout;

import com.fhdo.ui.util.NotificationUtil;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinSession;

public class MainLayout extends VerticalLayout implements RouterLayout {

    public MainLayout() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setSpacing(true);
        header.getStyle().set("padding", "10px").set("border-bottom", "1px solid #e0e0e0");

        Image logo = new Image("images/logo.webp", "Smart Parking Logo");
        logo.setHeight("40px");

        Div spacer = new Div();
        spacer.setWidthFull();

        HorizontalLayout rightContent = new HorizontalLayout();

        if (VaadinSession.getCurrent().getAttribute("userEmail") != null) {
            String email = (String) VaadinSession.getCurrent().getAttribute("userEmail");
            Avatar userAvatar = new Avatar(email);
            userAvatar.setHeight("40px");
            userAvatar.setWidth("40px");

            ContextMenu userMenu = new ContextMenu(userAvatar);
            userMenu.setOpenOnClick(true);
            userMenu.addItem("Log out", event -> logoutUser());

            rightContent.add(userAvatar);
        } else {
            Button loginButton = new Button("Login", e -> navigateToLogin());
            Button signupButton = new Button("Sign Up", e -> navigateToRegistration());
            signupButton.getStyle().set("background-color", "#0070f3").set("color", "white");

            rightContent.add(loginButton, signupButton);
        }

        header.add(logo, spacer, rightContent);
        header.setAlignItems(Alignment.CENTER);
        add(header);
    }

    private void navigateToLogin() {
        getUI().ifPresent(ui -> ui.navigate("login"));
    }

    private void navigateToRegistration() {
        getUI().ifPresent(ui -> ui.navigate("registration"));
    }

    private void logoutUser() {
        VaadinSession.getCurrent().close();
        NotificationUtil.showSuccessNotification("Logged out successfully");
        getUI().ifPresent(ui -> ui.navigate(""));
    }
}
