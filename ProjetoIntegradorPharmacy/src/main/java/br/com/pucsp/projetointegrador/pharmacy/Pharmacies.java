package br.com.pucsp.projetointegrador.pharmacy;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.pharmacy.pharmacies.PharmaciesDB;
import br.com.pucsp.projetointegrador.pharmacy.utils.GetUserCoordinates;

public class Pharmacies {
	private static String name = Pharmacies.class.getSimpleName();
	private static Logger log = Logger.getLogger(Pharmacies.class.getName());
	
	public JSONObject getPharmacies(Map <String, String> variables, String distance, String session) throws SQLException {
		log.entering(name, "getPharmacies");
		
		PharmaciesDB pharmaciesDB = new PharmaciesDB();
		
		JSONObject payload = null;
		
		int sessionLength = Integer.parseInt(variables.get("SESSION_LENGTH"));
		
		if(distance.length() < 3 && session.length() == sessionLength) {
			GetUserCoordinates getUserCoordinates = new GetUserCoordinates();
			Map<String, String> getAddress = getUserCoordinates.coordinatesFromDB(variables, session);
			
			payload = new JSONObject(pharmaciesDB.getPharmacies(variables, distance, getAddress.get("lat"), getAddress.get("lon")).toString());
		}
		
		log.exiting(name, "getPharmacies");
		return payload;
	}
}