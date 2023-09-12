package com.examples.school.repository.mongo;

import static org.assertj.core.api.Assertions.*;
import static com.examples.school.repository.mongo.StudentMongoRepository.SCHOOL_DB_NAME;
import static com.examples.school.repository.mongo.StudentMongoRepository.STUDENT_COLLECTION_NAME;

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
	
	@Before
	public void setup() {		
		client = new MongoClient(
				new ServerAddress(
						mongo.getContainerIpAddress(),
						mongo.getMappedPort(27017)));
		studentRepository = new StudentMongoRepository(client);
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
		studentCollection.insertOne(
				new Document()
					.append("id", "1")
					.append("name", "test1"));
		studentCollection.insertOne(
				new Document()
					.append("id", "2")
					.append("name", "test2"));
		assertThat(studentRepository.findAll())
			.containsExactly(
					new Student("1", "test1"),
					new Student("2", "test2"));
			
	}
}
