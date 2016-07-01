package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.test.WithApplication;

public class ModelsTest extends WithApplication {

	@Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
    }
	
	@Test
	public void createAndRetrieveUser() {
		new User("bob@gmail.com","Bob","123456").save();
		User bob = User.find.where().eq("email", "bob@gmail.com").findUnique();
		assertNotNull(bob);
		assertEquals(bob.name,"Bob");
	}
	
	@Test
	public void tryAuthenticateUser() {
		new User("bob@gmail.com","Bob","123456").save();
		assertNotNull(User.authenticate("bob@gmail.com", "123456"));
		assertNull(User.authenticate("bob@gmail.com", "098765"));
		assertNull(User.authenticate("test@test.com", "123456"));
	}
	
	@Test
	public void findProjectsInvolving() {
		new User("bob@gmail.com", "Bob", "secret").save();
        new User("jane@gmail.com", "Jane", "secret").save();

        Project.create("Play 2", "play", "bob@gmail.com");
        Project.create("Play 1", "play", "jane@gmail.com");
        
        List<Project> results = Project.findInvolving("bob@gmail.com");
        
        assertEquals(1, results.size());
        assertEquals("Play 2", results.get(0).name);
	}
	
	@Test
    public void findTodoTasksInvolving() {
        User bob = new User("bob@gmail.com", "Bob", "secret");
        bob.save();

        Project project = Project.create("Play 2", "play", "bob@gmail.com");
        Task t1 = new Task();
        t1.title = "Write tutorial";
        t1.assignedTo = bob;
        t1.done = true;
        t1.save();

        Task t2 = new Task();
        t2.title = "Release next version";
        t2.project = project;
        t2.save();

        List<Task> results = Task.findTodoInvolving("bob@gmail.com");
        assertEquals(1, results.size());
        assertEquals("Release next version", results.get(0).title);
    }
}
