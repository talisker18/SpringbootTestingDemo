package com.henz.student;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Student {
	
	/*
	 * Using a database sequence is the most efficient Hibernate identifier generation strategy, 
	 * as it allows you to take advantage of the automatic JDBC batching mechanism.
	 * 
	 * */
	
	/*
	 * allocationSize:
	 * 
	 *  'allocationSize' doesn't mean that the entities ids will increase by this value but it is a number after which the 
	 *  database query will be made again to get the next database sequence value. On the application side, ids for an entities 
	 *  instances will always increase by 1 unless we reach the allocationSize limit. After 'allocationSize' is reached, 
	 *  the next id will be retrieved from the database sequence again. In case if application restarts or redeployed before 
	 *  allocationSize limit is reached, we will see a one-time jump in the next value. 'allocationSize' is to improve performance.
	 *  
	 *  Note that if we are not generating schema via JPA then we should specify the values of @SequenceGenerator which should 
	 *  be consistent with existing database sequence.
	 *  
	 *  All elements of @SequenceGenerator are optional except for 'name' element. The default value of 'allocationSize' is 50 and 
	 *  that of 'initialValue' is 1.
	 *  
	 *  ---> see comments bellow for more information
	 * 
	 * */
	
	
    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 5 //The allocationSize is used to instruct the JPA provider the number of values that can be allocated by the application using a single database sequence call.
            // size 1 means with every entity saved, the sequence is called to get a new id
            
            //Imagine a situation where performance is critical and you are going to do a lot of inserts 
            //and a lot of IDs need to be generated. To help in such cases, the allocationSize comes to our help
            //allocationSize=N means that 'Go and fetch the next value from the database once in every N persist calls'
            //And locally increment the value by 1 in between
            
            /*
             * Let me give an example. Assume that the sequence is set up like this in the database:
             * 
             * CREATE SEQUENCE ITEM_ID_SEQ START WITH 1 INCREMENT BY 10;
             * 
             * And assume that all the values starting with 1 are legal (1, 2, 3, 4, ...) for the ITEM_ID column. 
             * In such case you will set the allocationSize=10. So the first persist call will go and fetch the 
             * ITEM_ID_SEQ.NEXTVAL from database. The subsequent persist calls will not go to the database, rather 
             * they will return last value+1 locally until the value reaches 10. That saves 9 database reads.
             * 
             * What if there are two entity managers that try to do the same thing? When the first entity manager calls 
             * the ITEM_ID_SEQ.NEXTVAL it will get 1 and the second one will get 11. 
             * Hence the first one will go on like 1,2,3,...10 and the second one will go on like 11,12,13...20, before 
             * fetching the next ITEM_ID_SEQ.NEXTVAL.
             * 
             * 
             * --------------another example
             * 
             * 
	
			@Id
			@GeneratedValue(strategy = GenerationType.TABLE) --> this uses allocationSize = 32768
			private Long id;
             * 
             * 
             * now we start the application and insert 5 Clazz objects
             * 
             * 
             * On the first run and insert the first record into the clazz table, the hibernate_sequences table will not have the 
             * value for the Clazz entity, the id value in the clazz table will begin with 1. 
             * At the second insert into the clazz table of this run, JPA will not query the table hibernate_sequences 
             * to get the value to generate for the id column anymore. Since the default allocation value of Hibernate 
             * is up to 32768, JPA only needs to increase the value of the id column in the clazz table by 1. 
             * 
             * So until the value of the id column in the clazz table reaches the allocationSize level, 
             * then JPA queries the hibernate_sequence table again to retrieve the new value to generate for the id column. 
             * allocationSize will not change the value so the generated values for the id column in the clazz table 
             * keep repeating until the applications turn off.
             * 
             * ---> then we shut down application and start it again
             * 		---> we save 5 Clazz objects again. The sequence is called again because we restarted the app, and the sequence now starts
             * 				at 32768. This will generate ids 32768, 32769, 32770 etc
             * 
             * */
    )
    @GeneratedValue(
            generator = "student_sequence", //must reference the 'name' attribute of @SequenceGenerator
            strategy = GenerationType.SEQUENCE) //with @SequenceGenerator, customize the sequence
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String name;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    public Student(String name, String email, Gender gender) {
        this.name = name;
        this.email = email;
        this.gender = gender;
    }
}
