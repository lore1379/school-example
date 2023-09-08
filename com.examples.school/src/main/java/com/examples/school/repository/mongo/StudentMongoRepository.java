package com.examples.school.repository.mongo;

import java.util.Collections;
import java.util.List;

import org.bson.Document;

import com.examples.school.model.Student;
import com.examples.school.repository.StudentRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

public class StudentMongoRepository implements StudentRepository {

	public StudentMongoRepository(MongoClient client) {
		MongoCollection<Document> studentCollection = client
				.getDatabase("school")
				.getCollection("student");
	}

	@Override
	public List<Student> findAll() {
		return Collections.emptyList();
	}

	@Override
	public Student findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Student student) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String string) {
		// TODO Auto-generated method stub

	}

}
