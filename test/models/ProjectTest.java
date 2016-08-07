package models;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeGlobal;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.libs.Yaml;

import com.avaje.ebean.Ebean;

public class ProjectTest {


	@Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(),fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }
	
	@Test
	public void createProject() {
		int countBefore = Project.find.findRowCount();
		Project.create("new project", (long)1, "bob@example.com");
		Project nProject = Project.find.where().eq("name", "new project").findUnique();
		
		assertEquals(countBefore + 1,Project.find.findRowCount());
		assertNotNull(nProject);
		assertEquals(nProject.members.get(0).email,"bob@example.com");
	}
	
	@Test
	public void userIsNotMemberOfProject() {
		assertEquals(Project.isMemberOf((long)1,"blabla@example.com.br"), false);
	}
	
	@Test
	public void userIsMemberOfProject() {
		long id = Project.find.where().eq("name","Play 2.0").findUnique().id;
		assertEquals(Project.isMemberOf(id,"bob@example.com"), true);
	}
}
