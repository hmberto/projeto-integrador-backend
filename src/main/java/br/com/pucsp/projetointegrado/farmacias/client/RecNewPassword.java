package br.com.pucsp.projetointegrado.farmacias.client;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.client.password.NewPass;
import br.com.pucsp.projetointegrado.farmacias.client.password.RecNewPasswordDB;

public class RecNewPassword {
	public static String NAME =  RecNewPassword.class.getSimpleName();
	private static Logger LOG = Logger.getLogger( RecNewPassword.class.getName());
	
	public boolean changePass(Map<String, String> variables, NewPass pass) {
		LOG.entering(NAME, "changePass");
		
		int SESSION_LENGTH = Integer.parseInt(variables.get("SESSION_LENGTH"));
		
		boolean checkEmail = pass.getEmail().toLowerCase().matches(variables.get("REGEX_EMAIL"));
		if(pass.getEmail().length() < 60 && checkEmail) {
			LOG.log(Level.INFO, "RecNewPassword.changePass: Email OK");
			
			RecNewPasswordDB recNewPasswordDB = new RecNewPasswordDB();
			boolean change = recNewPasswordDB.newPassword(variables, pass, SESSION_LENGTH);
			LOG.exiting(NAME, "changePass");
			return change;
		}
		else { 
			LOG.log(Level.SEVERE, "RecNewPassword.changePass: Incorrect Email");
		}
		
		LOG.exiting(NAME, "changePass");
		return false;
	}
}