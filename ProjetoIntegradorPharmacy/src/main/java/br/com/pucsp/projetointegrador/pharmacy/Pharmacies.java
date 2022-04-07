package br.com.pucsp.projetointegrador.pharmacy;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.pharmacy.pharmacies.PharmaciesDB;

public class Pharmacies {
	public static String NAME = Pharmacies.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(Pharmacies.class.getName());
	
	public JSONObject getPharmacies(Map <String, String> variables, String distance, String session) {
		LOG.entering(NAME, "getPharmacies");
		PharmaciesDB pharmaciesDB = new PharmaciesDB();
		
		int SESSION_LENGTH = Integer.parseInt(variables.get("SESSION_LENGTH"));
		if(distance.length() < 3) {
			if(session.length() == SESSION_LENGTH) {
				JSONObject payload = new JSONObject(pharmaciesDB.getPharmacies(variables, distance, session).toString());
				
				LOG.exiting(NAME, "getPharmacies");
				return payload;
			}
		}
		return null;
	}
}