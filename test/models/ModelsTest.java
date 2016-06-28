package models;

import models.*;
import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

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
}
