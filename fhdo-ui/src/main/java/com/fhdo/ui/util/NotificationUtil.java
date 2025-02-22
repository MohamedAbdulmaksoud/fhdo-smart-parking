package com.fhdo.ui.util;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class NotificationUtil {

    public static void showSuccessNotification(String message) {
        Notification notification = new Notification(message, 3000);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.addClassName("success-notification");
        notification.open();
    }

    public static void showInfoNotification(String message) {
        Notification.show(message, 3000, Notification.Position.TOP_CENTER);
    }


    public static void showErrorNotification(String message) {
        Notification notification = new Notification(message, 5000);
        notification.addClassName("error-notification");
        notification.open();
    }
}
