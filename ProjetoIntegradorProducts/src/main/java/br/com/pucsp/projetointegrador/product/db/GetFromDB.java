package br.com.pucsp.projetointegrador.product.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.product.utils.LogMessage;

public class GetFromDB {
	private static String name = GetFromDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(GetFromDB.class.getName());
	
	public Map<String, String> getFromDB(PreparedStatement statement) {
		log.entering(name, "getFromDB");
		
		try {
			Map<String, String> data = new HashMap<String, String>();
			
			ResultSet g = statement.executeQuery();
			ResultSetMetaData h = g.getMetaData();
			int columnCount = h.getColumnCount();
			
			while(g.next()) {
				for (int i = 1; i <= columnCount; i++) {
					data.put(h.getColumnName(i), g.getString(i));
				}
			}
			
			statement.close();
			
			log.log(Level.INFO, "Data getted from DB! SQL: " + statement);
			log.exiting(name, "getUser");
			return data;
		}
		catch (Exception e) {
			log.log(Level.SEVERE, LogMessage.message(e.toString()));
		}
		finally {
			DB.disconnect();
		}
		
		log.exiting(name, "getFromDB");
		return null;
	}
}