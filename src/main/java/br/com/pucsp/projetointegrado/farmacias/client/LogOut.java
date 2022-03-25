package br.com.pucsp.projetointegrado.farmacias.client;

import br.com.pucsp.projetointegrado.farmacias.client.logout.LogoutDB;

public class LogOut {
	public boolean logout(int SESSION_LENGTH, String session) {
		LogoutDB userFromDb = new LogoutDB();
		
		if(session.matches("[0-9 a-z A-Z .]+@[0-9 a-z A-Z .]+")) {
			String sql = "SELECT * FROM client WHERE (email LIKE ?);";
			boolean check = userFromDb.LogoutUser(sql, session);
			
			if(check) { return true; }
			else { return false; }
		}
		else if(session.length() > 10 && session.length() < 250) {
			String sql = "SELECT * FROM client WHERE (session LIKE ?);";
			boolean check = userFromDb.LogoutUser(sql, session);
						
			if(check) { return true; }
			else { return false; }
		}
		else {
			return false;
		}
	}
}