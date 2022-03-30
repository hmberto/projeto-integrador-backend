package br.com.pucsp.projetointegrado.farmacias.client.pharmacies;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONWriter;

import br.com.pucsp.projetointegrado.farmacias.db.DB;

public class PharmaciesDB {
	public StringBuffer getPharmacies(Map <String, String> variables, String distance, String session) {		
		String sql1 = variables.get("PHARMACY");
		
		Map<Integer, String> user = new HashMap<Integer, String>();
		
		StringBuffer payload = new StringBuffer();
		JSONWriter createPayload = new JSONWriter(payload);
		
		try {
			PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
			statement1.setString(1, session);
			
			ResultSet f1 = statement1.executeQuery();
			
			while(f1.next()) {
				for(int i = 1; i < 18; i++) {
					user.put(1, f1.getString(17));
					user.put(2, f1.getString(18));
				}
			}
			
			statement1.close();
			
			String sql = "SELECT *, (6371 *\n"
					+ "        acos(\n"
					+ "            cos(radians(" + user.get(1) + ")) *\n"
					+ "            cos(radians(lat)) *\n"
					+ "            cos(radians(" + user.get(2) + ") - radians(lon)) +\n"
					+ "            sin(radians(" + user.get(1) + ")) *\n"
					+ "            sin(radians(lat))\n"
					+ "        )) AS distance\n"
					+ "FROM pharmacies HAVING distance <= " + distance + ";";
			
			PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
						
			ResultSet f = statement.executeQuery();
			
			createPayload.object();
			
			int i = 0;
			while(f.next()) {
				createPayload.key("pharmacy " + (i+1));
				
				createPayload.object();
				
				double num = Double.parseDouble(f.getString(5));
				
				createPayload.key("name").value(f.getString(2));
				createPayload.key("distance").value(Math.round(num*100.0)/100.0);
				
				createPayload.endObject();
				
				i++;
			}
			
			createPayload.endObject();
			
			statement.close();
		}
		catch (SQLException e) {
			// LOG.log(Level.SEVERE, "Data not geted from the database: ", e);
			System.out.println(e);
		}
		finally {
			DB.disconnect();
		}
		
		// LOG.exiting(NAME, "GetCar");
		return payload;
	}
}