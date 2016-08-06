package models;

import javax.persistence.*;

import play.db.ebean.*;

import com.avaje.ebean.*;
import com.avaje.ebean.Model;

@Entity
public class ZenUser extends Model {

	@Id
	public String email;
	public String name;
	public String password;
	
	public static Finder<String,ZenUser> find = new Finder<String,ZenUser>(String.class,ZenUser.class);

	
	public ZenUser(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}
	
	
	public static ZenUser authenticate(String email, String password) {
		return find.where().eq("email", email).eq("password", password).findUnique();
	}
}
