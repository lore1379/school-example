package com.examples.school.view;

import java.util.List;

import com.examples.school.model.Student;

public interface StudentView {

	void showAllStudents(List<Student> studends);

	void studentAdded(Student student);

}
