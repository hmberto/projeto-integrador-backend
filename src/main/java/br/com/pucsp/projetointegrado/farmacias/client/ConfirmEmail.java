package br.com.pucsp.projetointegrado.farmacias.client;

import java.util.Map;

import br.com.pucsp.projetointegrado.farmacias.client.confirmemail.ConfirmEmailDB;

public class ConfirmEmail {
	public boolean confirm(Map <String, String> variables, String token, String email) {
		ConfirmEmailDB confirmEmail = new ConfirmEmailDB();
		
		boolean emailMatches = email.matches(variables.get("REGEX_EMAIL"));
		if(email.length() > 10 && email.length() < 60 && emailMatches && token.length() > 10 && token.length() < 250) {
			return confirmEmail.confirmation(variables, token, email);
		}
		return false;
	}
}