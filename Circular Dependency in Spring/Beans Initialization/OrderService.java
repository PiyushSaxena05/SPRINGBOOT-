package org.example;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
//@Lazy
public class OrderService {
    PaymentService paymentService;
    public OrderService(@Lazy PaymentService paymentService){
      this.paymentService = paymentService;
        System.out.println("OrderService created");
  }

  public void placeOrder(){
        paymentService.pay();
      System.out.println("Order placed");
  }
  public void getOrderDetails(){
      System.out.println("Order Details");
  }

//    public OrderService(){
//        System.out.println("OrderService created");
//    }

    /*
    in this case orderservice will use proxy which will be
    a fake payment service object which will act like a
    payment service that is what is proxy.
     public OrderService(@Lazy PaymentService paymentService){
      this.paymentService = paymentService;
        System.out.println("OrderService created");
  }

  in Application.properties
  if u will set
  spring.main.lazy-initialization = true;
  (by default it's value is false)

  all classes will be lazy
  to avoid this u can do
  @Lazy("false")


     */


}
