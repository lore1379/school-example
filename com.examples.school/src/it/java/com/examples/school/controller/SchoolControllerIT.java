package com.examples.school.controller;

import static org.mockito.Mockito.verify;
import static java.util.Arrays.asList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.examples.school.model.Student;
import com.examples.school.repository.mongo.StudentMongoRepository;
import com.examples.school.view.StudentView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class SchoolControllerIT {
	
	private static int mongoPort =
			Integer.parseInt(System.getProperty("mongo.port", "27017"));
	
	@Mock
	private StudentView studentView;
	
	private StudentMongoRepository studentRepository;
	
	@InjectMocks
	private SchoolController schoolController;
	
	private AutoCloseable closeable;

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);	
		studentRepository = new StudentMongoRepository(
				new MongoClient(
						new ServerAddress("localhost", mongoPort)));
		for (Student student : studentRepository.findAll()) {
			studentRepository.delete(student.getId());
		}
		schoolController = new SchoolController(studentView, studentRepository);	
	}
	
	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllStudents() {
		Student student = new Student("1", "test");
		studentRepository.save(student);
		schoolController.allStudents();
		verify(studentView)
			.showAllStudents(asList(student));
	}
	
	@Test
	public void testNewStudent() {
		Student student = new Student("1", "test");
		schoolController.newStudent(student);
		verify(studentView)
			.studentAdded(student);
		
	}
	
	@Test
	public void testDeleteStudent() {
		Student studentToDelete = new Student("1", "test");
		studentRepository.save(studentToDelete);
		schoolController.deleteStudent(studentToDelete);
		verify(studentView)
			.studentRemoved(studentToDelete);
	}
}
