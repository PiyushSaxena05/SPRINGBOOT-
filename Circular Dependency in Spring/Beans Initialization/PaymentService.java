package org.example;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Lazy
public class PaymentService {
    OrderService orderService;
public PaymentService(OrderService orderService){
    this.orderService = orderService;
   // System.out.println(" PaymentService created");
}
public void pay(){
    System.out.println("payment successful");
    orderService.getOrderDetails();
}

}
/*
By default, eager intialization
after initializing it to lazy
we need to call the particular object
if i will write @Scope("prototype")
instead of @Lazy still an object will not be created.


 */
