package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class AppConfig {
    @Bean
    public OrderService getOrder(){
        return new OrderService();
    }
    @Bean
    public OrderService getOrder2(){
        return new OrderService();
    }
}

/*
suppose if scope is like @Scope("singleton")

in AppConfig
we have
 @Bean
    public OrderService getOrder(){
        return new OrderService();
    }
    @Bean
    public OrderService getOrder2(){
        return new OrderService();
    }

    they both are a singleton,
    but two different beans will be created
    for these two bean definitions
    it will print OrderService created two times...

    singleton does not means that only one bean will exist
    it means one bean per defintion will exist.

 */
