package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderService {
    @Autowired//field injection
    private PaymentService paymentService;

//    @Autowired
//    public OrderService( PaymentService paymentService){
//        this.paymentService = paymentService;
//    }
//    @Autowired
//    public void setPaymentService(PaymentService paymentService){
//        this.paymentService= paymentService;
//    }
    public void placeOrder(){
        paymentService.pay();
        System.out.println("Order placed");
    }

}

/*
if there is only one constructor, then
it can run without @Autowired Annotation.

Why constructor Injection is recommended??
1)Dependency gets wired at the time of object creation.
2)Final can be used
    private final PaymentService paymentService;
    it is used so that no one can change the
    dependency.
 3)Easy to test the class.






 */

