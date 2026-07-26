package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.crypto.spec.PSource;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DemoApplication.class, args);

		PaymentGateway paymentGateway = context.getBean(PaymentGateway.class);
		//paymentGateway.setType("Paytm");
		//paymentGateway.setRetryCount(5);
		System.out.println(paymentGateway.getType());
		System.out.println(paymentGateway.getRetryCount());

	}
}


/*
Application.properties(non-java file)
It is known as configuration file.
we can give a key-value pair over here.

@Value is a annotation used for setting value.





 */
