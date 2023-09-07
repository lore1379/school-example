package com.examples.school.controller;

import java.util.List;

import com.examples.school.model.Student;
import com.examples.school.repository.StudentRepository;
import com.examples.school.view.StudentView;

public class SchoolController {

	private StudentView studentView ;
	private StudentRepository studentRepository ;

	public SchoolController(StudentView studentView, StudentRepository studentRepository) {
		this.studentView = studentView;
		this.studentRepository = studentRepository;
	}

	public void allStudents() {
		List<Student> studends = studentRepository.findAll();
		studentView.showAllStudents(studends);
	}

}
