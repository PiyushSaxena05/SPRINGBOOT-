package org.example;

import org.example.Notification.EmailService;
import org.example.Notification.NotificationService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        NotificationService n = new EmailService();

        Order order = new Order();
        order.setN(n);
        order.order();
        }
    }
/*
A class should ask what it needs
and not build everything itself

dependency injection can be done without springframework
using constructors
using getters and setters
field injection


/*
IOC (INVERSION OF CONTROL)
IOC is a idea or principle
dependency injection (DI) is approach/technique to achieve IOC

spring framework consist of IOC container

1)creates objects
2)manage objects
3) connects objects together

In java code we talk about beans
SPRING IOC container call it BEANS
every bean is an object
but, not every object is an bean
bean is managed by spring ioc
 */





