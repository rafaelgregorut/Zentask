package controllers;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.DEFAULT_TIMEOUT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeGlobal;
import static play.test.Helpers.header;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.route;
import static play.test.Helpers.session;
import static play.test.Helpers.start;
import static play.test.Helpers.status;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.db.Database;
import play.db.Databases;
import play.libs.Yaml;
import play.mvc.Http;
import play.mvc.Result;
import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

@SuppressWarnings("deprecation")
public class LoginTest {

	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase(),fakeGlobal()));
		Ebean.save((List) Yaml.load("test-data.yml"));
	}
	
	@Test
	public void authenticateSuccessfully() {	
		//Crio um request falso com: metodo POST, rota do login e form com os dados do Bob
		Http.RequestBuilder request = new Http.RequestBuilder()
            .method("POST")
            .uri("/login").bodyForm(ImmutableMap.of(
					"email", "bob@example.com","password", "secret"));
		    
		//resultado do meu request
		Result result2 = route(request, DEFAULT_TIMEOUT);
		//Code 303 HTTP referente a SEE_OTHER (dei uma olhada no network do Firefox e foi esse mesmo o codigo retornado apos um login com sucesso
		assertEquals(303, status(result2));
		//vejo se o email do bob ficou na sessao
		assertEquals("bob@example.com", session(result2).get("email"));
	}
	
	@Test
	public void authenticateEmailFail() {
		//Crio um request falso com: metodo POST, rota do login e form com email errado
		Http.RequestBuilder request = new Http.RequestBuilder()
            .method("POST")
            .uri("/login").bodyForm(ImmutableMap.of(
					"email", "wrongemail@incorrect.com","password", "secret"));
		    
		//resultado do meu request
		Result result2 = route(request, DEFAULT_TIMEOUT);
		//Code 400 HTTP referente a BAD_REQUEST (dei uma olhada no network do Firefox e foi esse mesmo o codigo retornado apos um login com erro)
		assertEquals(400, status(result2));
		//nao deve ter ficado nenhum email na sessao
		assertNull(session(result2).get("email"));
	}
	
	@Test
	public void authenticatePasswordFail() {
		//Crio um request falso com: metodo POST, rota do login e form com senha errada
		Http.RequestBuilder request = new Http.RequestBuilder()
            .method("POST")
            .uri("/login").bodyForm(ImmutableMap.of(
					"email", "bob@example.com","password", "123456"));
		    
		//resultado do meu request
		Result result2 = route(request, DEFAULT_TIMEOUT);
		//Code 400 HTTP referente a BAD_REQUEST (dei uma olhada no network do Firefox e foi esse mesmo o codigo retornado apos um login com erro)
		assertEquals(400, status(result2));
		//nao deve ter ficado nenhum email na sessao
		assertNull(session(result2).get("email"));
	}
	
	@Test
	public void authenticateTotalFail() {
		//Crio um request falso com: metodo POST, rota do login e form com tudo errado
		Http.RequestBuilder request = new Http.RequestBuilder()
            .method("POST")
            .uri("/login").bodyForm(ImmutableMap.of(
					"email", "bob@wrongemail.com","password", "123456"));
		    
		//resultado do meu request
		Result result2 = route(request, DEFAULT_TIMEOUT);
		//Code 400 HTTP referente a BAD_REQUEST (dei uma olhada no network do Firefox e foi esse mesmo o codigo retornado apos um login com erro)
		assertEquals(400, status(result2));
		//nao deve ter ficado nenhum email na sessao
		assertNull(session(result2).get("email"));
	}
	
	@Test
	public void userNotYetAuthenticated() {
		Http.RequestBuilder request = new Http.RequestBuilder()
        .method("GET")
        .uri("/");
		Result result2 = route(request, DEFAULT_TIMEOUT);
		assertEquals(303, status(result2));
	    assertEquals("/login", header("Location", result2));
	}
	
	@Test
	public void userAlreadyAuthenticated() {
		Http.RequestBuilder request = new Http.RequestBuilder()
        .method("GET")
        .uri("/")
        .session("email", "bob@example.com");
		Result result2 = route(request, DEFAULT_TIMEOUT);
		assertEquals(200, status(result2));
	}
	
}
