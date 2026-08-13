package com.example.SpringSecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @GetMapping
    public ResponseEntity<String>getStudents(){
        return ResponseEntity.ok("Students retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<String>createStudent(){
        return ResponseEntity.ok("Students created successfully");
    }

    @DeleteMapping
    public ResponseEntity<String>deleteStudent(){
        return ResponseEntity.ok("Students deleted successfully");
        
        
    }

}
