package br.com.pucsp.projetointegrador.pharmacy;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.pucsp.projetointegrador.pharmacy.pharmacies.PharmacyDB;

public class PharmacyID {
	public static String NAME = PharmacyID.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(PharmacyID.class.getName());
	
	public JSONObject getPharmacy(Map <String, String> variables, String pharmacyId) throws JSONException, SQLException {
		LOG.entering(NAME, "getPharmacy");
		
		PharmacyDB pharmacy = new PharmacyDB();
		JSONObject payload = new JSONObject(pharmacy.getPharmacy(variables, pharmacyId).toString());
		
		LOG.exiting(NAME, "getPharmacy");
		
		LOG.exiting(NAME, "getPharmacy");
		return payload;
	}
}