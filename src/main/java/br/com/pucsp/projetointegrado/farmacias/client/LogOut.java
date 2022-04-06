package br.com.pucsp.projetointegrado.farmacias.client;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.client.logout.LogoutDB;
import br.com.pucsp.projetointegrado.farmacias.db.DB;
import br.com.pucsp.projetointegrado.farmacias.db.GetFromDB;

public class LogOut {
	public static String NAME = LogOut.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(LogOut.class.getName());
	
	public boolean logout(Map <String, String> variables, String session) {
		LOG.entering(NAME, "logout");
		
		int SESSION_LENGTH = Integer.parseInt(variables.get("SESSION_LENGTH"));
		
		LogoutDB userFromDb = new LogoutDB();
		GetFromDB getFromDB = new GetFromDB();
		
		if(session.toLowerCase().matches(variables.get("REGEX_EMAIL"))) {
			try {
				String sql1 = "SELECT * FROM Usuario WHERE (email LIKE ?);";
				PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
				statement1.setString(1, session.toLowerCase());
				
				Map<String, String> getUser = getFromDB.getFromDB(variables, statement1);
				
				String sql2 = "SELECT * FROM Login_Sessao WHERE (id_usuario LIKE ?);";
				PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
				statement2.setString(1, getUser.get("id_usuario"));
				
				Map<String, String> getLoginSession = getFromDB.getFromDB(variables, statement2);
				
				boolean check = userFromDb.LogoutUser(variables, getLoginSession.get("id_session"));
				
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