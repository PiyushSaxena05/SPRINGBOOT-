package org.example;

import org.example.Notification.EmailService;
import org.example.Notification.NotificationService;
import org.example.Notification.SmsService;

public class Order {
    NotificationService n ;
    public Order(NotificationService s){
        this.n = s;
    }
    public Order(){

    }

    public void setN(NotificationService n) {
        this.n = n;
    }

    public void order(){
       // NotificationService n = new SmsService();
        System.out.println("Order Placed");
        n.notification();
    }
}
