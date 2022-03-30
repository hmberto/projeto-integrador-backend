package br.com.pucsp.projetointegrado.farmacias.client;

import java.util.Map;

import br.com.pucsp.projetointegrado.farmacias.client.login.LoginDB;

public class LogIn {
	public Map<Integer, String> authenticateUser(String userAgent, Map<String, String> variables, String user, String pass, String IP, String newLogin) {
		LoginDB loginDB = new LoginDB();				
		Map<Integer, String> session = loginDB.LoginUser(userAgent, variables, user, pass, newLogin, IP);
		
		return session;
	}
}