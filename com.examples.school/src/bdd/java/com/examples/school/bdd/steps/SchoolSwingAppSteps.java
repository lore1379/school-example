package com.examples.school.bdd.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.*;

import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SchoolSwingAppSteps {

	private static final String COLLECTION_NAME = "test-collection";
	private static final String DB_NAME = "test-db";
	
	private static int mongoPort =
			Integer.parseInt(System.getProperty("mongo.port", "27017"));
	
	private MongoClient mongoClient;
	private FrameFixture window;

	@Before
	public void setup() {	
		mongoClient = new MongoClient(
				new ServerAddress("localhost", mongoPort));
		mongoClient.getDatabase(DB_NAME).drop();
	}
	
	@After
	public void tearDown() {
		mongoClient.close();
		if (window != null)
			window.cleanUp();
	}
	
	@Given("The database contains the students with the following values")
	public void the_database_contains_the_students_with_the_following_values(
			List<List<String>> values) {
		values.forEach(
			v -> mongoClient
				.getDatabase(DB_NAME)
				.getCollection(COLLECTION_NAME)
				.insertOne(
					new Document()
						.append("id", v.get(0))
						.append("name", v.get(1)))
		);
	}

	@When("The Student View is shown")
	public void the_Student_View_is_shown() {
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
		}).using(BasicRobot.robotWithCurrentAwtHierarchy());
	}

	@Then("The list contains elements with the following values")
	public void the_list_contains_elements_with_the_following_values(
			List<List<String>> values) {
		values.forEach(
			v -> assertThat(window.list().contents())
				.anySatisfy(e -> assertThat(e).contains(v.get(0), v.get(1)))
		);
	}
	
	@When("The user enters the following values in the text fields")
	public void the_user_enters_the_following_values_in_the_text_fields(
			List<Map<String, String>> values) {
		values
			.stream()
			.flatMap(m -> m.entrySet().stream())
			.forEach(
				e -> window
					.textBox(e.getKey() + "TextBox")
					.enterText(e.getValue())
			);
	}

	@When("The user clicks the {string} button")
	public void the_user_clicks_the_button(String buttonText) {
		window.button(JButtonMatcher.withText(buttonText)).click();
	}
	
	@Then("An error is shown containing the following values")
	public void an_error_is_shown_containing_the_following_values(
			List<List<String>> values) {
		assertThat(window.label("errorMessageLabel").text())
			.contains(values.get(0));
	}
}
