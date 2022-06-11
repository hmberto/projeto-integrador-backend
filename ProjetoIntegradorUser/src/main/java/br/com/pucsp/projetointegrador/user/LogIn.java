package br.com.pucsp.projetointegrador.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.user.login.LogInUser;
import br.com.pucsp.projetointegrador.user.login.LoginDB;

public class LogIn {
	private static String name = LogIn.class.getSimpleName();
	private static Logger log = Logger.getLogger(LogIn.class.getName());
	
	public Map<Integer, String> authenticateUser(Map<String, String> variables, LogInUser login, String userAgent, String ip) throws MessagingException, ClassNotFoundException, SQLException, IOException {
		log.entering(name, "authenticateUser");
		
		boolean check = false;
		
		int maxPassLength = Integer.parseInt(variables.get("MAX_PASS_LENGTH"));
		int minPassLength = Integer.parseInt(variables.get("MIN_PASS_LENGTH"));
		
		boolean emailMatches = login.getEmail().matches(variables.get("REGEX_EMAIL"));
		boolean passMatches = login.getPass().matches(variables.get("REGEX_PASS"));
		
		if(login.getEmail().length() > 10 && login.getEmail().length() < 60 && emailMatches && login.getPass().length() >= minPassLength && login.getPass().length() < maxPassLength && passMatches) {
			check = true;
		}
		
		Map<Integer, String> session = new HashMap<Integer, String>();
		
		LoginDB loginDB = new LoginDB();
		
		if (check) {
			String userSession = loginDB.loginUser(userAgent, variables, login.getEmail(), login.getPass(), login.getNewLogin(), ip);
			
			session.put(1, userSession);
		}
		
		log.exiting(name, "authenticateUser");
		return session;
	}
}