package br.com.pucsp.projetointegrador.user.confirmemail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.db.GetFromDB;
import br.com.pucsp.projetointegrador.mail.EmailConfirmation;
import br.com.pucsp.projetointegrador.utils.EmailTemplate;

public class ConfirmEmailDB {
	public static String NAME = ConfirmEmailDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(ConfirmEmailDB.class.getName());
	
	public boolean confirmation(Map <String, String> variables, String token, String email) {
		LOG.entering(NAME, "confirmation");
		
		GetFromDB getFromDB = new GetFromDB();
		
		String sql1 = "UPDATE Usuario SET ativo = true WHERE (token_confirmacao_cadastro LIKE ?) AND (email LIKE ?);";
		String sql2 = "UPDATE Usuario SET token_confirmacao_cadastro = ? WHERE (email LIKE ?);";
		
		try {
			String sql3 = "SELECT * FROM Usuario WHERE (email LIKE ?);";
			PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
			statement3.setString(1, email);
			
			Map<String, String> getUser = getFromDB.getFromDB(variables, statement3);
			
			PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
			statement1.setString(1, token);
			statement1.setString(2, email);
			
			statement1.execute();
			statement1.close();
			
			PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
			statement2.setString(1, "NULL");
			statement2.setString(2, email);
			
			statement2.execute();
			statement2.close();
			
			LOG.log(Level.INFO, "Successfully confirmation - E-mail: " + email);
			
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
			
			LOG.exiting(NAME, "confirmation");
			return true;
		}
		catch (SQLException e) {
			LOG.log(Level.SEVERE, "Failed confirmation - E-mail: " + email + " - Erro: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "confirmation");
		return false;
	}
}