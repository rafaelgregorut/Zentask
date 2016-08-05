package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import models.*;

public class Secured extends Security.Authenticator {
	
	@Override
	public String getUsername (Context context) {
		return context.session().get("email");
	}
	
	@Override
	public Result onUnauthorized(Context context) {
		return redirect(routes.Login.login());
	}

	public static boolean isMemberOf(Long projectID, String user) {
		//Context.current().request() me permitiria pegar o request sem estar em uma action
		//Mas preferi passar o user como parametro e lidar com o request no projectcontroller mesmo
		return Project.isMemberOf(projectID, user);
	}
	
	public static boolean isTheAssignedTo(String user, Long taskID) {
		return Task.find.ref(taskID).assignedTo.email.equals(user);
	}
}
