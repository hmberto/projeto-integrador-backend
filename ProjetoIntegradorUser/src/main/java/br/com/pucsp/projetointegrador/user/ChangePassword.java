package br.com.pucsp.projetointegrador.user;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.user.password.ChangePass;
import br.com.pucsp.projetointegrador.user.password.ChangePasswordDB;

public class ChangePassword {
	public static String NAME =  ChangePassword.class.getSimpleName();
	private static Logger LOG = Logger.getLogger( ChangePassword.class.getName());
	
	public boolean changePass(Map<String, String> variables, ChangePass pass) {
		LOG.entering(NAME, "changePass");
		
		int SESSION_LENGTH = Integer.parseInt(variables.get("SESSION_LENGTH"));
		
		if(pass.getToken().length() == SESSION_LENGTH) {
			boolean checkEmail = pass.getEmail().toLowerCase().matches(variables.get("REGEX_EMAIL"));
			if(pass.getEmail().length() < 60 && checkEmail) {
				LOG.log(Level.INFO, "ChangePassword.changePass: Email OK");
				
				int MIN_PASS_LENGTH = Integer.parseInt(variables.get("MIN_PASS_LENGTH"));
				int MAX_PASS_LENGTH = Integer.parseInt(variables.get("MAX_PASS_LENGTH"));
				
				boolean checkPass = pass.getNewPass().matches(variables.get("REGEX_PASS"));
				if(pass.getNewPass().length() >= MIN_PASS_LENGTH && pass.getNewPass().length() < MAX_PASS_LENGTH && checkPass) {
					LOG.log(Level.INFO, "ChangePassword.changePass: Pass OK");
					
					ChangePasswordDB changePasswordDB = new ChangePasswordDB();
					
					boolean change = changePasswordDB.changePassword(variables, pass, SESSION_LENGTH);
					LOG.exiting(NAME, "changePass");
					return change;
				}
				else { 
					LOG.log(Level.SEVERE, "ChangePassword.changePass: Incorrect Pass");
				}
			}
			else { 
				LOG.log(Level.SEVERE, "ChangePassword.changePass: Incorrect Email");
			}
		}
		else { 
			LOG.log(Level.SEVERE, "ChangePassword.changePass: Incorrect Token");
		}
		
		LOG.exiting(NAME, "changePass");
		return false;
	}
}