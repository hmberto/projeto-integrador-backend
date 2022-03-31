package br.com.pucsp.projetointegrado.farmacias.client;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrado.farmacias.client.pharmacies.PharmaciesDB;

public class Pharmacies {
	public static String NAME = Pharmacies.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(Pharmacies.class.getName());
	
	public JSONObject getPharmacies(Map <String, String> variables, String distance, String session) {
		LOG.entering(NAME, "getPharmacies");
		PharmaciesDB pharmaciesDB = new PharmaciesDB();
		JSONObject payload = new JSONObject(pharmaciesDB.getPharmacies(variables, distance, session).toString());
		
		LOG.exiting(NAME, "getPharmacies");
		return payload;
	}
}