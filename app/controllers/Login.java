package controllers;

import models.Project;
import models.Task;
import play.data.Form;
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
		//TODO implementar autenticacao
		return null;
	}
}
