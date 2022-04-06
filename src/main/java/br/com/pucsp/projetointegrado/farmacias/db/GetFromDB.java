package br.com.pucsp.projetointegrado.farmacias.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetFromDB {
	public static String NAME = GetFromDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(GetFromDB.class.getName());
	
	public Map<String, String> getFromDB(Map<String, String> variables, PreparedStatement statement) {
		LOG.entering(NAME, "getFromDB");
		
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
			
			LOG.log(Level.INFO, "Data getted from DB! SQL: " + statement);
			LOG.exiting(NAME, "getUser");
			return data;
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "GetFromDB.getFromDB: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "getFromDB");
		return null;
	}
}