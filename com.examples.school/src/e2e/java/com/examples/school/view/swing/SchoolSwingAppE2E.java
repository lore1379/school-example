package com.examples.school.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.*;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@RunWith(GUITestRunner.class)
public class SchoolSwingAppE2E extends AssertJSwingJUnitTestCase{
	
	private static final String STUDENT_FIXTURE_2_NAME = "second student";
	private static final String STUDENT_FIXTURE_2_ID = "2";
	private static final String STUDENT_FIXTURE_1_NAME = "first student";
	private static final String STUDENT_FIXTURE_1_ID = "1";
	private static final String DB_NAME = "test-db";
	private static final String COLLECTION_NAME = "test-collection";

	private static int mongoPort =
			Integer.parseInt(System.getProperty("mongo.port", "27017"));
	
	private MongoClient mongoClient;
	private FrameFixture window;

	@Override
	protected void onSetUp() throws Exception {
		
		mongoClient = new MongoClient(
				new ServerAddress("localhost", mongoPort));
		mongoClient.getDatabase(DB_NAME).drop();
		addTestStudentToDatabase(STUDENT_FIXTURE_1_ID, STUDENT_FIXTURE_1_NAME);
		addTestStudentToDatabase(STUDENT_FIXTURE_2_ID, STUDENT_FIXTURE_2_NAME);
		application("com.examples.school.app.swing.SchoolSwingApp")
			.withArgs(
				"--mongo-host=" + "localhost",
				"--mongo-port=" + Integer.toString(mongoPort),
				"--db-name=" + DB_NAME,
				"--db-collection=" + COLLECTION_NAME
			)
			.start();
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Student View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
	}
	
	@Test @GUITest
	public void testOnStartAllDatabaseElementsAreShown() {
		assertThat(window.list().contents())
			.anySatisfy(e -> assertThat(e).contains(STUDENT_FIXTURE_1_ID, STUDENT_FIXTURE_1_NAME))
			.anySatisfy(e -> assertThat(e).contains(STUDENT_FIXTURE_2_ID, STUDENT_FIXTURE_2_NAME));
	}
	
	private void addTestStudentToDatabase(String id, String name) {
		mongoClient
			.getDatabase(DB_NAME)
			.getCollection("test-collection")
			.insertOne(
				new Document()
					.append("id", id)
					.append("name", name));
	}

}
