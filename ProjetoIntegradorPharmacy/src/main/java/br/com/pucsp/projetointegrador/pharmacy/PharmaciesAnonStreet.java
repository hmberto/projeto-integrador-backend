package br.com.pucsp.projetointegrador.pharmacy;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.pucsp.projetointegrador.pharmacy.pharmacies.PharmaciesDB;
import br.com.pucsp.projetointegrador.pharmacy.utils.GetUserCoordinates;

public class PharmaciesAnonStreet {
	private static String name = PharmaciesAnonStreet.class.getSimpleName();
	private static Logger log = Logger.getLogger(PharmaciesAnonStreet.class.getName());
	
	public JSONObject getPharmacies(Map <String, String> variables, String distance, String street) throws IOException, JSONException, SQLException {
		log.entering(name, "getPharmacies");
		
		PharmaciesDB pharmaciesDB = new PharmaciesDB();
		
		GetUserCoordinates getUserCoordinates = new GetUserCoordinates();
		
		String lat = "";
		String lon = "";
		
		JSONObject payload = null;
		
		if(distance.length() < 3) {
			String coordinates = getUserCoordinates.coordinates(street, "1");
			JSONArray jsonarray = new JSONArray(coordinates);
			
			for(int i=0; i<jsonarray.length(); i++){
		        JSONObject obj = jsonarray.getJSONObject(i);

		        lat = obj.getString("lat");
		        lon = obj.getString("lon");
			}
			
			log.log(Level.INFO, "User coordinates getted. lat: " + lat + " - lon: " + lon);
			
			payload = new JSONObject(pharmaciesDB.getPharmacies(variables, distance, lat, lon).toString());
		}
		
		log.exiting(name, "getPharmacies");
		return payload;
	}
}