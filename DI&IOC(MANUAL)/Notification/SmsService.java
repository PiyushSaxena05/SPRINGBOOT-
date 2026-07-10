package org.example.Notification;

public class SmsService implements NotificationService{
    @Override
    public void notification() {
        System.out.println("SMS SERVICE");
    }
}
