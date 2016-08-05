package controllers;

import models.User;
import play.data.*;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Login extends Controller {
	
	public String email;
	public String password;
	
	public Result login() {
    	return ok(login.render(Form.form(Login.class)));	
    }
	
	public Result authenticate() {
		//No Eclipse com opcao de build automatically o bind parece nao funcionar direito (Stack)
		Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
		
		if(loginForm.hasErrors()) {
			//Logger.info(email+" "+password);
			return badRequest(login.render(loginForm));
		} else {
			session().clear();
			session("email",loginForm.get().email);
			return redirect(routes.Application.index());
		}
		
	}
	
	public String validate() {
		if (User.authenticate(email, password) == null) {
			//Mensagem retornada pelo validate se torna um erro global
			return "Invalid user or password";
		}
		//se o validate passa, entao tenho que retornar null
		return null;
	}
	
	public Result logout() {
		session().clear();
		flash("You have been logged out");
		return redirect (routes.Login.login());
	}
	
}
