package com.SpringSecurityDatabase.SpringSecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class Usercontroller {

    @GetMapping("/hello")
    public String sayHello(){
        return "hello";
    }
}
