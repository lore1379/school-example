package com.examples.school.repository;

import java.util.List;

import com.examples.school.model.Student;

public interface StudentRepository {

	public List<Student> findAll();

}
