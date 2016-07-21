package controllers;

import models.Project;
import models.Task;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {

    public Result index() {

        return ok(index.render(
        			Project.find.all(),
        			Task.find.all()
        		));
    }

}
