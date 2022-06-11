package br.com.pucsp.projetointegrador.user.password;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.db.GetFromDB;
import br.com.pucsp.projetointegrador.mail.EmailConfirmation;
import br.com.pucsp.projetointegrador.utils.CreateToken;
import br.com.pucsp.projetointegrador.utils.EmailTemplate;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class RecNewPasswordDB {
	private static String name =  RecNewPasswordDB.class.getSimpleName();
	private static Logger log = Logger.getLogger( RecNewPasswordDB.class.getName());
	
	public boolean newPassword(Map<String, String> variables, NewPass pass) throws MessagingException, ClassNotFoundException, SQLException {
		log.entering(name, "newPassword");
		
		GetFromDB getFromDB = new GetFromDB();
		
		CreateToken createToken = new CreateToken();
		String emailSession = createToken.createToken(variables);
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String time = dtf.format(LocalDateTime.now());
	    
	    String getIdUser = "SELECT * FROM Usuario WHERE (email LIKE ?);";
		PreparedStatement getIdUserStat = DB.connect(variables).prepareStatement(getIdUser);
		Map<String, String> getUser = new HashMap<String, String>();
		
		try {
			getIdUserStat.setString(1, pass.getEmail());
			
			getUser = getFromDB.getFromDB(getIdUserStat);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			getIdUserStat.close();
			DB.disconnect();
		}
		
		String getIdRecPass = "SELECT * FROM Senha WHERE (id_usuario LIKE ?);";
		PreparedStatement getIdPassStat = DB.connect(variables).prepareStatement(getIdRecPass);
		Map<String, String> getPass = new HashMap<String, String>();
		
		try {
			getIdPassStat.setInt(1, Integer.parseInt(getUser.get("id_usuario")));
			
			getPass = getFromDB.getFromDB(getIdPassStat);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			getIdPassStat.close();
			DB.disconnect();
		}
		
		String updatePass = "UPDATE Rec_Senha SET data_rec = ?, token_rec_senha = ? WHERE (id_rec_senha LIKE ?);";
		PreparedStatement updateRecPassStat = DB.connect(variables).prepareStatement(updatePass);
		
		try {
			updateRecPassStat.setString(1, time);
			updateRecPassStat.setString(2, emailSession);
			updateRecPassStat.setString(3, getPass.get("id_rec_senha"));
			
			updateRecPassStat.execute();
			
			String messageSubject = "Entrega de Farmácias - Recuperação de conta";
			String shortText = "Recuperação de senha.";
			String info = "Você está a um passo de recuperar sua conta. Clique no link abaixo para alterar a senha da sua conta.";
			String btnText = "Recuperar conta";
			String btnLink = "https://projeto-integrador-frontend.herokuapp.com/recuperacao?email=" + pass.getEmail() + "&token=" + emailSession;
			String messageText = EmailTemplate.template(messageSubject, info, shortText, btnText, btnLink);
			
			EmailConfirmation sendEmail = new EmailConfirmation();
			sendEmail.confirmation(pass.getEmail().toLowerCase(), messageSubject, messageText);
			
			log.exiting(name, "changePassword");
			return true;
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			updateRecPassStat.close();
			DB.disconnect();
		}
	}
}