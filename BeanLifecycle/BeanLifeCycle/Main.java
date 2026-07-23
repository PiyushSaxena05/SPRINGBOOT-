package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        OrderService order = context.getBean(OrderService.class);
        order.placeOrder();

        UserService obj = new UserService();
        obj.setBeanName("bean2");


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

    IOC CONTAINER => READ CONFIGURATION => READ BEAN DEFINITION => Instantiate Objects => Dependencies are injected => Aware Interfaces
    => Initialization callbacks => Bean is ready to use => Destruction callbacks => Bean is destroyed



UserService obj = new UserService();
        obj.setBeanName("bean2");
        in this case we have called this
        setBean method so it will print what is passed
        but Bean name will remain same
        that will be mentioned in @Component(" ")

        eg : - @Component("Bean1")
        so, original name will remain Bean1

     */
