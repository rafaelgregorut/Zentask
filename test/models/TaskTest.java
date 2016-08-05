package models;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeGlobal;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.libs.Yaml;
import play.test.WithApplication;

import com.avaje.ebean.Ebean;


public class TaskTest extends WithApplication {

	@Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(),fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }
	
	@Test
	public void findProjectsInvolving() {
        List<Project> results = Project.findInvolving("jane@example.com");
        
        assertEquals(3, results.size());
        assertEquals("Play 2.0", results.get(0).name);
	}
	
	@Test
    public void findTodoTasksInvolving() {
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
	public void createTask() throws ParseException {
		String dateStr = "04/27/93"; 
		DateFormat formatter = new SimpleDateFormat("MM/dd/yy"); 
		Date dueDate = (Date)formatter.parse(dateStr);
		int countBeforeCreation = Task.find.findRowCount();
		Task task = Task.create("title", dueDate, User.find.ref("bob@example.com"), (long)1);
		assertEquals("title",task.title);
		assertEquals(dueDate.toString(),task.dueDate.toString());
		assertEquals("bob@example.com",task.assignedTo.email);
		assertEquals(countBeforeCreation + 1,Task.find.findRowCount());
	}
	
	@Test
	public void updateTheDoneStatus() {
		long taskID = Task.find.all().get(0).id;
		
		Task.updateDone(taskID);
		assertEquals(true,Task.find.ref(taskID).done);
		Task.updateDone(taskID);
		assertEquals(false,Task.find.ref(taskID).done);
	}
}
