package com.henz.student;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.henz.student.exception.BadRequestException;
import com.henz.student.exception.StudentNotFoundException;

import java.util.List;

@AllArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

  //for better integration testing, return here the new Student object
    public Student addStudent(Student student) {
        Boolean existsEmail = studentRepository
                .selectExistsEmail(student.getEmail());
        if (existsEmail) {
            throw new BadRequestException(
                    "Email " + student.getEmail() + " taken");
        }

        return studentRepository.save(student); //this returns the newly saved Student instance
    }
    
    public Student getStudent(Long studentId) {
        if(!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(
                    "Student with id " + studentId + " does not exists");
        }
        return studentRepository.findById(studentId).get();
    }

    public void deleteStudent(Long studentId) {
        if(!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(
                    "Student with id " + studentId + " does not exists");
        }
        studentRepository.deleteById(studentId);
    }
    
    public Student updateStudent(Long studentId, Student student) {
    	Student existingStudent = this.studentRepository.findById(studentId).orElse(null);
    	
    	existingStudent.setEmail(student.getEmail());
    	existingStudent.setName(student.getName());
    	existingStudent.setGender(student.getGender());
    	
    	return this.studentRepository.save(existingStudent);
    }
}
