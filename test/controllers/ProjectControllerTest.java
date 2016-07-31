package controllers;

import static org.junit.Assert.*;
import static play.test.Helpers.DEFAULT_TIMEOUT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeGlobal;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.route;
import static play.test.Helpers.start;
import static play.test.Helpers.status;

import java.util.List;

import models.Folder;
import models.Project;

import org.junit.Before;
import org.junit.Test;

import play.libs.Yaml;
import play.mvc.Http;
import play.mvc.Result;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

@SuppressWarnings("deprecation")
public class ProjectControllerTest {

	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase(),fakeGlobal()));
		Ebean.save((List) Yaml.load("test-data.yml"));
	}
	
	@Test
	public void createProject() {
		int countProjectsBefore;
		Http.RequestBuilder request = new Http.RequestBuilder();
		request.method("POST").path("/projects");
		request.bodyForm(ImmutableMap.of("folder","1"));
		request.session(ImmutableMap.of("email","bob@example.com"));
		
		countProjectsBefore = Project.find.where().eq("members.email", "bob@example.com").findRowCount();
		
		Result result = route(request,DEFAULT_TIMEOUT);
		
		
		assertEquals(200,status(result));
		assertEquals(Project.find.where().eq("members.email", "bob@example.com").findRowCount(),countProjectsBefore+1);
		Project project = Project.find.where().eq("name", "New Project").findUnique();
		assertNotNull(project);
		assertEquals("bob@example.com",project.members.get(0).email);
	}
	
	@Test
	public void renameProjectAuthorizedUser() {
		Long projectID = Project.find.where().eq("name", "Website").findUnique().id;
		
		Http.RequestBuilder request = new Http.RequestBuilder();
		request.method("PUT").path("/projects/"+projectID);
		request.bodyForm(ImmutableMap.of("name","New Name"));
		request.session(ImmutableMap.of("email","bob@example.com"));
		
		Result result = route(request,DEFAULT_TIMEOUT);
		
		assertEquals(200,status(result));
		Project project = Project.find.ref(projectID);
		
		assertNotNull(project);
		assertEquals("New Name",project.name);
	}
	
	@Test
	public void renameProjectUnauthorizedUser() {
		Long projectID = Project.find.where().eq("name", "Website").findUnique().id;
		
		Http.RequestBuilder request = new Http.RequestBuilder();
		request.method("PUT").path("/projects/"+projectID);
		request.bodyForm(ImmutableMap.of("name","New Name"));
		request.session(ImmutableMap.of("email","bubbles@example.com"));
		
		Result result = route(request,DEFAULT_TIMEOUT);

		//403 eh o codigo do forbidden
		assertEquals(403,status(result));
		
		Project project = Project.find.ref(projectID);
		assertNotNull(project);
		assertEquals("Website",project.name);
	}
	
	@Test
	public void renameProjectNotLoggedIn() {
		Long projectID = Project.find.where().eq("name", "Website").findUnique().id;
		
		Http.RequestBuilder request = new Http.RequestBuilder();
		request.method("PUT").path("/projects/"+projectID);
		request.bodyForm(ImmutableMap.of("name","New Name"));
		
		Result result = route(request,DEFAULT_TIMEOUT);

		//303 pq se nao tem usuario na sessao entao ele me redireciona para a tela de login
		assertEquals(303,status(result));
		
		Project project = Project.find.ref(projectID);
		assertNotNull(project);
		assertEquals("Website",project.name);
	}
	
	@Test
	public void deleteProjectAuthorizedUser() {
		Long projectID = Project.find.where().eq("name", "Website").findUnique().id;
		
		Http.RequestBuilder request = new Http.RequestBuilder();
		request.method("DELETE").path("/projects/"+projectID);
		request.session(ImmutableMap.of("email","bob@example.com"));
		
		Result result = route(request,DEFAULT_TIMEOUT);
		
		assertEquals(200,status(result));
		assertEquals(false,Project.find.where().eq("name", "Website").findRowCount() > 0);
	}
	
	@Test
	public void deleteProjectUnauthorizedUser() {
		Long projectID = Project.find.where().eq("name", "Website").findUnique().id;
		
		Http.RequestBuilder request = new Http.RequestBuilder();
		request.method("DELETE").path("/projects/"+projectID);
		request.session(ImmutableMap.of("email","jane@example.com"));
		
		Result result = route(request,DEFAULT_TIMEOUT);
		
		assertEquals(403,status(result));
		assertEquals(true,Project.find.where().eq("name", "Website").findRowCount() == 1);
	}
	
	@Test
	public void deleteProjectNotLoggedIn() {
		Long projectID = Project.find.where().eq("name", "Website").findUnique().id;
		
		Http.RequestBuilder request = new Http.RequestBuilder();
		request.method("DELETE").path("/projects/"+projectID);
		
		Result result = route(request,DEFAULT_TIMEOUT);
		
		//deve redirecionar para o login, entao eh o erro 303 (SEE OTHER)
		assertEquals(303,status(result));
		assertEquals(true,Project.find.where().eq("name", "Website").findRowCount() == 1);
	}

	
}
