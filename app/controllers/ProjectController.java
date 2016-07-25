package controllers;

import java.util.ArrayList;

import models.Project;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.projects.item;
import views.html.projects.folder.folder;

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
	
	public Result delete(Long projectID) {
		if(Secured.isMemberOf(projectID, request().username())) {
			Project.find.ref(projectID).delete();
			return ok();
		} else {
			return forbidden();
		}
	}
	
	public Result addFolder() {
		return ok(folder.render("New Folder",new ArrayList<Project>()));
	}
}
