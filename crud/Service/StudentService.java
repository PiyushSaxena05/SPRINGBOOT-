package com.springcrud.crud.Service;

import com.springcrud.crud.Repository.StudentRepository;
import com.springcrud.crud.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private StudentRepository studentRepository;
    public StudentService(StudentRepository studentRepository){
        this.studentRepository=studentRepository;
    }

    public Student createStudent(Student studentReq){
        Student studentResp = studentRepository.save(studentReq);
        return studentResp;
    }

    public Student getStudent(Long id){
        Optional<Student>studentResp = studentRepository.findById(id);
        if(studentResp.isPresent()){
            return studentResp.get();
        }
       // System.out.println(studentResp);
        return null;
    }

    /*
    public Student getStudent(Long id){
    return studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));
}
     */

    public List<Student> getAllStudent(){
        List<Student>ls = studentRepository.findAll();
        return ls;
    }

    public Student updateStudent(Long id, Student studentreq) {
        Optional<Student> exisiting = studentRepository.findById(id);

        if (exisiting.isEmpty()) {
            return null;
        }

        Student studentTosave = exisiting.get();

        studentTosave.setName(studentreq.getName());
        studentTosave.setAge(studentreq.getAge());
        studentTosave.setEmail(studentreq.getEmail());
        studentTosave.setSubject(studentreq.getSubject());
        studentTosave.setRollNo(studentreq.getRollNo());

        return studentRepository.save(studentTosave);
    }

        public Boolean deleteStudent (Long id){
            Boolean isStudent = studentRepository.existsById(id);

            if (!isStudent) return false;
            studentRepository.deleteById(id);
            return true;

    }



}
