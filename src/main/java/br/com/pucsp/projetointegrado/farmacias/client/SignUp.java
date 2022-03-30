package br.com.pucsp.projetointegrado.farmacias.client;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.pucsp.projetointegrado.farmacias.client.signup.CreateUsers;
import br.com.pucsp.projetointegrado.farmacias.client.signup.SignUpDB;
import br.com.pucsp.projetointegrado.farmacias.utils.CheckSignUp;
import br.com.pucsp.projetointegrado.farmacias.utils.GetUserCoordinates;

public class SignUp {
	public boolean createUser(Map<String, String> variables, CreateUsers user) {
		CheckSignUp validate = new CheckSignUp();
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
			
			SignUpDB createUser = new SignUpDB();
			boolean create = createUser.CreateUserDB(variables, user, lat, lon);
			
			if(create) {
				return true;			
			}
		}
		
		return false;
	}
}