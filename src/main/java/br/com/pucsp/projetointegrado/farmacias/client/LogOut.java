package br.com.pucsp.projetointegrado.farmacias.client;

import java.util.Map;

import br.com.pucsp.projetointegrado.farmacias.client.logout.LogoutDB;

public class LogOut {
	public boolean logout(Map <String, String> variables, String session) {
		LogoutDB userFromDb = new LogoutDB();
		
		if(session.matches(variables.get("REGEX_EMAIL"))) {
			String sql = variables.get("LOGOUT_1");
			boolean check = userFromDb.LogoutUser(variables, sql, session);
			
			if(check) { return true; }
		}
		else if(session.length() > 10 && session.length() < 250) {
			String sql = variables.get("LOGOUT_2");
			boolean check = userFromDb.LogoutUser(variables, sql, session);
						
			if(check) { return true; }
		}
		
		return false;
	}
}