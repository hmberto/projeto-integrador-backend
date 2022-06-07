package br.com.pucsp.projetointegrador.pharmacy;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.pucsp.projetointegrador.pharmacy.pharmacies.PharmacyDB;

public class PharmacyID {
	private static String name = PharmacyID.class.getSimpleName();
	private static Logger log = Logger.getLogger(PharmacyID.class.getName());
	
	public JSONObject getPharmacy(Map <String, String> variables, String pharmacyId) throws JSONException, SQLException {
		log.entering(name, "getPharmacy");
		
		PharmacyDB pharmacy = new PharmacyDB();
		JSONObject payload = new JSONObject(pharmacy.getPharmacy(variables, pharmacyId).toString());
		
		log.exiting(name, "getPharmacy");
		
		log.exiting(name, "getPharmacy");
		return payload;
	}
}