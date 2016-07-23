package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;

@Entity
public class Project extends Model {

	@Id
	public Long id;
	public String name;
	public String folder;
	
    @ManyToMany(cascade = CascadeType.REMOVE)
	public List<User> members = new ArrayList<User>();
	
	public Project (String name, String folder, User owner) {
		this.name = name;
		this.folder = folder;
		this.members.add(owner);
	}
	
	public static Model.Finder<Long, Project> find = new Model.Finder(Long.class,Project.class);
	
	public static Project create(String name, String folder, String owner) {
		Project project = new Project(name,folder,User.find.ref(owner));
		project.save();
		Ebean.saveManyToManyAssociations(project, "members");
		//project.saveManyToManyAssociations("members");
		return project;
	}
	
	public static List<Project> findInvolving(String user) {
		return find.where().eq("members.email", user).findList();
	}
	
	public static boolean isMemberOf(Long projectID, String user) {
		//procuro por projetos com o ID dado e que tem como membro o usuario
		//se a query retornar algum resultado, entao o usuario eh menbro do projeto
		return find.where().eq("id", projectID).eq("members.email", user).findRowCount() > 0;
	}
	
	public static String rename(Long projectID, String newName) {
		Project toBeRenamed = find.ref(projectID);
		toBeRenamed.name = newName;
		toBeRenamed.update();
		return newName;
	}
}
