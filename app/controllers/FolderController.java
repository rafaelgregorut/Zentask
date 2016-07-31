package controllers;

import java.util.ArrayList;

import models.Folder;
import models.Project;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.projects.folder.folder;


@Security.Authenticated(Secured.class)
public class FolderController extends Controller{

	public Result addFolder() {
		Logger.info("metodo de add folder");
		Folder newFolder = Folder.create("New Folder");
		return ok(folder.render(newFolder,new ArrayList<Project>()));
	}
	
	public Result renameFolder(String name) {
		return ok(Folder.rename(name, Form.form().bindFromRequest().get("name")));
	}
	

}
