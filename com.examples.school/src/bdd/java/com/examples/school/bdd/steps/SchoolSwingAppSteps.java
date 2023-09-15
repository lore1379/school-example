package com.examples.school.bdd.steps;

import org.bson.Document;

import com.mongodb.MongoClient;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SchoolSwingAppSteps {

	private static final String COLLECTION_NAME = "test-collection";
	private static final String DB_NAME = "test-db";
	
	private MongoClient mongoClient;

	@Before
	public void setup() {	
		mongoClient = new MongoClient();
		mongoClient.getDatabase(DB_NAME).drop();
	}
	
	@After
	public void tearDown() {
		mongoClient.close();
	}
	
	@Given("The database contains a student with id {string} and name {string}")
	public void the_database_contains_a_student_with_id_and_name(
			String id, String name) {
	    mongoClient
	    	.getDatabase(DB_NAME)
	    	.getCollection(COLLECTION_NAME)
	    	.insertOne(
	    			new Document()
	    				.append("id", id)
	    				.append("name", name));
	}

	@When("The Student View is shown")
	public void the_Student_View_is_shown() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("The list contains an element with id {string} and name {string}")
	public void the_list_contains_an_element_with_id_and_name(String string, String string2) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}
}
