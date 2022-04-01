package br.com.pucsp.projetointegrado.farmacias.client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.client.logout.LogoutDB;
import br.com.pucsp.projetointegrado.farmacias.db.DB;

public class LogOut {
	public static String NAME = LogOut.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(LogOut.class.getName());
	
	public boolean logout(Map <String, String> variables, String session, int SESSION_LENGTH) {
		LOG.entering(NAME, "logout");
		
		LogoutDB userFromDb = new LogoutDB();
		
		if(session.toLowerCase().matches(variables.get("REGEX_EMAIL"))) {
			String sql1 = "SELECT id_usuario FROM Usuario WHERE (email LIKE ?);";
			String sql2 = "SELECT id_session FROM Login_Sessao WHERE (id_usuario LIKE ?);";
			
			try {
				PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
				statement1.setString(1, session.toLowerCase());
				
				String userId = "";
				ResultSet g = statement1.executeQuery();
				while(g.next()) {
					userId = g.getString(1);
				}
				
				PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
				statement2.setString(1, userId);
				
				String sessionId = "";
				ResultSet h = statement2.executeQuery();
				while(h.next()) {
					sessionId = h.getString(1);
				}
				
				boolean check = userFromDb.LogoutUser(variables, sessionId);
				
				if(check) { 
					LOG.exiting(NAME, "logout");
					return true; 
				}
			} catch (SQLException e) {
				LOG.log(Level.SEVERE, "LogOut.logout Error:: " + e);
			}
		}
		else if(session.length() == SESSION_LENGTH) {
			boolean check = userFromDb.LogoutUser(variables, session);
						
			if(check) {
				LOG.exiting(NAME, "logout");
				return true; 
			}
		}
		
		LOG.exiting(NAME, "logout");
		return false;
	}
}