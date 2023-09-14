package com.examples.school.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.examples.school.controller.SchoolController;
import com.examples.school.model.Student;
import com.examples.school.repository.mongo.StudentMongoRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@RunWith(GUITestRunner.class)
public class StudentSwingIT extends AssertJSwingJUnitTestCase {
	
	private static int mongoPort =
			Integer.parseInt(System.getProperty("mongo.port", "27017"));
	
	private StudentSwingView studentSwingView;
	private SchoolController schoolController;
	private FrameFixture window;
	private StudentMongoRepository studentRepository;

	@Override
	protected void onSetUp() throws Exception {
		studentRepository = new StudentMongoRepository(
				new MongoClient(
						new ServerAddress("localhost", mongoPort)));
		for (Student student : studentRepository.findAll()) {
			studentRepository.delete(student.getId());
		}
		GuiActionRunner.execute(() -> {
			studentSwingView = new StudentSwingView();
			schoolController = new SchoolController(studentSwingView, studentRepository);
			studentSwingView.setSchoolController(schoolController);
			return studentSwingView;
		});
		window = new FrameFixture(robot(), studentSwingView);
		window.show();
	}

	@Test @GUITest
	public void testAllStudents() {
		// use the repository to add students to the database
		Student student1 = new Student("1", "test1");
		Student student2 = new Student("2", "test2");
		studentRepository.save(student1);
		studentRepository.save(student2);
		// use the controller's allStudents
		GuiActionRunner.execute(
			() -> schoolController.allStudents());
		// and verify that the view's list is populated
		assertThat(window.list().contents())
			.containsExactly(student1.toString(), student2.toString());
	}
	
	@Test @GUITest
	public void testAddButtonSuccess() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents())
			.containsExactly(new Student("1", "test").toString());
	}
	
	@Test @GUITest
	public void testAddButtonError() {
		studentRepository.save(new Student("1", "existing"));
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents()).isEmpty();
		window.label("errorMessageLabel")
			.requireText("Already existing student with id 1: " + new Student("1", "existing"));
	}
}
