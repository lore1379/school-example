package com.examples.school.repository.mongo;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.examples.school.model.Student;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class StudentMongoRepositoryTestcontainersIT {

	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");
	private MongoClient client;
	private StudentMongoRepository studentRepository;
	private MongoCollection<Document> studentCollection;
	
	public static final String STUDENT_COLLECTION_NAME = "student";
	public static final String SCHOOL_DB_NAME = "school";
	
	@Before
	public void setup() {		
		client = new MongoClient(
				new ServerAddress(
						mongo.getContainerIpAddress(),
						mongo.getFirstMappedPort()));
		studentRepository = new StudentMongoRepository(client, SCHOOL_DB_NAME, STUDENT_COLLECTION_NAME);
		MongoDatabase database = client.getDatabase(SCHOOL_DB_NAME);
		database.drop();
		studentCollection = database.getCollection(STUDENT_COLLECTION_NAME);
	}
	
	@After
	public void tearDown() {
		client.close();		
	}

	@Test
	public void testFindAll() {
		addTestStudentToDatabase("1", "test1");
		addTestStudentToDatabase("2", "test2");
		assertThat(studentRepository.findAll())
			.containsExactly(
					new Student("1", "test1"),
					new Student("2", "test2"));
			
	}
	
	@Test
	public void testFindById() {
		addTestStudentToDatabase("1", "test1");
		addTestStudentToDatabase("2", "test2");
		assertThat(studentRepository.findById("2"))
			.isEqualTo(new Student("2", "test2"));
		
	}
	
	@Test
	public void testSave() {
		Student student = new Student ("1", "added student");
		studentRepository.save(student);
		assertThat(readAllStudentsFromDatabase())
			.containsExactly(student);
	}
	
	@Test
	public void testDelete() {
		addTestStudentToDatabase("1", "test1");
		studentRepository.delete("1");
		assertThat(readAllStudentsFromDatabase())
			.isEmpty();
	}

	private List<Student> readAllStudentsFromDatabase() {
		return StreamSupport.stream(studentCollection.find().spliterator(), false)
					.map(d -> new Student(""+d.get("id"), ""+d.get("name")))
					.collect(Collectors.toList());
	}

	private void addTestStudentToDatabase(String id, String name) {
		studentCollection.insertOne(
				new Document()
					.append("id", id)
					.append("name", name));
	}
}
