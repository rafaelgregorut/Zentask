package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.format.Formats;

import com.avaje.ebean.Model;

@Entity
public class Task extends Model{

	@Id
	public Long id;
	public String title;
	public boolean done = false;
    @Formats.DateTime(pattern="MM/dd/yy")
	public Date dueDate;
	@ManyToOne
	public User assignedTo;
	@ManyToOne
	public Project project;
	
    public static Model.Finder<Long,Task> find = new Model.Finder(Long.class, Task.class);
    
    public Task(String title, Date dueDate, User assignedTo, Project project) {
    	this.title = title;
    	this.dueDate = dueDate;
    	this.assignedTo = assignedTo;
    	this.project = project;
    }
    
    public static List<Task> findProjectsWithPendingTasksInvolving(String user) {
    	return find.fetch("project").where().eq("done","false").eq("project.members.email", user).findList();
    }
    
    public static List<Task> findTasksFromProject(Long projectID) {
    	return find.where().eq("project.id", projectID).findList();
    }
    
    public static List<Task> findAssignedTo(String user) {
    	return find.where().eq("assignedTo.email", user).findList();
    }
    
    public static Task create(String title, Date dueDate, User assignedTo, Long project) {
    	
   		Task task = new Task(title,dueDate,assignedTo,Project.find.ref(project));	
		task.save();
	    return task;
    }
    
    public static Task updateDone(Long taskID) {
    	Task toBeUpdated = Task.find.ref(taskID);
    	toBeUpdated.done = !toBeUpdated.done;
    	toBeUpdated.update();
    	return toBeUpdated;
    }
}
