package br.com.pucsp.projetointegrador.user;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.user.password.NewPass;
import br.com.pucsp.projetointegrador.user.password.RecNewPasswordDB;

public class RecNewPassword {
	private static String name =  RecNewPassword.class.getSimpleName();
	private static Logger log = Logger.getLogger( RecNewPassword.class.getName());
	
	public boolean changePass(Map<String, String> variables, NewPass pass) throws MessagingException, ClassNotFoundException, SQLException {
		log.entering(name, "changePass");
		
		boolean checkEmail = pass.getEmail().toLowerCase().matches(variables.get("REGEX_EMAIL"));
		if(pass.getEmail().length() < 60 && checkEmail) {
			RecNewPasswordDB recNewPasswordDB = new RecNewPasswordDB();
			
			log.exiting(name, "changePass");
			
			return recNewPasswordDB.newPassword(variables, pass);
		}
		
		return false;
	}
}