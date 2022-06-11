package br.com.pucsp.projetointegrador.order.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.order.utils.LogMessage;

public class GetFromDB {
	private static String name = GetFromDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(GetFromDB.class.getName());
	
	public Map<String, String> getFromDB(PreparedStatement statement) throws SQLException {
		String methodName = "getFromDB";
		log.entering(name, methodName);
		
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
			
			log.exiting(name, methodName);
			return data;
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			DB.disconnect();
		}
	}
}