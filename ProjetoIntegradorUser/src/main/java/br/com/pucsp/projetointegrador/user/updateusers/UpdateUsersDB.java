package br.com.pucsp.projetointegrador.user.updateusers;

import java.sql.PreparedStatement;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.db.GetFromDB;
import br.com.pucsp.projetointegrador.mail.EmailConfirmation;
import br.com.pucsp.projetointegrador.user.signup.InsertSex;
import br.com.pucsp.projetointegrador.utils.EmailTemplate;

public class UpdateUsersDB {
	public static String NAME = UpdateUsersDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(UpdateUsersDB.class.getName());
	
	public boolean updateUsersDB(Map<String, String> variables, UpdateUsers user, String lat, String lon) {
		LOG.entering(NAME, "updateUsersDB");
		
		GetFromDB getFromDB = new GetFromDB();
		
		try {
			String sqlUser = "SELECT * FROM Usuario WHERE (email LIKE ?);";
			PreparedStatement statementUser = DB.connect(variables).prepareStatement(sqlUser);
			statementUser.setString(1, user.getEmail());
			
			Map<String, String> getUser = getFromDB.getFromDB(variables, statementUser);
			statementUser.close();
			
			UpdateAddress.updateAddress(variables, user, lat, lon, getUser.get("id_endereco"));
			String idSex = InsertSex.insertSex(variables, user.getSex());
			
			String sql = "UPDATE Usuario SET nome = ?, id_genero = ? WHERE (email LIKE ?);";	
			
			PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
			
			statement.setString(1, user.getName());
			statement.setString(2, idSex);
			statement.setString(3, user.getEmail().toLowerCase());
						
			statement.execute();
			statement.close();
			
			LOG.log(Level.INFO, "User updated on database. ID: " + getUser.get("id_usuario") + " - Name: " + user.getName() + " - Email: " + user.getEmail().toLowerCase());
			
			String[] nomeSeparado = user.getName().split(" ");
			
			String messageSubject = "Entrega de Farmácias - Cadastro atualizado";
			String shortText = nomeSeparado[0] + ", as informações da sua conta foram atualizadas.";
			String info = "As novas informações já constam na sua conta e você pode continuar comprando em nossa plataforma.";
			String btnText = "Comprar";
			String btnLink = "https://pharmacy-delivery.herokuapp.com";
			String messageText = EmailTemplate.template(messageSubject, info, shortText, btnText, btnLink);
			
			EmailConfirmation sendEmail = new EmailConfirmation();
			sendEmail.confirmation(user.getEmail().toLowerCase(), messageSubject, messageText);
			
			LOG.exiting(NAME, "updateUsersDB");
			return true;
		}
		catch(Exception e) {
			LOG.log(Level.SEVERE, "User not updated on the database: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "updateUsersDB");
		return false;
	}
}