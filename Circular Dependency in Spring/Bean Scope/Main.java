package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {


        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
       OrderService order = context.getBean(OrderService.class);
     OrderService order2 = context.getBean(OrderService.class);

        System.out.println(order==order2);//it will print true as both are same
        //object will be created once

        }
    }
    /*
    Bean Scopes:
    1) Singleton(By Default)(Eager initialization)
    if the scope of a bean is singleton, then one object will be created per bean definition
    u do  getBean() or dependency injection, but only one object will be created for that class
if the scope is singleton then u can make your own objects but spring will handle only one.
2) Prototype (Lazy initialization) - It will create a new object every time.
     */
