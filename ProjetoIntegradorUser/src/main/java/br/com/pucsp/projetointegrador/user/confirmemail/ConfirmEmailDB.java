package br.com.pucsp.projetointegrador.user.confirmemail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.db.GetFromDB;
import br.com.pucsp.projetointegrador.mail.EmailConfirmation;
import br.com.pucsp.projetointegrador.utils.EmailTemplate;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class ConfirmEmailDB {
	private static String name = ConfirmEmailDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(ConfirmEmailDB.class.getName());
	
	public boolean confirmation(Map <String, String> variables, String token, String email) throws MessagingException, ClassNotFoundException, SQLException {
		log.entering(name, "confirmation");
		
		GetFromDB getFromDB = new GetFromDB();
		
		String sql1 = "UPDATE Usuario SET ativo = true WHERE (token_confirmacao_cadastro LIKE ?) AND (email LIKE ?);";
		String sql2 = "UPDATE Usuario SET token_confirmacao_cadastro = ? WHERE (email LIKE ?);";
		
		String sql3 = "SELECT * FROM Usuario WHERE (email LIKE ?);";
		PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
		
		Map<String, String> getUser = new HashMap<String, String>();
		
		try {
			statement3.setString(1, email);
			
			getUser = getFromDB.getFromDB(statement3);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement3.close();
			DB.disconnect();
		}
		
		PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
		
		try {
			statement1.setString(1, token);
			statement1.setString(2, email);
			
			statement1.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement1.close();
			DB.disconnect();
		}
		
		PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
		
		try {
			statement2.setString(1, "NULL");
			statement2.setString(2, email);
			
			statement2.execute();
			
			String messageSubject = "Entrega de Farmácias - E-mail confirmado";
			String shortText = "Obrigado por confirmar sua conta.";
			String info = "Agora você pode realizar suas compras e receber no conforto da sua casa. <br><br>Realize login na plataforma e comece a comprar.";
			String btnText = "Comprar";
			String btnLink = "https://projeto-integrador-frontend.herokuapp.com";
			String messageText = EmailTemplate.template(messageSubject, info, shortText, btnText, btnLink);
			
			if(getUser.get("ativo").equals("false") || getUser.get("ativo").equals("0")) {
				EmailConfirmation sendEmail = new EmailConfirmation();
				sendEmail.confirmation(email, messageSubject, messageText);
			}
			
			log.exiting(name, "confirmation");
			return true;
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement2.close();
			DB.disconnect();
		}
	}
}