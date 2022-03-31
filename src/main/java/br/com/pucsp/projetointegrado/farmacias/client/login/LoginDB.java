package br.com.pucsp.projetointegrado.farmacias.client.login;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.db.DB;
import br.com.pucsp.projetointegrado.farmacias.mail.EmailConfirmation;
import br.com.pucsp.projetointegrado.farmacias.utils.GetDateByIP;
import br.com.pucsp.projetointegrado.farmacias.utils.GetZone;

public class LoginDB {
	public static String NAME = LoginDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(LoginDB.class.getName());
	
	public Map<Integer, String> LoginUser(String userAgent, Map<String, String> variables, String email, String pass, String newLogin, String IP) {
		LOG.entering(NAME, "LoginUser");
		
		int SESSION_LENGTH = Integer.parseInt(variables.get("SESSION_LENGTH"));
		
//		String sql = EnvVariables.getEnvVariable("DATABASE_GET_USER");
//		String sql2 = EnvVariables.getEnvVariable("DATABASE_INSERT_USER_SESSION");
		
		String sql1 = "SELECT * FROM Usuario WHERE (email LIKE ?);";
		String sql2 = "SELECT id_login_sessao, id_senha from Login_Sessao where (id_usuario LIKE ?);";
		String sql3 = "SELECT senha from Senha where (senha LIKE ?) AND (id_usuario LIKE ?) AND (ativo LIKE ?);";
		String sql4 = "UPDATE Login_Sessao SET id_session = ? WHERE (id_usuario LIKE ?) AND (id_senha LIKE ?);";
		
		Map<Integer, String> user = new HashMap<Integer, String>();
		Map<Integer, String> session = new HashMap<Integer, String>();
		
		String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String nome = "";
		
		try {
			PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
			statement1.setString(1, email);
			
			String userId = "";
			ResultSet f = statement1.executeQuery();
			while(f.next()) {
				userId = f.getString(1);
				for(int i = 1; i < 3; i++) {
					user.put(i, f.getString(i));
				}
			}
			
			PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
			statement2.setString(1, userId);
			
			String idPass = "";
//			String idLoginSession = "";
			ResultSet g = statement2.executeQuery();
			while(g.next()) {
				idPass = g.getString(2);
//				idLoginSession = g.getString(1);
			}
			
			PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
			statement3.setString(1, pass);
			statement3.setString(2, userId);
			statement3.setBoolean(3, true);
			
			String passDB = "";
			ResultSet h = statement3.executeQuery();
			while(h.next()) {
				passDB = h.getString(1);
			}
			
			if(passDB.equals(pass)) {
				LOG.log(Level.INFO, "Data geted from the database. Email: " + email);
				
				String userSession = "";
				for(int i = 0; i < SESSION_LENGTH; i++) {
					int myindex = (int)(alphaNumeric.length() * Math.random());
					
					userSession = userSession + alphaNumeric.charAt(myindex);
				}
				
				PreparedStatement statement4 = DB.connect(variables).prepareStatement(sql4);
				statement4.setString(1, userSession);
				statement4.setString(2, userId);
				statement4.setString(3, idPass);
				
				statement4.execute();
				
				session.put(1, userSession);
				
				statement1.close();
				statement2.close();
				statement3.close();
				statement4.close();
				
				// email = user.get(6);
				nome = user.get(2);
				if(newLogin.equals("true")) {
					String[] nomeSeparado = nome.split(" ");
					
					String messageSubject = "Entrega de Farmácias - Detectamos um novo acesso à sua conta";
					
					GetDateByIP getDate = new GetDateByIP();
					GetZone getZone = new GetZone();
					String info = userAgent + getDate.getData(IP) + "<br> " + getZone.getZone(IP);
					
					String messageText = "" 
							+ "<!DOCTYPE html>\n"
							+ "<html lang=\"pt-br\">\n"
							+ "<head>\n"
							+ "  <meta charset=\"UTF-8\">\n"
							+ "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
							+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
							+ "  <title>" + messageSubject + "</title>\n"
							+ "</head>\n"
							+ "<body>\n"
							+ "  <div role=\"banner\">\n"
							+ "    <div class=\"header\" style=\"Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);\" id=\"emb-email-header-container\">\n"
							+ "      <div class=\"logo emb-logo-margin-box\" style=\"font-size: 26px;line-height: 32px;Margin-top: 16px;Margin-bottom: 32px;color: #41637e;font-family: sans-serif;Margin-left: 20px;Margin-right: 20px;\" align=\"center\">\n"
							+ "        <div class=\"logo-center\" align=\"center\" id=\"emb-email-header\"><img style=\"display: block;height: auto;width: 100%;border: 0;max-width: 141px;\" src=\"https://i.ibb.co/PzSBwSL/ico.png\" alt width=\"141\"></div>\n"
							+ "      </div>\n"
							+ "    </div>\n"
							+ "  </div>\n"
							+ "  <div>\n"
							+ "    <div class=\"layout one-col fixed-width stack\" style=\"Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;\">\n"
							+ "    <div class=\"layout__inner\" style=\"border-collapse: collapse;display: table;width: 100%;background-color: #ffffff;\">\n"
							+ "    <div class=\"column\" style=\"text-align: left;color: #717a8a;font-size: 16px;line-height: 24px;font-family: sans-serif;\">\n"
							+ "    <div style=\"Margin-left: 20px;Margin-right: 20px;Margin-top: 24px;\">\n"
							+ "  </div>\n"
							+ "  <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n"
							+ "    <h1 style=\"Margin-top: 0;Margin-bottom: 20px;font-style: normal;font-weight: normal;color: #3d3b3d;font-size: 30px;line-height: 38px;text-align: center;\">\n"
							+ "      " + nomeSeparado[0] + ", detectamos um novo acesso à sua conta." + "\n"
							+ "    </h1>\n"
							+ "  </div>\n"
							+ "  <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n"
							+ "    <h2 class=\"size-24\" style=\"Margin-top: 0;Margin-bottom: 16px;font-style: normal;font-weight: normal;color: #3d3b3d;font-size: 20px;line-height: 28px;text-align: center;\" lang=\"x-size-24\">\n"
							+ "      " + info + "<br><br>\n"
							+ "    </h2>\n"
							+ "  </div>\n"
							+ "  <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n"
							+ "    <div class=\"btn btn--flat btn--large\" style=\"Margin-bottom: 20px;text-align: center;\">\n"
							+ "      <a style=\"border-radius: 4px;display: inline-block;font-size: 14px;font-weight: bold;line-height: 24px;padding: 12px 24px;text-align: center;text-decoration: none !important;transition: opacity 0.1s ease-in;color: #ffffff !important;background-color: #337ab7;font-family: sans-serif;\" href=\"https://projeto-integrador-frontend.herokuapp.com/logout/?email=" + email + "\" target=\"_blank\">\n"
							+ "        Não fui eu\n"
							+ "      </a>\n"
							+ "  </div>\n"
							+ "</body>\n"
							+ "</html>";
					
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
		return session;
	}
}