package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.tasks.index;
import models.*;

@Security.Authenticated(Secured.class)
public class TaskController extends Controller {

	public Result index(Long projectID) {
		if(Secured.isMemberOf(projectID,request().username())) {
			return ok(index.render(Project.find.ref(projectID),
					Task.findTasksFromProject(projectID),
					Project.findInvolving(request().username()),
        			User.find.byId(request().username())
					));
		} else {
			return forbidden();
		}
	}
}
