package com.examples.school.repository.mongo;


import static org.assertj.core.api.Assertions.*;

import java.net.InetSocketAddress;

import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class StudentMongoRepositoryTest {

	@Test
	public void testFindAllWhenDatabaseIsEmpty() {
		MongoServer server = new MongoServer(new MemoryBackend());
		InetSocketAddress serverAddress = server.bind();
		MongoClient client = new MongoClient(new ServerAddress(serverAddress));
		StudentMongoRepository studentRepository = new StudentMongoRepository(client);
		assertThat(studentRepository.findAll()).isEmpty();
	}
}
