package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import models.Project;
import models.Task;
import models.User;
import play.Logger;
import play.data.DynamicForm;
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
        	//Form<Task> taskForm = Form.form(Task.class).bindFromRequest("title","dueDate","assignedTo.email");
        	DynamicForm dynForm = Form.form().bindFromRequest();
        	
        	if(dynForm.hasErrors()) {
        		Logger.info("bad request");
	            return badRequest();
	        } else {
	        	DateFormat formatter = new SimpleDateFormat("MM/dd/yy"); 
	        	Date dueDate;
				try {
					dueDate = (Date)formatter.parse(dynForm.get("dueDate"));
				} catch (ParseException e) {
					Logger.info("incorrect date parsing");
					e.printStackTrace();
					return badRequest();
				}
	        	User user = User.find.ref(dynForm.get("assignedTo"));
	        	return ok(item.render(Task.create(dynForm.get("title"), dueDate, user, projectID)));
	        }
	    } else {
			return forbidden();
		}
	}
}
