package br.com.pucsp.projetointegrador.pharmacy;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.pharmacy.pharmacies.PharmaciesDB;

public class PharmaciesAnon {
	public static String NAME = PharmaciesAnon.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(PharmaciesAnon.class.getName());
	
	public JSONObject getPharmacies(Map <String, String> variables, String distance, String lat, String lon) {
		LOG.entering(NAME, "getPharmacies");
		PharmaciesDB pharmaciesDB = new PharmaciesDB();
		
		if(distance.length() < 3) {
			JSONObject payload = new JSONObject(pharmaciesDB.getPharmacies(variables, distance, lat, lon).toString());
			
			LOG.exiting(NAME, "getPharmacies");
			return payload;
		}
		return null;
	}
}