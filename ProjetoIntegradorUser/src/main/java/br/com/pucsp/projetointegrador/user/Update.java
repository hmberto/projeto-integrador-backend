package br.com.pucsp.projetointegrador.user;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.pucsp.projetointegrador.user.updateusers.UpdateUsers;
import br.com.pucsp.projetointegrador.user.updateusers.UpdateUsersDB;
import br.com.pucsp.projetointegrador.utils.CheckUpdate;
import br.com.pucsp.projetointegrador.utils.GetUserCoordinates;

public class Update {
	public static String NAME = CheckUpdate.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(CheckUpdate.class.getName());
	
	public boolean updateUsers(Map<String, String> variables, UpdateUsers user) {
		LOG.entering(NAME, "updateUsers");
		CheckUpdate validate = new CheckUpdate();
		boolean check = validate.checkData(variables, user);
		
		if(check) {
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
			
			UpdateUsersDB updateUsersDB = new UpdateUsersDB();
			boolean update = updateUsersDB.updateUsersDB(variables, user, lat, lon);
			
			LOG.exiting(NAME, "updateUsers");
			return update;
		}
		
		LOG.exiting(NAME, "updateUsers");
		return false;
	}
}