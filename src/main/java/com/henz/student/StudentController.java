package com.henz.student;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    //for better integration testing, return here the new Student object
    @PostMapping
    public Student addStudent(@Valid @RequestBody Student student) {
    	System.out.println("--------------post controller was called--------------");
        return studentService.addStudent(student);
    }
    
    @PutMapping(path = "/update/{studentId}")
    public Student updateStudent(@Valid @RequestBody Student student, @PathVariable("studentId") Long studentId) {
        return studentService.updateStudent(studentId,student);
    }
    
    @GetMapping(path = "{studentId}") //maps to http://localhost:<port>/api/v1/students/{id} --> slashes are added automatically
    public Student getStudentById(@PathVariable("studentId") Long studentId) {
        return studentService.getStudent(studentId);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long studentId) {
        studentService.deleteStudent(studentId);
    }
}
