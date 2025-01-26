package com.fhdo.ui.views.header;

import com.fhdo.ui.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
public class HeaderView extends VerticalLayout {

    public HeaderView() {
        // Header Layout
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setSpacing(true);

        Div spacer = new Div();
        spacer.setWidthFull();

        // Optional content (e.g., landing page description)
        addLandingPageContent();
    }

    private void addLandingPageContent() {
        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.CENTER);
        content.setWidthFull();

        Span welcomeMessage = new Span("Welcome to Smart Parking!");
        welcomeMessage.getStyle().set("font-size", "1.2em").set("font-weight", "bold");

        Span description = new Span(
                "Effortlessly find and reserve parking spaces with real-time availability and recommendations."
        );
        description.getStyle().set("font-size", "1em").set("color", "gray");

        content.add(welcomeMessage, description);
        add(content);
    }
}
