package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeGlobal;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.libs.Yaml;
import play.test.WithApplication;

import com.avaje.ebean.Ebean;

public class UserTest extends WithApplication {

	@Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(),fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }
	
	//USER
	@Test
	public void createAndRetrieveUser() {
		new User("greg@example.com","Greg","123456").save();
		User greg = User.find.where().eq("email", "greg@example.com").findUnique();
		assertNotNull(greg);
		assertEquals(greg.name,"Greg");
	}
	
	@Test
	public void tryAuthenticateUser() {
		assertNull(User.authenticate("bob@example", "098765"));
		assertNull(User.authenticate("test@test.com", "secret"));
		assertNotNull(User.authenticate("jane@example.com", "secret"));
		assertNotNull(User.authenticate("bob@example.com","secret"));
		
	}
}
