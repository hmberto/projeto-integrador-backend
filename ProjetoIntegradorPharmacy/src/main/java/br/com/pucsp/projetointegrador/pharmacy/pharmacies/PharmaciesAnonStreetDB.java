package br.com.pucsp.projetointegrador.pharmacy.pharmacies;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.pharmacy.db.DB;
import br.com.pucsp.projetointegrador.pharmacy.utils.CalcFeeDelivery;
import br.com.pucsp.projetointegrador.pharmacy.utils.CalcTimeDelivery;
import br.com.pucsp.projetointegrador.pharmacy.utils.GetUserCoordinates;
import br.com.pucsp.projetointegrador.pharmacy.utils.ReplaceImageNames;

public class PharmaciesAnonStreetDB {
	public static String NAME = PharmaciesAnonStreetDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(PharmaciesAnonStreetDB.class.getName());
	
	public StringBuffer getPharmacies(Map <String, String> variables, String distance, String street, String district, String state, String city) {
		LOG.entering(NAME, "getPharmacies");
				
		StringBuffer payload = new StringBuffer();
		JSONWriter createPayload = new JSONWriter(payload);
		
		String lat = "";
		String lon = "";
		
		GetUserCoordinates getUserCoordinates = new GetUserCoordinates();
		
		try {
			String coordinates = getUserCoordinates.coordinates(street, "1");
			JSONArray jsonarray = new JSONArray(coordinates);
			
			for(int i=0; i<jsonarray.length(); i++){
		        JSONObject obj = jsonarray.getJSONObject(i);

		        lat = obj.getString("lat");
		        lon = obj.getString("lon");
			}
			
			String sql = "SELECT Farmacia.nome, (6371 *\n"
					+ "        acos(\n"
					+ "            cos(radians(" + lat + ")) *\n"
					+ "            cos(radians(lat)) *\n"
					+ "            cos(radians(" + lon + ") - radians(lon)) +\n"
					+ "            sin(radians(" + lat + ")) *\n"
					+ "            sin(radians(lat))\n"
					+ "        )) AS distance\n"
					+ "FROM Endereco, Farmacia WHERE Endereco.id_endereco = Farmacia.id_endereco HAVING distance <= " + distance + ";";
			
			PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
			
			ResultSet f = statement.executeQuery();
			
			LOG.log(Level.INFO, "Nearby pharmacies getted from DB");
			
			createPayload.object();
			
			int i = 0;
			while(f.next()) {
				createPayload.key("pharmacy-" + (i+1));
				
				createPayload.object();
				
				double num = Double.parseDouble(f.getString(2));
				String dist = String.format("%.1f", num);
				
				createPayload.key("name").value(f.getString(1));
				createPayload.key("distance").value(dist + " km");
				createPayload.key("fee").value("R$ " + CalcFeeDelivery.calcFeeDelivery(num).replace(".", ","));
				createPayload.key("time").value(CalcTimeDelivery.calcTimeDelivery(Math.round((num*100.0)/100.0)));
				createPayload.key("imgpath").value(ReplaceImageNames.replaceNames(f.getString(1).toLowerCase()) + ".png");
				
				createPayload.endObject();
				
				i++;
			}
			
			createPayload.endObject();
			
			statement.close();
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "Data not geted from the database: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		// LOG.exiting(NAME, "GetCar");
		LOG.log(Level.INFO, "Nearby pharmacies payload: " + payload);
		LOG.exiting(NAME, "getPharmacies");
		return payload;
	}
}