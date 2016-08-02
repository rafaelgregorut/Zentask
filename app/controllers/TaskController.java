package controllers;

import models.Project;
import models.Task;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.tasks.*;
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
	
	public Result addTask(Long projectID) {
		if(Secured.isMemberOf(projectID, request().username())) {
			Form<Task> taskForm = Form.form(Task.class).bindFromRequest();
			return ok(item.render(Task.create(taskForm.get().title, taskForm.get().dueDate, taskForm.get().assignedTo, projectID)));
		} else {
			return forbidden();
		}
	}
}
