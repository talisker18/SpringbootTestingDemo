package com.henz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.henz.student.Gender;
import com.henz.student.Student;
import com.henz.student.StudentRepository;

@SpringBootApplication
public class SpringbootTestingDemoApplication{
	
	@Autowired
	StudentRepository studentRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootTestingDemoApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		
//		Student s1 = new Student("joe4", "hjoel99@gmx.ch", Gender.MALE);
//		Student s2 = new Student("joel5", "hjoel10@0gmx.ch", Gender.MALE);
//		Student s3 = new Student("joel6", "hjoel11@gmx.ch", Gender.MALE);
//		Student s4 = new Student("joel6", "hjoel12@gmx.ch", Gender.MALE);
//		Student s5 = new Student("joel6", "hjoel13@gmx.ch", Gender.MALE);
//		Student s6 = new Student("joel6", "hjoel14@gmx.ch", Gender.MALE);
//		
//		studentRepository.save(s1);
//		studentRepository.save(s2);
//		studentRepository.save(s3);
//		studentRepository.save(s4);
//		studentRepository.save(s5);
//		studentRepository.save(s6);
//	}

}
