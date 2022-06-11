package br.com.pucsp.projetointegrador.user;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.user.confirmemail.ConfirmEmailDB;

public class ConfirmEmail {
	private static String nome = ConfirmEmail.class.getSimpleName();
	private static Logger log = Logger.getLogger(ConfirmEmail.class.getName());
	
	public boolean confirm(Map <String, String> variables, String token, String email) throws MessagingException, ClassNotFoundException, SQLException {
		log.entering(nome, "confirm");
		
		int sessionLength= Integer.parseInt(variables.get("SESSION_LENGTH"));
		
		ConfirmEmailDB confirmEmail = new ConfirmEmailDB();
		
		boolean emailMatches = email.matches(variables.get("REGEX_EMAIL"));
		if(email.length() > 10 && email.length() < 60 && emailMatches && token.length() == sessionLength) {
			return confirmEmail.confirmation(variables, token, email);
		}
		
		log.exiting(nome, "confirm");
		return false;
	}
}