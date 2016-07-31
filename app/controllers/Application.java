package controllers;

//import play.api.mvc.*;
//import play.api.routing.*;
import models.Project;
import models.Task;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;
import play.Routes;

public class Application extends Controller {

	@Security.Authenticated(Secured.class)
    public Result index() {
        return ok(index.render(
        			Project.findInvolving(request().username()),
        			Task.findProjectsWithPendingTasksInvolving(request().username()),
        			User.find.byId(request().username())
        		));
    }
	
	//JavaScript router instead of AJAX calls with hardcoded URLs
	public Result javascriptRoutes() {
		//script tag
		response().setContentType("text/javascript");
		//Router will be bound to global variable called jsRoutes
		return ok(
				Routes.javascriptRouter("jsRoutes",
						controllers.routes.javascript.ProjectController.addProject(),
						controllers.routes.javascript.ProjectController.delete(),
						controllers.routes.javascript.ProjectController.rename(),
						controllers.routes.javascript.FolderController.addFolder(),
						controllers.routes.javascript.FolderController.renameFolder()
						)
				);
	}

}
