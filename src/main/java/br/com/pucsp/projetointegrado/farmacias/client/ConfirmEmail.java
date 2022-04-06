package br.com.pucsp.projetointegrado.farmacias.client;

import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.client.confirmemail.ConfirmEmailDB;

public class ConfirmEmail {
	public static String NAME = ConfirmEmail.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(ConfirmEmail.class.getName());
	
	public boolean confirm(Map <String, String> variables, String token, String email) {
		LOG.entering(NAME, "confirm");
		
		int SESSION_LENGTH = Integer.parseInt(variables.get("SESSION_LENGTH"));
		
		ConfirmEmailDB confirmEmail = new ConfirmEmailDB();
		
		boolean emailMatches = email.matches(variables.get("REGEX_EMAIL"));
		if(email.length() > 10 && email.length() < 60 && emailMatches && token.length() == SESSION_LENGTH) {
			return confirmEmail.confirmation(variables, token, email);
		}
		
		LOG.exiting(NAME, "confirm");
		return false;
	}
}