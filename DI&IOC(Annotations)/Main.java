package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        OrderService order = context.getBean(OrderService.class);
        order.placeOrder();

//        PaymentService pay = context.getBean(PaymentService.class);
//        pay.pay();


        }
    }
    /*
    In spring framework IOC container is known
    as ApplicationContext

    AnnotationConfigApplicationContext();
    it means we will be using annotations
    in a spring application.


ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
This means we are using IOC container with Annotation-based configuration,
and the rules for that configuration will be available in AppConfig class
     */
