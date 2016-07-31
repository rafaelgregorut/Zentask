package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;

@Entity
public class Project extends Model {

	@Id
	public Long id;
	public String name;
	
	@ManyToOne
	public Folder folder;
	
    @ManyToMany(cascade = CascadeType.ALL)
	public List<User> members = new ArrayList<User>();
	
	public static Model.Finder<Long, Project> find = new Model.Finder(Long.class,Project.class);
    
	public Project (String name, Folder folder, User owner) {
		this.name = name;
		this.folder = folder;
		this.members.add(owner);
	}
	
	public static Project create(String name, Long folder, String owner) {
		Project project = new Project(name,Folder.find.ref(folder),User.find.ref(owner));
		project.save();
		Ebean.saveManyToManyAssociations(project, "members");
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
