package models;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeGlobal;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.libs.Yaml;

import com.avaje.ebean.Ebean;

public class FolderTest {
	
	@Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(),fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }
	
	@Test
	public void createNewFolder() {
		int countBefore = Folder.find.findRowCount();
		Folder folder = Folder.create("new folder");
		
		assertEquals(countBefore+1,Folder.find.findRowCount());
		assertEquals(folder.id, Folder.find.where().eq("name", "new folder").findUnique().id);
	}
	
	@Test
	public void renameFolder() {
		Folder folder = Folder.find.all().get(0);
		Folder.rename(folder.id, "new name");
		
		assertEquals(Folder.find.ref(folder.id).getName(),"new name");
	}
}
