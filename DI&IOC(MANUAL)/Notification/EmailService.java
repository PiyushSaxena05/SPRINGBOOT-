package org.example.Notification;

public class EmailService implements NotificationService {
    @Override
    public void notification() {
        System.out.println("Email Sent");
    }
}
