package br.com.pucsp.projetointegrador.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.pucsp.projetointegrador.user.updateusers.UpdateUsers;
import br.com.pucsp.projetointegrador.user.updateusers.UpdateUsersDB;
import br.com.pucsp.projetointegrador.utils.CheckUpdate;
import br.com.pucsp.projetointegrador.utils.GetUserCoordinates;

public class Update {
	private static String name = Update.class.getSimpleName();
	private static Logger log = Logger.getLogger(Update.class.getName());
	
	public boolean updateUsers(Map<String, String> variables, UpdateUsers user) throws SQLException, ClassNotFoundException, MessagingException, IOException {
		log.entering(name, "updateUsers");
		CheckUpdate validate = new CheckUpdate();
		
		boolean check = validate.checkData(variables, user);
		boolean update = false; 
		
		GetUserCoordinates userCoordinates = new GetUserCoordinates();
		String coordinates = userCoordinates.coordinates(user.getStreet(), user.getNumber());
		JSONArray jsonarray = new JSONArray(coordinates);
		
		String lat = "";
		String lon = "";

		for(int i=0; i<jsonarray.length(); i++){
	        JSONObject obj = jsonarray.getJSONObject(i);

	        lat = obj.getString("lat");
	        lon = obj.getString("lon");
	    }
		
		if(check && lat.length() > 1 && lon.length() > 1) {
			UpdateUsersDB updateUsersDB = new UpdateUsersDB();
			update = updateUsersDB.updateUsersDB(variables, user, lat, lon);
		}
		
		log.exiting(name, "updateUsers");
		return update;
	}
}