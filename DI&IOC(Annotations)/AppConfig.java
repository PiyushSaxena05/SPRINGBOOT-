package org.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.example")
public class AppConfig {

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




 */
