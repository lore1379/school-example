package com.examples.school.repository.mongo;


import static org.assertj.core.api.Assertions.*;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.examples.school.model.Student;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class StudentMongoRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;
	
	private MongoClient client;
	private StudentMongoRepository studentRepository;
	private MongoCollection<Document> studentCollection;
	
	public static final String STUDENT_COLLECTION_NAME = "student";
	public static final String SCHOOL_DB_NAME = "school";
	
	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();		
	}
	
	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}
	
	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(serverAddress));
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
	public void testFindAllWhenDatabaseIsEmpty() {
		assertThat(studentRepository.findAll()).isEmpty();
	}
	
	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {
		addTestStudentToDatabase("1", "test1");
		addTestStudentToDatabase("2", "test2");
		assertThat(studentRepository.findAll())
			.containsExactly(
					new Student("1", "test1"),
					new Student("2", "test2"));
	}
	
	@Test
	public void testFindByIdNotFound() {
		assertThat(studentRepository.findById("1"))
			.isNull();
	}
	
	@Test
	public void testFindByIdFound() {
		addTestStudentToDatabase("1", "test1");
		addTestStudentToDatabase("2", "test2");
		assertThat(studentRepository.findById("2"))
			.isEqualTo(new Student("2", "test2"));
	}
	
	@Test
	public void testSave() {
		Student student = new Student("1", "addedStudent");
		studentRepository.save(student);
		assertThat(readAllStudentFromDatabase())
			.containsExactly(student);
	}
	
	@Test
	public void testDelete() {
		addTestStudentToDatabase("1", "test1");
		studentRepository.delete("1");
		assertThat(readAllStudentFromDatabase())
			.isEmpty();
	}

	private List<Student> readAllStudentFromDatabase() {
		return StreamSupport
					.stream(studentCollection.find().spliterator(), false)
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
