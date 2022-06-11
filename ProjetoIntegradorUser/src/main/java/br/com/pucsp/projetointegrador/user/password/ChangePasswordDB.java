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
import br.com.pucsp.projetointegrador.utils.EmailTemplate;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class ChangePasswordDB {
	private static String name =  ChangePasswordDB.class.getSimpleName();
	private static Logger log = Logger.getLogger( ChangePasswordDB.class.getName());
	
	public boolean changePassword(Map<String, String> variables, ChangePass pass) throws MessagingException, ClassNotFoundException, SQLException {
		log.entering(name, "changePassword");
		
		GetFromDB getFromDB = new GetFromDB();
		
		String getIdRecPass = "SELECT * FROM Rec_Senha WHERE (token_rec_senha LIKE ?);";
		PreparedStatement getIdRecPassStat = DB.connect(variables).prepareStatement(getIdRecPass);
		Map<String, String> getUser = new HashMap<String, String>();
		
		try {
			getIdRecPassStat.setString(1, pass.getToken());
			
			getUser = getFromDB.getFromDB(getIdRecPassStat);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			getIdRecPassStat.close();
			DB.disconnect();
		}
		
	    if(getUser.get("id_rec_senha").length() >= 1) {
	    	return updateSession(variables, pass, getUser);
		}
	    
		log.exiting(name, "changePassword");
		return false;
	}
	
	public boolean updateSession(Map<String, String> variables, ChangePass pass, Map<String, String> getUser) throws ClassNotFoundException, SQLException, MessagingException {
		log.entering(name, "updateSession");
		
		String idRecPass =  getUser.get("id_rec_senha");
		
		boolean check = true;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String time = dtf.format(LocalDateTime.now());
		
		String updatePass = "UPDATE Senha SET senha = ?, data_criacao = ? WHERE (id_rec_senha LIKE ?);";
		PreparedStatement getIdPassStat = DB.connect(variables).prepareStatement(updatePass);
		
		try {
			getIdPassStat.setString(1, pass.getNewPass());
			getIdPassStat.setString(2, time);
			getIdPassStat.setString(3, idRecPass);
			
			getIdPassStat.execute();
		}
		catch (SQLException e) {
			check = false;
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			getIdPassStat.close();
			DB.disconnect();
		}
		
		String removeToken = "UPDATE Rec_Senha SET token_rec_senha = ? WHERE (id_rec_senha LIKE ?) AND (token_rec_senha LIKE ?);";
		PreparedStatement statementRemoveToken = DB.connect(variables).prepareStatement(removeToken);
		
		try {
			statementRemoveToken.setString(1, "NULL");
			statementRemoveToken.setString(2, idRecPass);
			statementRemoveToken.setString(3, pass.getToken());
			
			statementRemoveToken.execute();
		}
		catch (SQLException e) {
			check = false;
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementRemoveToken.close();
			DB.disconnect();
		}
		
		String messageSubject = "Entrega de Farmácias - Senha alterada";
		String shortText = "Sua conta foi recuperada.";
		String info = "Agora você pode usar suas novas informações de segurança para acessar sua conta.";
		String btnText = "Acessar conta";
		String btnLink = "https://projeto-integrador-frontend.herokuapp.com/login";
		String messageText = EmailTemplate.template(messageSubject, info, shortText, btnText, btnLink);
		
		EmailConfirmation sendEmail = new EmailConfirmation();
		sendEmail.confirmation(pass.getEmail().toLowerCase(), messageSubject, messageText);
		
		log.exiting(name, "updateSession");
		return check;
	}
}