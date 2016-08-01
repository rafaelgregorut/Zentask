package models;

import javax.persistence.*;

import com.avaje.ebean.Model;

import java.util.*;

@Entity
public class Task extends Model{

	@Id
	public Long id;
	public String title;
	public boolean done = false;
	public Date dueDate;
	@ManyToOne
	public User assignedTo;
	@ManyToOne
	public Project project;
	
    public static Model.Finder<Long,Task> find = new Model.Finder(Long.class, Task.class);
    
    public static List<Task> findProjectsWithPendingTasksInvolving(String user) {
    	return find.fetch("project").where().eq("done","false").eq("project.members.email", user).findList();
    }
    
    public static List<Task> findTasksFromProject(Long projectID) {
    	return find.where().eq("project.id", projectID).findList();
    }
    
    public static List<Task> findAssignedTo(String user) {
    	return find.where().eq("assignedTo.email", user).findList();
    }
    
    public static Task create(Task task, Long project) {
        task.project = Project.find.ref(project);
        task.save();
        return task;
    }
}
