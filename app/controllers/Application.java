package controllers;

import models.Project;
import models.Task;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;

public class Application extends Controller {

	@Security.Authenticated(Secured.class)
    public Result index() {
        return ok(index.render(
        			Project.findInvolving(request().username()),
        			Task.findProjectsWithPendingTasksInvolving(request().username()),
        			User.find.byId(request().username())
        		));
    }

}
