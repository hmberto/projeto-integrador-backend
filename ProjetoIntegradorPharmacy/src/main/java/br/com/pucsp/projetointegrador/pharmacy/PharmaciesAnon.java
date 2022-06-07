package br.com.pucsp.projetointegrador.pharmacy;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.pucsp.projetointegrador.pharmacy.pharmacies.PharmaciesDB;

public class PharmaciesAnon {
	private static String name = PharmaciesAnon.class.getSimpleName();
	private static Logger log = Logger.getLogger(PharmaciesAnon.class.getName());
	
	public JSONObject getPharmacies(Map <String, String> variables, String distance, String lat, String lon) throws JSONException, SQLException {
		log.entering(name, "getPharmacies");
		
		PharmaciesDB pharmaciesDB = new PharmaciesDB();
		
		if(distance.length() < 3) {
			JSONObject payload = new JSONObject(pharmaciesDB.getPharmacies(variables, distance, lat, lon).toString());
			
			log.exiting(name, "getPharmacies");
			return payload;
		}
		return null;
	}
}