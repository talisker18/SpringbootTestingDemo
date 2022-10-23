package com.henz.student;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.never;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;

import com.henz.student.exception.BadRequestException;

/**
 * this is a Unit test
 * 
 * */

@ExtendWith(MockitoExtension.class) //instead of using AutoCloseable
class StudentServiceTest {
	
	//here we do not have to test repository again. so we do not use @Autowired here. Instead we mock it
	//if we have tested something already, we can use Mocking
	
	@Mock
	private StudentRepository studentRepository; //advantage: much faster than using real DB
	
	//private AutoCloseable autoCloseable;
	
	private StudentService studentService;
	
	@BeforeEach
	void setUp() {
		//this.autoCloseable = MockitoAnnotations.openMocks(this);
		studentService = new StudentService(studentRepository);
	}
	
//	@AfterEach
//	void tearDown() throws Exception {
//		this.autoCloseable.close();
//	}

	@Test
	void testGetAllStudents() {
		//when
		studentService.getAllStudents(); //this is called 'interaction with mock'. Mockito captures the result of this List and on next line, mockito verifies if same was returned
		
		//then
		Mockito.verify(studentRepository).findAll(); //check whether 'when' part does the same like the mock. If you use e.g. 'deleteAll' here, the test will fail
	}

	@Test
	void testAddStudent() {
		//given
		String email = "test2@test.ch";
		Student s = new Student("joel", email, Gender.MALE);
		
		//when
		studentService.addStudent(s); //to capture the argument 's' in mockito, we use ArgumentCaptor on the next lines
		
		//then
		ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
		Mockito.verify(studentRepository).save(studentArgumentCaptor.capture()); //capture the argument 's' here and verify
		
		Student captured =  studentArgumentCaptor.getValue();
		Assertions.assertThat(captured).isEqualTo(s);
	}
	
	@Test
	void throwExceptionWhenEmailAlreadyExists() {
		//given
		String email = "test2@test.ch";
		Student s = new Student("joel", email, Gender.MALE);
		
		BDDMockito.given(studentRepository.selectExistsEmail(s.getEmail())) //we can also use 'anyString' method. Important is that we tell the mock that the email we are looking for already exists
			.willReturn(true); //mock the saving of 's' by saying that selectExistsEmail returns true
		
		//when
		//then
		Assertions.assertThatThrownBy(() -> studentService.addStudent(s))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("Email " + s.getEmail() + " taken");
		
		Mockito.verify(studentRepository, never()).save(ArgumentMatchers.any()); //verify that line with 'studentRepository.save(student);' of StudentService is not called in this test. Test will pass if it is NOT called
	}

	@Test
	void testDeleteStudent() {
	}

}
