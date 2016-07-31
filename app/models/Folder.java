package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;

@Entity
public class Folder extends Model{

	@Id
	public String name;
	
    public static Model.Finder<String,Folder> find = new Model.Finder(String.class, Folder.class);
	
	public Folder(String name) {
		this.name = name;
	}
	
	public static Folder create(String name) {
		Folder folder = new Folder(name);
		folder.save();
		return folder;
	}
	
	public static String rename(String oldName, String newName) {
		Folder toRename = find.ref(oldName);
		toRename.name = newName;
		toRename.update();
		return newName;
	}
	
}
