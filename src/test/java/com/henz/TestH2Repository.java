package com.henz;

import org.springframework.data.jpa.repository.JpaRepository;

import com.henz.student.Student;

public interface TestH2Repository extends JpaRepository<Student, Long>{

}
