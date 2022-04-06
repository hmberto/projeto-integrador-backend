package br.com.pucsp.projetointegrado.farmacias.client.pharmacies;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONWriter;

import br.com.pucsp.projetointegrado.farmacias.db.DB;
import br.com.pucsp.projetointegrado.farmacias.db.GetFromDB;
import br.com.pucsp.projetointegrado.farmacias.utils.CalcFeeDelivery;
import br.com.pucsp.projetointegrado.farmacias.utils.CalcTimeDelivery;
import br.com.pucsp.projetointegrado.farmacias.utils.ReplaceImageNames;

public class PharmaciesDB {
	public static String NAME = PharmaciesDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(PharmaciesDB.class.getName());
	
	public StringBuffer getPharmacies(Map <String, String> variables, String distance, String session) {
		LOG.entering(NAME, "getPharmacies");
		
		GetFromDB getFromDB = new GetFromDB();
		
		StringBuffer payload = new StringBuffer();
		JSONWriter createPayload = new JSONWriter(payload);
		
		try {
			String sql1 = "SELECT * FROM Login_Sessao WHERE (id_session LIKE ?);";
			PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
			statement1.setString(1, session);
			
			Map<String, String> getLoginSession = getFromDB.getFromDB(variables, statement1);
			statement1.close();
			
			String sql2 = "SELECT * FROM Usuario WHERE (id_usuario LIKE ?);";
			PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
			statement2.setString(1, getLoginSession.get("id_usuario"));
			
			Map<String, String> getUser = getFromDB.getFromDB(variables, statement2);
			statement2.close();
			
			String sql3 = "SELECT lat, lon FROM Endereco WHERE (id_endereco LIKE ?);";
			PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
			statement3.setString(1, getUser.get("id_endereco"));
			
			Map<String, String> getAddress = getFromDB.getFromDB(variables, statement3);
			statement3.close();
			
			String sql = "SELECT Farmacia.nome, (6371 *\n"
					+ "        acos(\n"
					+ "            cos(radians(" + getAddress.get("lat") + ")) *\n"
					+ "            cos(radians(lat)) *\n"
					+ "            cos(radians(" + getAddress.get("lon") + ") - radians(lon)) +\n"
					+ "            sin(radians(" + getAddress.get("lat") + ")) *\n"
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