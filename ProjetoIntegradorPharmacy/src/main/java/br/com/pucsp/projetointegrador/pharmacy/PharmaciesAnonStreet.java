package br.com.pucsp.projetointegrador.pharmacy;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.pucsp.projetointegrador.pharmacy.pharmacies.PharmaciesDB;
import br.com.pucsp.projetointegrador.pharmacy.utils.GetUserCoordinates;

public class PharmaciesAnonStreet {
	public static String NAME = PharmaciesAnonStreet.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(PharmaciesAnonStreet.class.getName());
	
	public JSONObject getPharmacies(Map <String, String> variables, String distance, String street, String district, String state, String city) {
		LOG.entering(NAME, "getPharmacies");
		
		PharmaciesDB pharmaciesDB = new PharmaciesDB();
		GetUserCoordinates getUserCoordinates = new GetUserCoordinates();
		
		String lat = "";
		String lon = "";
		
		if(distance.length() < 3) {
			String coordinates = getUserCoordinates.coordinates(street, "1");
			JSONArray jsonarray = new JSONArray(coordinates);
			
			for(int i=0; i<jsonarray.length(); i++){
		        JSONObject obj = jsonarray.getJSONObject(i);

		        lat = obj.getString("lat");
		        lon = obj.getString("lon");
			}
			
			JSONObject payload = new JSONObject(pharmaciesDB.getPharmacies(variables, distance, lat, lon).toString());
			
			LOG.exiting(NAME, "getPharmacies");
			return payload;
		}
		return null;
	}
}