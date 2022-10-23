package com.henz.student;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * this is a Unit test
 * 
 * */

@DataJpaTest //spin up database
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //We don't provide an embedded database by default. By default DataJpaTest replaces our DataSource with an embedded database but we don't have one. 
class StudentRepositoryTest {
	
	@Autowired
	private StudentRepository studentRepository;
	
	//make after method to delete all...but with @DataJpaTest we do not save any records
	
	//we do not need to test the JpaRepository methods!!

	@Test
	void testSelectExistsEmail() {
		//given
		Student s1 = new Student("joel","hjoel87@gmx.ch", Gender.MALE);
		studentRepository.save(s1);
		
		//when
		boolean exists = studentRepository.selectExistsEmail("hjoel87@gmx.ch");
		
		//then
		Assertions.assertThat(exists).isTrue();
	}
	
	@Test
	void testStudentDoesNotExistByEmail() {
		//given
		String email = "test@test.ch";
		
		//when
		boolean exists = studentRepository.selectExistsEmail(email);
		
		//then
		Assertions.assertThat(exists).isFalse();
	}

}
