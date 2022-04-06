package br.com.pucsp.projetointegrado.farmacias.client.password;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.db.DB;
import br.com.pucsp.projetointegrado.farmacias.db.GetFromDB;
import br.com.pucsp.projetointegrado.farmacias.mail.EmailConfirmation;
import br.com.pucsp.projetointegrado.farmacias.utils.EmailTemplate;

public class ChangePasswordDB {
	public static String NAME =  ChangePasswordDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger( ChangePasswordDB.class.getName());
	
	public boolean changePassword(Map<String, String> variables, ChangePass pass, int SESSION_LENGTH) {
		LOG.entering(NAME, "changePassword");
		
		GetFromDB getFromDB = new GetFromDB();
		
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    String time = dtf.format(LocalDateTime.now());
		    
			String getIdRecPass = "SELECT * FROM Rec_Senha WHERE (token_rec_senha LIKE ?);";
			PreparedStatement getIdRecPassStat = DB.connect(variables).prepareStatement(getIdRecPass);
			getIdRecPassStat.setString(1, pass.getToken());
			
			Map<String, String> getUser = getFromDB.getFromDB(variables, getIdRecPassStat);
			getIdRecPassStat.close();
			
			if(getUser.get("id_rec_senha").length() >= 1) {
				LOG.log(Level.INFO, "Rec Pass ID getted from User table! Rec_Pass ID: " + getUser.get("id_rec_senha"));
				
				String UpdatePass = "UPDATE Senha SET senha = ?, data_criacao = ? WHERE (id_rec_senha LIKE ?);";
				
				PreparedStatement getIdPassStat = DB.connect(variables).prepareStatement(UpdatePass);
				getIdPassStat.setString(1, pass.getNewPass());
				getIdPassStat.setString(2, time);
				getIdPassStat.setString(3, getUser.get("id_rec_senha"));
				
				getIdPassStat.execute();
				getIdPassStat.close();
				
				String removeToken = "UPDATE Rec_Senha SET token_rec_senha = ? WHERE (id_rec_senha LIKE ?) AND (token_rec_senha LIKE ?);";
				PreparedStatement statementRemoveToken = DB.connect(variables).prepareStatement(removeToken);
				statementRemoveToken.setString(1, "NULL");
				statementRemoveToken.setString(2, getUser.get("id_rec_senha"));
				statementRemoveToken.setString(3, pass.getToken());
				
				statementRemoveToken.execute();
				statementRemoveToken.close();
				
				String messageSubject = "Entrega de Farmácias - Senha alterada";
				String shortText = "Sua conta foi recuperada.";
				String info = "Agora você pode usar suas novas informações de segurança para acessar sua conta.";
				String btnText = "Acessar conta";
				String btnLink = "https://projeto-integrador-frontend.herokuapp.com/login";
				String messageText = EmailTemplate.template(messageSubject, info, shortText, btnText, btnLink);
				
				EmailConfirmation sendEmail = new EmailConfirmation();
				sendEmail.confirmation(pass.getEmail().toLowerCase(), messageSubject, messageText);
				
				LOG.exiting(NAME, "changePassword");
				return true;
			}
		}
		catch (SQLException e) {
			LOG.log(Level.SEVERE, "ChangePasswordDB.changePassword: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "changePassword");
		return false;
	}
}