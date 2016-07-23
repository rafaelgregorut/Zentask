package controllers;

import play.*;
import play.data.Form;
import play.mvc.*;
import models.*;
import views.html.projects.*;

@Security.Authenticated(Secured.class)
public class ProjectController extends Controller{

	public Result addProject() {
		Project newProject = Project.create(
				"New Project",
				Form.form().bindFromRequest().get("folder"),
				request().username());
		return ok(item.render(newProject));
	}
	
	public Result rename(Long projectID) {
		if(Secured.isMemberOf(projectID,request().username())) {
			return ok(Project.rename(projectID, Form.form().bindFromRequest().get("name")));
		} else {
			return forbidden();
		}
	}
}
