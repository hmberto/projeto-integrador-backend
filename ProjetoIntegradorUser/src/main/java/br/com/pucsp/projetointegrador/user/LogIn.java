package br.com.pucsp.projetointegrador.user;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.user.login.LoginDB;

public class LogIn {
	public static String NAME = LogIn.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(LogIn.class.getName());
	
	public Map<Integer, String> authenticateUser(String userAgent, Map<String, String> variables, String user, String pass, String IP, String newLogin) {
		LOG.entering(NAME, "authenticateUser");
		
		Map<Integer, String> session = new HashMap<Integer, String>();
		
		LoginDB loginDB = new LoginDB();				
		String userSession = loginDB.LoginUser(userAgent, variables, user, pass, newLogin, IP);
		
		session.put(1, userSession);
		
		LOG.exiting(NAME, "authenticateUser");
		return session;
	}
}