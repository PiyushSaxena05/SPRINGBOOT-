package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentGateway {
    @Value("${paymentGateway.type}")
    private String type;

    //    @Value("${paymentGateway.type:Razorpay}") this is used when no value is being assigned
    // it will automatically take Razorpay and will print it.
    @Value("${paymentGateway.retry-count}")
    private int retryCount;

//    public PaymentGateway(@Value("${paymentGateway.retry-count}") int retryCount, @Value("${paymentGateway.type}") String type) {
//        this.retryCount = retryCount;
//        this.type = type;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }


}
