package br.com.pucsp.projetointegrador.pharmacy;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.pharmacy.pharmacies.PharmaciesAnonStreetDB;

public class PharmaciesAnonStreet {
	public static String NAME = PharmaciesAnonStreet.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(PharmaciesAnonStreet.class.getName());
	
	public JSONObject getPharmacies(Map <String, String> variables, String distance, String street, String district, String state, String city) {
		LOG.entering(NAME, "getPharmacies");
		PharmaciesAnonStreetDB pharmaciesDB = new PharmaciesAnonStreetDB();
		
		if(distance.length() < 3) {
			JSONObject payload = new JSONObject(pharmaciesDB.getPharmacies(variables, distance, street, district, state, city).toString());
			
			LOG.exiting(NAME, "getPharmacies");
			return payload;
		}
		return null;
	}
}