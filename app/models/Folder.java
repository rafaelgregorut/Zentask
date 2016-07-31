package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;

import com.avaje.ebean.Model;

@Entity
public class Folder extends Model{

	@Id
	public Long id;
	public String name;
	
    public static Model.Finder<Long,Folder> find = new Model.Finder(Long.class, Folder.class);
	
	public Folder(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public static Folder create(String name) {
		Folder folder = new Folder(name);
		folder.save();
		return folder;
	}
	
	public static String rename(Long id, String newName) {
		Folder toRename = find.ref(id);
		toRename.setName(newName);
		toRename.update();
		return newName;
	}
	
}
