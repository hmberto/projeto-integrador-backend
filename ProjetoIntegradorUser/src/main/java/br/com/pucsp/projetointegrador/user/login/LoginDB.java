package br.com.pucsp.projetointegrador.user.login;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import java.util.logging.Logger;

import javax.mail.MessagingException;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.db.GetFromDB;
import br.com.pucsp.projetointegrador.mail.EmailConfirmation;
import br.com.pucsp.projetointegrador.utils.CreateToken;
import br.com.pucsp.projetointegrador.utils.EmailTemplate;
import br.com.pucsp.projetointegrador.utils.GetDate;
import br.com.pucsp.projetointegrador.utils.GetZone;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class LoginDB {
	private static String name = LoginDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(LoginDB.class.getName());
	
	public String loginUser(String userAgent, Map<String, String> variables, String email, String pass, String newLogin, String ip) throws MessagingException, ClassNotFoundException, SQLException, IOException {
		log.entering(name, "LoginUser");
		
		GetFromDB getFromDB = new GetFromDB();
		
		CreateToken createToken = new CreateToken();
		String userSession = createToken.createToken(variables);
		
		String sql1 = "SELECT * FROM Usuario WHERE (email LIKE ?) AND (ativo LIKE ?);";
		PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
		Map<String, String> getUser = new HashMap<String, String>();
		
		try {
			statement1.setString(1, email);
			statement1.setBoolean(2, true);
			
			getUser = getFromDB.getFromDB(statement1);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement1.close();
			DB.disconnect();
		}
		
		String sql2 = "SELECT * from Login_Sessao where (id_usuario LIKE ?);";
		PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
		Map<String, String> getSession = new HashMap<String, String>();
		
		String userId = getUser.get("id_usuario");
		
		try {
			statement2.setString(1, userId);
			
			getSession = getFromDB.getFromDB(statement2);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement2.close();
			DB.disconnect();
		}
		
		String sql3 = "SELECT * from Senha where (senha LIKE ?) AND (id_usuario LIKE ?) AND (ativo LIKE ?);";
		PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
		Map<String, String> getPass = new HashMap<String, String>();
		
		try {
			statement3.setString(1, pass);
			statement3.setString(2, userId);
			statement3.setBoolean(3, true);
			
			getPass = getFromDB.getFromDB(statement3);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement3.close();
			DB.disconnect();
		}
		
		if(getPass.get("senha").equals(pass)) {
			insertToken(variables, userSession, getUser, getSession);
			notifyUser(userAgent, getUser, newLogin, email, ip);
		}
		
		log.exiting(name, "LoginUser");
		return userSession;
	}
	
	public void insertToken(Map<String, String> variables, String userSession, Map<String, String> getUser, Map<String, String> getSession) throws ClassNotFoundException, SQLException {
		String sql4 = "UPDATE Login_Sessao SET id_sessao = ? WHERE (id_usuario LIKE ?) AND (id_senha LIKE ?);";
		PreparedStatement statement4 = DB.connect(variables).prepareStatement(sql4);
		
		try {
			statement4.setString(1, userSession);
			statement4.setString(2, getUser.get("id_usuario"));
			statement4.setString(3, getSession.get("id_senha"));
			
			statement4.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement4.close();
			DB.disconnect();
		}
	}
	
	public void notifyUser(String userAgent, Map<String, String> getUser, String newLogin, String email, String ip) throws MessagingException, IOException {
		if(newLogin.equals("true")) {
			GetZone getZone = new GetZone();
			
			String[] nomeSeparado = getUser.get("nome").split(" ");
			
			String messageSubject = "Entrega de Farmácias - Detectamos um novo acesso à sua conta";
			String shortText = nomeSeparado[0] + ", detectamos um novo acesso à sua conta.";
			String info = userAgent + GetDate.getDate() + "<br>" + getZone.getZone(ip);
			String btnText = "Não fui eu";
			String btnLink = "https://projeto-integrador-frontend.herokuapp.com/logout?email=" + email;
			String messageText = EmailTemplate.template(messageSubject, info, shortText, btnText, btnLink);
			
			EmailConfirmation sendEmail = new EmailConfirmation();
			sendEmail.confirmation(email, messageSubject, messageText);
		}
	}
}