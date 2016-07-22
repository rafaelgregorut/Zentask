package controllers;

import models.Project;
import models.Task;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;

public class Application extends Controller {

	@Security.Authenticated(Secured.class)
    public Result index() {

        return ok(index.render(
        			Project.find.all(),
        			Task.find.all()
        		));
    }

}
