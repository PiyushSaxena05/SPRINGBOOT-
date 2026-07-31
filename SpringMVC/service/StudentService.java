package org.example.service;


import org.example.entity.Student;
import org.example.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student studentReq) {
       return  studentRepository.save(studentReq);
    }

    public Student getStudent(Long id) {
        return studentRepository.findbyId(id);
    }

    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }




}
