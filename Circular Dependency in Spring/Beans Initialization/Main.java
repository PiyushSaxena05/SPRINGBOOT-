package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

   OrderService order = context.getBean(OrderService.class);
//        PaymentService payment = context.getBean(PaymentService.class);
       // System.out.println("payment service not started yet");
           order.placeOrder();
        /*
        by default
        scope singleton = Eager intialization
        prototype ->lazy
         */

        }
    }
