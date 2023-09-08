package com.examples.school.repository.mongo;


import static org.assertj.core.api.Assertions.*;
import static com.examples.school.repository.mongo.StudentMongoRepository.SCHOOL_DB_NAME;
import static com.examples.school.repository.mongo.StudentMongoRepository.STUDENT_COLLECTION_NAME;

import java.net.InetSocketAddress;

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
		studentRepository = new StudentMongoRepository(client);		
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
		MongoDatabase database = client.getDatabase(SCHOOL_DB_NAME);
		database.drop();
		MongoCollection<Document> studentCollection = database.getCollection(STUDENT_COLLECTION_NAME);
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
