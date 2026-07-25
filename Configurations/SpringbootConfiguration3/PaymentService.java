package com.example.SpringbootConfiguration3;

import org.springframework.stereotype.Component;

@Component
public class PaymentService {


    public void pay(){
        System.out.println("Payment done ");
    }
}
