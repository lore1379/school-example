package com.examples.school.view;

import java.util.List;

import com.examples.school.model.Student;

public interface StudentView {

	void showAllStudents(List<Student> studends);

	void studentAdded(Student student);

	void showError(String string, Student existingStudent);

	void studentRemoved(Student studentToDelete);

	void showErrorStudentNotFound(String message, Student student);

}
