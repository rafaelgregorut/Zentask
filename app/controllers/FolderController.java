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
	
	public Result renameFolder(Long id) {
		return ok(Folder.rename(id, Form.form().bindFromRequest().get("name")));
	}
	
	public Result removeFolder(Long id) {
		Folder.find.ref(id).delete();
		return ok();
	}
}
