package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        OrderService order = context.getBean(OrderService.class);
        order.placeOrder();
        }
    }

    /*

    Bean Definition:
    beanName : orderService
    beanClass : OrderService
    scope: singleton
    lazy : false
    dependency: paymentService

    IOC container will collect bean definitions
    and then will make objects

    IOC CONTAINER => READ CONFIGURATION => READ BEAN DEFINITION => Instantiate Objects => Dependencies are injected


     */
