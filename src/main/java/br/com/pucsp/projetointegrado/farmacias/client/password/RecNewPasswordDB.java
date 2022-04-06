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
import br.com.pucsp.projetointegrado.farmacias.utils.CreateToken;
import br.com.pucsp.projetointegrado.farmacias.utils.EmailTemplate;

public class RecNewPasswordDB {
	public static String NAME =  RecNewPasswordDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger( RecNewPasswordDB.class.getName());
	
	public boolean newPassword(Map<String, String> variables, NewPass pass, int SESSION_LENGTH) {
		LOG.entering(NAME, "newPassword");
		
		GetFromDB getFromDB = new GetFromDB();
		
		CreateToken createToken = new CreateToken();
		String emailSession = createToken.createToken(variables);
		
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    String time = dtf.format(LocalDateTime.now());
		    
			String getIdUser = "SELECT * FROM Usuario WHERE (email LIKE ?);";
			PreparedStatement getIdUserStat = DB.connect(variables).prepareStatement(getIdUser);
			getIdUserStat.setString(1, pass.getEmail());
			
			Map<String, String> getUser = getFromDB.getFromDB(variables, getIdUserStat);
			getIdUserStat.close();
			
			LOG.log(Level.INFO, "User ID getted from User table! User ID: " + getUser.get("id_usuario"));
			
			String getIdRecPass = "SELECT * FROM Senha WHERE (id_usuario LIKE ?);";
			PreparedStatement getIdPassStat = DB.connect(variables).prepareStatement(getIdRecPass);
			getIdPassStat.setInt(1, Integer.parseInt(getUser.get("id_usuario")));
			
			Map<String, String> getPass = getFromDB.getFromDB(variables, getIdPassStat);
			getIdPassStat.close();
			
			LOG.log(Level.INFO, "Rec_Senha ID getted from Rec_Senha table! Rec_Senha ID: " + getPass.get("id_rec_senha"));
			
			String updatePass = "UPDATE Rec_Senha SET data_rec = ?, token_rec_senha = ? WHERE (id_rec_senha LIKE ?);";
			
			PreparedStatement updateRecPassStat = DB.connect(variables).prepareStatement(updatePass);
			updateRecPassStat.setString(1, time);
			updateRecPassStat.setString(2, emailSession);
			updateRecPassStat.setString(3, getPass.get("id_rec_senha"));
			
			updateRecPassStat.execute();
			updateRecPassStat.close();
			
			String messageSubject = "Entrega de Farmácias - Recuperação de conta";
			String shortText = "Recuperação de senha.";
			String info = "Você está a um passo de recuperar sua conta. Clique no link abaixo para alterar a senha da sua conta.";
			String btnText = "Recuperar conta";
			String btnLink = "https://projeto-integrador-frontend.herokuapp.com/recuperacao?email=" + pass.getEmail() + "&token=" + emailSession;
			String messageText = EmailTemplate.template(messageSubject, info, shortText, btnText, btnLink);
			
			EmailConfirmation sendEmail = new EmailConfirmation();
			sendEmail.confirmation(pass.getEmail().toLowerCase(), messageSubject, messageText);
			
			LOG.exiting(NAME, "changePassword");
			return true;
		}
		catch (SQLException e) {
			LOG.log(Level.SEVERE, "RecNewPasswordDB.newPassword: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "newPassword");
		return false;
	}
}