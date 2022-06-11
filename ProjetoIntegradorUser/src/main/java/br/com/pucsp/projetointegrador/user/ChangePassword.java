package br.com.pucsp.projetointegrador.user;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.user.password.ChangePass;
import br.com.pucsp.projetointegrador.user.password.ChangePasswordDB;

public class ChangePassword {
	private static String name =  ChangePassword.class.getSimpleName();
	private static Logger log = Logger.getLogger( ChangePassword.class.getName());
	
	public boolean changePass(Map<String, String> variables, ChangePass pass) throws MessagingException, ClassNotFoundException, SQLException {
		log.entering(name, "changePass");
		
		int sessionLength = Integer.parseInt(variables.get("SESSION_LENGTH"));
		boolean checkEmail = pass.getEmail().toLowerCase().matches(variables.get("REGEX_EMAIL"));
		boolean checkPass = pass.getNewPass().matches(variables.get("REGEX_PASS"));
		
		int minPassLength = Integer.parseInt(variables.get("MIN_PASS_LENGTH"));
		int maxPassLength = Integer.parseInt(variables.get("MAX_PASS_LENGTH"));
		
		if(pass.getToken().length() == sessionLength && pass.getEmail().length() < 60 && checkEmail && pass.getNewPass().length() >= minPassLength && pass.getNewPass().length() < maxPassLength && checkPass) {
			ChangePasswordDB changePasswordDB = new ChangePasswordDB();
			
			boolean change = changePasswordDB.changePassword(variables, pass);
			
			log.exiting(name, "changePass");
			return change;
		}
		
		return false;
	}
}