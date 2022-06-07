package br.com.pucsp.projetointegrador.pharmacy.pharmacies;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.pharmacy.db.DB;
import br.com.pucsp.projetointegrador.pharmacy.utils.CalcFeeDelivery;
import br.com.pucsp.projetointegrador.pharmacy.utils.CalcTimeDelivery;
import br.com.pucsp.projetointegrador.pharmacy.utils.LogMessage;
import br.com.pucsp.projetointegrador.pharmacy.utils.ReplaceImageNames;
import br.com.pucsp.projetointegrador.pharmacy.utils.SQLPharmacies;

public class PharmaciesDB {
	private static String name = PharmaciesDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(PharmaciesDB.class.getName());
	
	public StringBuilder getPharmacies(Map <String, String> variables, String distance, String lat, String lon) throws SQLException {
		log.entering(name, "getPharmacies");
		
		StringBuilder payload = new StringBuilder();
		JSONWriter createPayload = new JSONWriter(payload);
		
		String sql = SQLPharmacies.sql(lat, lon, distance);
		
		PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
		
		try {
			ResultSet f = statement.executeQuery();
			
			createPayload.object();
			
			int i = 0;
			while(f.next()) {
				createPayload.key("pharmacy-" + (i+1));
				
				createPayload.object();
				
				double num = Double.parseDouble(f.getString(3));
				String dist = String.format("%.1f", num);
				
				createPayload.key("idPharmacy").value(f.getString(2));
				createPayload.key("name").value(f.getString(1));
				createPayload.key("distance").value(dist + " km");
				createPayload.key("fee").value("R$ " + CalcFeeDelivery.calcFeeDelivery(num).replace(".", ","));
				createPayload.key("time").value(CalcTimeDelivery.calcTimeDelivery(Math.round((num*100.0)/100.0)));
				createPayload.key("imgpath").value(ReplaceImageNames.replaceNames(f.getString(1).toLowerCase()) + ".png");
				
				createPayload.endObject();
				
				i++;
			}
			
			createPayload.endObject();
		}
		catch (Exception e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement.close();
			DB.disconnect();
		}
		
		log.exiting(name, "getPharmacies");
		return payload;
	}
}