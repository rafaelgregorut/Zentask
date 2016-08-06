import play.*;
import play.libs.Yaml;

import com.avaje.ebean.*;

import models.*;

import java.util.*;

public class Global extends GlobalSettings{
	
	@Override
	public void onStart(Application app) {
		//Check if we need to import sample data for the users
		if(ZenUser.find.findRowCount() == 0) {
			Ebean.save((List) Yaml.load("dev-data.yml"));
		}
	}

}
