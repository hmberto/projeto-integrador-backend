package br.com.pucsp.projetointegrado.farmacias.client.pharmacies;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONWriter;

import br.com.pucsp.projetointegrado.farmacias.db.DB;
import br.com.pucsp.projetointegrado.farmacias.utils.CalcFeeDelivery;
import br.com.pucsp.projetointegrado.farmacias.utils.CalcTimeDelivery;
import br.com.pucsp.projetointegrado.farmacias.utils.ReplaceImageNames;

public class PharmaciesDB {
	public static String NAME = PharmaciesDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(PharmaciesDB.class.getName());
	
	public StringBuffer getPharmacies(Map <String, String> variables, String distance, String session) {
		LOG.entering(NAME, "getPharmacies");
		
		String sql1 = "SELECT id_usuario FROM Login_Sessao WHERE (id_session LIKE ?);";
		String sql2 = "SELECT id_endereco FROM Usuario WHERE (id_usuario LIKE ?);";
		String sql3 = "SELECT lat, lon FROM Endereco WHERE (id_endereco LIKE ?);";
		
		Map<Integer, String> user = new HashMap<Integer, String>();
		
		StringBuffer payload = new StringBuffer();
		JSONWriter createPayload = new JSONWriter(payload);
		
		try {
			PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
			statement1.setString(1, session);
			
			ResultSet f1 = statement1.executeQuery();
			
			while(f1.next()) {
				user.put(1, f1.getString(1));
				
				LOG.log(Level.INFO, "PharmaciesDB.getPharmacies: User ID: " + user.get(1));
			}
			
			PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
			statement2.setString(1, user.get(1));
			
			ResultSet f2 = statement2.executeQuery();
			
			while(f2.next()) {
				user.put(2, f2.getString(1));
				
				LOG.log(Level.INFO, "PharmaciesDB.getPharmacies: Address ID: " + user.get(2));
			}
			
			PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
			statement3.setString(1, user.get(2));
			
			ResultSet f3 = statement3.executeQuery();
			
			while(f3.next()) {
				user.put(3, f3.getString(1));
				user.put(4, f3.getString(2));
				
				LOG.log(Level.INFO, "User coordinates found - Lat: " + user.get(3) + " - Lon: " + user.get(4));
			}
			
			statement1.close();
			statement2.close();
			statement3.close();
			
			String sql = "SELECT Farmacia.nome, (6371 *\n"
					+ "        acos(\n"
					+ "            cos(radians(" + user.get(3) + ")) *\n"
					+ "            cos(radians(lat)) *\n"
					+ "            cos(radians(" + user.get(4) + ") - radians(lon)) +\n"
					+ "            sin(radians(" + user.get(3) + ")) *\n"
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
				String dist = String.format("%.2f", num);
				
				createPayload.key("name").value(f.getString(1));
				createPayload.key("distance").value(dist + " km");
				createPayload.key("fee").value("R$ " + CalcFeeDelivery.calcFeeDelivery(num));
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