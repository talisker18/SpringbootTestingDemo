package com.henz.student;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import com.henz.TestH2Repository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

	@LocalServerPort
	private int port;
	
	private String baseUrl = "http://localhost";
	private final String basePath = "api/v1/students";
	
	private static RestTemplate restTemplate; //or use RestTestTemplate
	
	@Autowired
	private TestH2Repository testH2Repository;
	
	@BeforeAll
	public static void init() {
		restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void setUp() {
		baseUrl = baseUrl.concat(":").concat(port+"/").concat(basePath);
	}
	
	@Test
	public void testAddStudent() {
		Student s1 = new Student("joel", "hjoel99@gmx.ch", Gender.MALE);
		//perform Rest call with rest template
		Student newStudent = restTemplate.postForObject(baseUrl, s1, Student.class);
		
		assertThat(newStudent.getName()).isEqualTo(s1.getName());
		
		//check if it was saved in database
		assertThat(testH2Repository.findAll().size()).isEqualTo(1);
	}
	
	@Test
	@Sql(statements = "INSERT INTO student (id,name,email,gender) VALUES (1,'joel','hjoel87@gmx.ch','MALE')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM student WHERE name = 'joel'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testGetAllStudents() {
		List<Student> students= restTemplate.getForObject(baseUrl, List.class);
		
		Assertions.assertEquals(students.size(), testH2Repository.findAll().size());
	}
	
	@Test
	@Sql(statements = "INSERT INTO student (id,name,email,gender) VALUES (10,'joel','hjoel87@gmx.ch','MALE')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM student WHERE name = 'joel'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testFindStudentById() {
		Student s = restTemplate.getForObject(baseUrl+"/{id}", Student.class, 10);
		
		Assertions.assertAll(
				() -> Assertions.assertNotNull(s),
				() -> Assertions.assertEquals(10, s.getId()),
				() -> Assertions.assertEquals("joel", s.getName())
				);
	}
	
	@Test
	@Sql(statements = "INSERT INTO student (id,name,email,gender) VALUES (10,'joel','hjoel87@gmx.ch','MALE')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM student WHERE name = 'joel'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testUpdateStudent() {
		
		Student update = new Student(10L,"joel update", "hjoel87@gmxUpdate.ch", Gender.MALE);
		
		restTemplate.put(baseUrl+"/update/{id}", update, 10L);
		
		Student studentFromDb = testH2Repository.findById(10L).get();
		
		Assertions.assertAll(
				() -> Assertions.assertNotNull(studentFromDb),
				() -> Assertions.assertEquals(10, studentFromDb.getId()),
				() -> Assertions.assertEquals("joel update", studentFromDb.getName())
				);
	}
	
	@Test
	@Sql(statements = "INSERT INTO student (id,name,email,gender) VALUES (8,'joel','hjoel87@gmx.ch','MALE')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void testDeleteStudent() {
		int recordCount = this.testH2Repository.findAll().size();
		
		Assertions.assertEquals(1, recordCount);
		restTemplate.delete(baseUrl+"/{id}", 8L);
		
		recordCount = this.testH2Repository.findAll().size();
		Assertions.assertEquals(0, recordCount);
	}
}
