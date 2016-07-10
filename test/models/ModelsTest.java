package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.libs.Yaml;
import play.test.WithApplication;

import com.avaje.ebean.Ebean;

public class ModelsTest extends WithApplication {

	@Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
		Ebean.save((List) Yaml.load("test-data.yml"));

    }
	
	@Test
	public void createAndRetrieveUser() {
		new User("greg@example.com","Greg","123456").save();
		User greg = User.find.where().eq("email", "greg@example.com").findUnique();
		assertNotNull(greg);
		assertEquals(greg.name,"Greg");
	}
	
	@Test
	public void tryAuthenticateUser() {
		//new User("bob@gmail.com","Bob","123456").save();
		assertNull(User.authenticate("bob@example", "098765"));
		assertNull(User.authenticate("test@test.com", "secret"));
		assertNotNull(User.authenticate("jane@example.com", "secret"));
		assertNotNull(User.authenticate("bob@example.com","secret"));
		
	}
	
	@Test
	public void findProjectsInvolving() {
		//new User("bob@gmail.com", "Bob", "secret").save();
        //new User("jane@gmail.com", "Jane", "secret").save();

        //Project.create("Play 2", "play", "bob@gmail.com");
        //Project.create("Play 1", "play", "jane@gmail.com");
        
        List<Project> results = Project.findInvolving("jane@example.com");
        
        assertEquals(3, results.size());
        assertEquals("Play 2.0", results.get(0).name);
	}
	
	@Test
    public void findTodoTasksInvolving() {
        //User bob = new User("bob@gmail.com", "Bob", "secret");
        //bob.save();

        //Project project = Project.create("Play 2", "play", "bob@gmail.com");
        /*Task t1 = new Task();
        t1.title = "Write tutorial";
        t1.assignedTo = bob;
        t1.done = true;
        t1.save();

        Task t2 = new Task();
        t2.title = "Release next version";
        t2.project = project;
        t2.save();*/

        List<Task> results = Task.findProjectsWithPendingTasksInvolving("jeff@example.com");
        assertEquals(2, results.size());
    }
	
	@Test
	public void findTasksAssignedTo() {
		List<Task> assignedToJeff = Task.findAssignedTo("jeff@example.com");
		assertEquals(1, assignedToJeff.size());
		assertEquals("Finish zentask integration", assignedToJeff.get(0).title);
	}
	
	@Test
	public void fullTest() {
		
		//Counting
		assertEquals(3, User.find.findRowCount());
		assertEquals(7, Project.find.findRowCount());
		assertEquals(5, Task.find.findRowCount());
		
		//User authentication
		assertNotNull(User.authenticate("bob@example.com", "secret"));
		assertNotNull(User.authenticate("jane@example.com", "secret"));
		assertNull(User.authenticate("bob@bla.com", "secret"));
		assertNull(User.authenticate("jeff@example.com", "notasecret"));
		assertNull(User.authenticate("bilbo@middle.earth", "theonering"));
		
		//Find bob's projects
		List<Project> bobsProjects = Project.findInvolving("bob@example.com");
		assertEquals(5, bobsProjects.size());
		
		//Find bob's todos
		List<Task> bobsTodos = Task.findProjectsWithPendingTasksInvolving("bob@example.com");
		assertEquals(4, bobsTodos.size());
	}
}
