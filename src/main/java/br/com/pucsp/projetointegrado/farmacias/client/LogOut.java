package br.com.pucsp.projetointegrado.farmacias.client;

import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.client.logout.LogoutDB;

public class LogOut {
	public static String NAME = LogOut.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(LogOut.class.getName());
	
	public boolean logout(Map <String, String> variables, String session) {
		LOG.entering(NAME, "logout");
		
		LogoutDB userFromDb = new LogoutDB();
		
		if(session.matches(variables.get("REGEX_EMAIL"))) {
			String sql = variables.get("LOGOUT_1");
			boolean check = userFromDb.LogoutUser(variables, sql, session);
			
			if(check) { 
				LOG.exiting(NAME, "logout");
				return true; 
			}
		}
		else if(session.length() > 10 && session.length() < 250) {
			String sql = variables.get("LOGOUT_2");
			boolean check = userFromDb.LogoutUser(variables, sql, session);
						
			if(check) {
				LOG.exiting(NAME, "logout");
				return true; 
			}
		}
		
		LOG.exiting(NAME, "logout");
		return false;
	}
}