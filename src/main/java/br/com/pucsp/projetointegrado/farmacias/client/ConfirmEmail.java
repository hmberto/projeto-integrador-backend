package br.com.pucsp.projetointegrado.farmacias.client;

import br.com.pucsp.projetointegrado.farmacias.client.confirmemail.ConfirmEmailDB;

public class ConfirmEmail {
	public boolean confirm(String token, String email) {
		ConfirmEmailDB confirmEmail = new ConfirmEmailDB();
		
		if(email.length() > 10 && token.length() > 10 && token.length() < 250) {
			boolean check = confirmEmail.confirmation(token, email);
						
			if(check) { return true; }
			else { return false; }
		}
		else {
			return false;
		}
	}
}