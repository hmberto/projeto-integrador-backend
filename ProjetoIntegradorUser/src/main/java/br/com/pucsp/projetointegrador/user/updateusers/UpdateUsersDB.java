package br.com.pucsp.projetointegrador.user.updateusers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.db.GetFromDB;
import br.com.pucsp.projetointegrador.mail.EmailConfirmation;
import br.com.pucsp.projetointegrador.user.signup.InsertSex;
import br.com.pucsp.projetointegrador.utils.EmailTemplate;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class UpdateUsersDB {
	private static String name = UpdateUsersDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(UpdateUsersDB.class.getName());
	
	public boolean updateUsersDB(Map<String, String> variables, UpdateUsers user, String lat, String lon) throws SQLException, MessagingException, ClassNotFoundException {
		log.entering(name, "updateUsersDB");
		
		GetFromDB getFromDB = new GetFromDB();
		
		String sqlUser = "SELECT * FROM Usuario WHERE (email LIKE ?);";
		PreparedStatement statementUser = DB.connect(variables).prepareStatement(sqlUser);
		Map<String, String> getUser = new HashMap<String, String>();
		
		try {
			statementUser.setString(1, user.getEmail());
			
			getUser = getFromDB.getFromDB(statementUser);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementUser.close();
			DB.disconnect();
		}
		
		UpdateAddress.updateAddress(variables, user, lat, lon, getUser.get("id_endereco"));
		String idSex = InsertSex.insertSex(variables, user.getSex());
		
		String sql = "UPDATE Usuario SET nome = ?, id_genero = ? WHERE (email LIKE ?);";	
		PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
		
		try {
			statement.setString(1, user.getName());
			statement.setString(2, idSex);
			statement.setString(3, user.getEmail().toLowerCase());
						
			statement.execute();
		}
		catch(SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement.close();
			DB.disconnect();
		}
		
		String[] nomeSeparado = user.getName().split(" ");
		
		String messageSubject = "Entrega de Farmácias - Cadastro atualizado";
		String shortText = nomeSeparado[0] + ", as informações da sua conta foram atualizadas.";
		String info = "As novas informações já constam na sua conta e você pode continuar comprando em nossa plataforma.";
		String btnText = "Comprar";
		String btnLink = "https://pharmacy-delivery.herokuapp.com";
		String messageText = EmailTemplate.template(messageSubject, info, shortText, btnText, btnLink);
		
		EmailConfirmation sendEmail = new EmailConfirmation();
		sendEmail.confirmation(user.getEmail().toLowerCase(), messageSubject, messageText);
		
		log.exiting(name, "updateUsersDB");
		return true;
	}
}