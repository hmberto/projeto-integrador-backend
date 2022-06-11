package br.com.pucsp.projetointegrador.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.pucsp.projetointegrador.user.signup.CreateUsers;
import br.com.pucsp.projetointegrador.user.signup.SignUpDB;
import br.com.pucsp.projetointegrador.utils.CheckSignUp;
import br.com.pucsp.projetointegrador.utils.GetUserCoordinates;

public class SignUp {
	private static String name =  SignUp.class.getSimpleName();
	private static Logger log = Logger.getLogger( SignUp.class.getName());
	
	public boolean createUser(Map<String, String> variables, CreateUsers user) throws SQLException, ClassNotFoundException, MessagingException, IOException {
		log.entering(name, "createUser");
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
			boolean create = createUser.createUserDB(variables, user, lat, lon);
			
			log.exiting(name, "createUser");
			return create;
		}
		
		return false;
	}
}