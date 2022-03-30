package br.com.pucsp.projetointegrado.farmacias.client;

import java.util.Map;

import org.json.JSONObject;

import br.com.pucsp.projetointegrado.farmacias.client.pharmacies.PharmaciesDB;

public class Pharmacies {
	public JSONObject getPharmacies(Map <String, String> variables, String distance, String session) {
		PharmaciesDB pharmaciesDB = new PharmaciesDB();
		JSONObject payload = new JSONObject(pharmaciesDB.getPharmacies(variables, distance, session).toString());
		
		return payload;
	}
}