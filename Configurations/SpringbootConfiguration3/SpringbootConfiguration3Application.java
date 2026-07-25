package com.example.SpringbootConfiguration3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootConfiguration3Application {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(SpringbootConfiguration3Application.class, args);

		OrderService orderService = context.getBean(OrderService.class);
		orderService.placeOrder();



	}
	@Bean
	public UserService getUserServiceBean(){
return new UserService();
	}

}

/*
@SpringBootApplication
This annotation consist
of more annotations

like:-
@SpringBootConfiguration = @Configuration
@EnableAutoConfiguration
This configuration means that
look at my project and create any beans
seem important to you...
@ComponentScan

@ConditionalOnClass("class_name")
this annotation means make bean if this class
exists in dependency.
@ConditionalOnMissingBean()
this annotation means bean will be created
if bean does not exists.




 */
