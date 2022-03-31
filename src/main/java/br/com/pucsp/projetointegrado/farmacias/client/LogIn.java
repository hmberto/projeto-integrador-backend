package br.com.pucsp.projetointegrado.farmacias.client;

import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.client.login.LoginDB;

public class LogIn {
	public static String NAME = LogIn.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(LogIn.class.getName());
	
	public Map<Integer, String> authenticateUser(String userAgent, Map<String, String> variables, String user, String pass, String IP, String newLogin) {
		LOG.entering(NAME, "authenticateUser");
		
		LoginDB loginDB = new LoginDB();				
		Map<Integer, String> session = loginDB.LoginUser(userAgent, variables, user, pass, newLogin, IP);
		
		LOG.exiting(NAME, "authenticateUser");
		return session;
	}
}