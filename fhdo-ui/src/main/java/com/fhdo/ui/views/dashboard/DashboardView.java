package com.fhdo.ui.views.dashboard;

import com.fhdo.ui.layout.MainLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String userEmail = (String) VaadinSession.getCurrent().getAttribute("userEmail");
        if (userEmail == null) {
            event.forwardTo("login"); // Redirect to login view
        } else {
            add(new Span("Welcome, " + userEmail));
        }
    }
}
