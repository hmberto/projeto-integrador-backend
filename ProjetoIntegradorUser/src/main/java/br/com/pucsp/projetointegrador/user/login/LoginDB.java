package br.com.pucsp.projetointegrador.user.login;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.db.GetFromDB;
import br.com.pucsp.projetointegrador.mail.EmailConfirmation;
import br.com.pucsp.projetointegrador.utils.CreateToken;
import br.com.pucsp.projetointegrador.utils.EmailTemplate;
import br.com.pucsp.projetointegrador.utils.GetDate;
import br.com.pucsp.projetointegrador.utils.GetZone;

public class LoginDB {
	public static String NAME = LoginDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(LoginDB.class.getName());
	
	public String LoginUser(String userAgent, Map<String, String> variables, String email, String pass, String newLogin, String IP) {
		LOG.entering(NAME, "LoginUser");
		
		GetFromDB getFromDB = new GetFromDB();
		
		CreateToken createToken = new CreateToken();
		String userSession = createToken.createToken(variables);
		
		try {
			String sql1 = "SELECT * FROM Usuario WHERE (email LIKE ?) AND (ativo LIKE ?);";
			PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
			statement1.setString(1, email);
			statement1.setBoolean(2, true);
			
			Map<String, String> getUser = getFromDB.getFromDB(variables, statement1);
			statement1.close();
			
			String sql2 = "SELECT * from Login_Sessao where (id_usuario LIKE ?);";
			PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
			statement2.setString(1, getUser.get("id_usuario"));
			
			Map<String, String> getSession = getFromDB.getFromDB(variables, statement2);
			statement2.close();
			
			String sql3 = "SELECT * from Senha where (senha LIKE ?) AND (id_usuario LIKE ?) AND (ativo LIKE ?);";
			PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
			statement3.setString(1, pass);
			statement3.setString(2, getUser.get("id_usuario"));
			statement3.setBoolean(3, true);
			
			Map<String, String> getPass = getFromDB.getFromDB(variables, statement3);
			statement3.close();
			
			if(getPass.get("senha").equals(pass)) {
				LOG.log(Level.INFO, "Data geted from the database. Email: " + email);
				
				String sql4 = "UPDATE Login_Sessao SET id_session = ? WHERE (id_usuario LIKE ?) AND (id_senha LIKE ?);";
				PreparedStatement statement4 = DB.connect(variables).prepareStatement(sql4);
				statement4.setString(1, userSession);
				statement4.setString(2, getUser.get("id_usuario"));
				statement4.setString(3, getSession.get("id_senha"));
				
				statement4.execute();
				statement4.close();
				
				if(newLogin.equals("true")) {
					GetZone getZone = new GetZone();
					
					String[] nomeSeparado = getUser.get("nome").split(" ");
					
					String messageSubject = "Entrega de Farmácias - Detectamos um novo acesso à sua conta";
					String shortText = nomeSeparado[0] + ", detectamos um novo acesso à sua conta.";
					String info = userAgent + GetDate.getDate() + "<br>" + getZone.getZone(IP);
					String btnText = "Não fui eu";
					String btnLink = "https://projeto-integrador-frontend.herokuapp.com/logout?email=" + email;
					String messageText = EmailTemplate.template(messageSubject, info, shortText, btnText, btnLink);
					
					EmailConfirmation sendEmail = new EmailConfirmation();
					sendEmail.confirmation(email, messageSubject, messageText);
				}
			}
		}
		catch (SQLException e) {
			LOG.log(Level.SEVERE, "Data not geted from the database: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "LoginUser");
		return userSession;
	}
}