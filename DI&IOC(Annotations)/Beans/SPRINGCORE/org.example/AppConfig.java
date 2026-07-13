package org.example;

import org.example.payment.CardPayment;
import org.example.payment.PaymentService;
import org.example.payment.UpiPayment;
import org.example.payment.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.example")
public class AppConfig {
    @Bean
    public User createUser(){
return new User("Jack", 27);
    }

    @Bean
    public CartService createCartService(){
        return new CartService();
    }

//    @Bean
    //@Qualifier
//    public PaymentService createCardPayment(){
//        return new CartPayment();
//    } this is used when we don't wanna use @Component
//@Bean
    //@Qualifier
//public PaymentService createUPIPayment(){
//        return new UpiPayment();
//   }

   // public OrderService createOrderService(@Qualifier("createUpiPayment")PaymentService paymentService){
//        return new OrderService(paymentService);
//    }
//    @Bean
//    public OrderService createOrderService(PaymentService paymentService){
//        return new OrderService(paymentService);
//    }
}


/*
ComponentScan means to check that which all
classes contain component annotation
which will be handled by spring.

ComponentScan will scan in this package
named as org.example and also within its sub-packages.
If I want to write @ComponentScan
instead of @ComponentScan("org.example")
then it's fine, and by default, it will search in that package in
which that class is being made.


@Bean tell the spring framework to call the methods under this annotation
while reading the rues of appconfig and an object will be returned
that have to be stored in the IOC container.




 */
