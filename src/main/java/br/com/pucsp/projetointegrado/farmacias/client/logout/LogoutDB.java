package br.com.pucsp.projetointegrado.farmacias.client.logout;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.db.DB;

public class LogoutDB {
	public static String NAME = LogoutDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(LogoutDB.class.getName());
	private boolean check = false;
	
	public boolean LogoutUser(String sql, String sessionId) {
		LOG.entering(NAME, "LogoutUser");
		System.out.println("\n\nsession");
		
		String sql2 = "UPDATE client SET session = ? WHERE (email LIKE ?);";
		
		Map<Integer, String> user = new HashMap<Integer, String>();
		Map<Integer, String> session = new HashMap<Integer, String>();
		
		try {
			PreparedStatement statement = DB.connect().prepareStatement(sql);
			statement.setString(1, sessionId);
			
			ResultSet f = statement.executeQuery();
			
			while(f.next()) {
				for(int i = 1; i < 14; i++) {
					user.put(i, f.getString(i));
				}
				
				LOG.log(Level.INFO, "Data geted from the database. Email: " + f.getString(2));
			}
			
			String userSession = "";
			
			PreparedStatement statement2 = DB.connect().prepareStatement(sql2);
			statement2.setString(1, "NULL");
			statement2.setString(2, user.get(2));
			
			statement2.execute();
			
			session.put(1, userSession);
						
			statement.close();
			
			check = true;
		}
		catch (SQLException e) {
			LOG.log(Level.SEVERE, "Data not geted from the database: ", e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "LogoutUser");
		return check;
	}
}