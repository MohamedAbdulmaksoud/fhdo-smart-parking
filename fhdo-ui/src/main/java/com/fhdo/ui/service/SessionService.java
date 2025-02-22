package com.fhdo.ui.service;

import com.vaadin.flow.server.VaadinSession;

import java.util.UUID;

public class SessionService {

    public static UUID getLoggedInUserId() {
        return (UUID) VaadinSession.getCurrent().getAttribute("userId");
    }

    public static String getLoggedInUserEmail() {
        return (String) VaadinSession.getCurrent().getAttribute("userEmail");
    }

    public static void logout() {
        VaadinSession.getCurrent().close();
    }
}
