package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo {
    @GetMapping("hello")
    public String hello(){
        return "<h1>hello<h1>";
    }

    @GetMapping("nice")
    public String nice(){
        return "you are doing good";
    }
}
