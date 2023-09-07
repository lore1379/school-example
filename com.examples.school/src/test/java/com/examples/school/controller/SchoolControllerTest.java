package com.examples.school.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;

import com.examples.school.model.Student;
import com.examples.school.repository.StudentRepository;
import com.examples.school.view.StudentView;

public class SchoolControllerTest {
	
	@Test
	public void testAllStudends() {
		List<Student> studends = asList(new Student());
		StudentRepository studentRepository = mock(StudentRepository.class);
		when(studentRepository.findAll()).
			thenReturn(studends);
		SchoolController schoolController = new SchoolController();
		schoolController.allStudents();
		StudentView studentView = mock(StudentView.class);
		verify(studentView).showAllStudents(studends);
	}

}
