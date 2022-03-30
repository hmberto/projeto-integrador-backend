package br.com.pucsp.projetointegrado.farmacias.client.signup;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.db.DB;
import br.com.pucsp.projetointegrado.farmacias.mail.EmailConfirmation;

public class SignUpDB {
	public static String NAME = SignUpDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(SignUpDB.class.getName());
	
	public boolean CreateUserDB(Map <String, String> variables, CreateUsers user, String lat, String lon) {
		LOG.entering(NAME, "CreateUserDB");
		
		int SESSION_LENGTH = Integer.parseInt(variables.get("SESSION_LENGTH"));
		
		String sql = variables.get("SIGNUP");
		
		String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String emailSession = "";
		
		for(int i = 0; i < SESSION_LENGTH; i++) {
			int myindex = (int)(alphaNumeric.length() * Math.random());
			
			emailSession = emailSession + alphaNumeric.charAt(myindex);
		}
		
		try {
			PreparedStatement statement = DB.connect(variables).prepareStatement(sql);

			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getPass());
			statement.setString(4, user.getStreet());
			statement.setString(5, user.getNumber());
			statement.setString(6, user.getComplement());
			statement.setString(7, user.getZipCode());
			statement.setString(8, user.getState());
			statement.setString(9, user.getCity());
			statement.setString(10, user.getDistrict());
			statement.setString(11, user.getCpf());
			statement.setString(12, user.getBirthDate());
			statement.setString(13, user.getSex());
			statement.setString(14, emailSession);
			statement.setBoolean(15, false);
			statement.setString(16, lat);
			statement.setString(17, lon);
						
			statement.execute();

			LOG.log(Level.INFO, "User created on database. Email: " + user.getEmail());
			
			statement.close();
			
			String welcome = "";
			if (user.getSex().equals("2")) {
				welcome = "Bem vindo, ";
			} else if (user.getSex().equals("3")) {
				welcome = "Bem vinda, ";
			} else {
				welcome = "Bem vindx, ";
			}
			
			String[] nomeSeparado = user.getName().split(" ");
			
			String messageSubject = "Entrega de Farmácias - Confirme seu e-mail";
			
			String messageText = "<!DOCTYPE html>\n"
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
					+ "      " + welcome + nomeSeparado[0] + "! Confirme que este é seu endereço de e-mail." + "\n"
					+ "    </h1>\n"
					+ "  </div>\n"
					+ "  <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n"
					+ "    <h2 class=\"size-24\" style=\"Margin-top: 0;Margin-bottom: 16px;font-style: normal;font-weight: normal;color: #3d3b3d;font-size: 20px;line-height: 28px;text-align: center;\" lang=\"x-size-24\">\n"
					+ "      " + "Clique no link abaixo para confirmar seu e-mail e liberar o acesso ao site.<br><br>Se você não é " + user.getName() + ", desconsidere este e-mail." + "<br><br>\n"
					+ "    </h2>\n"
					+ "  </div>\n"
					+ "  <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n"
					+ "    <div class=\"btn btn--flat btn--large\" style=\"Margin-bottom: 20px;text-align: center;\">\n"
					+ "      <a style=\"border-radius: 4px;display: inline-block;font-size: 14px;font-weight: bold;line-height: 24px;padding: 12px 24px;text-align: center;text-decoration: none !important;transition: opacity 0.1s ease-in;color: #ffffff !important;background-color: #337ab7;font-family: sans-serif;\" href=\"https://pharmacy-delivery.herokuapp.com/client/confirm-email/" + user.getEmail() + "/" + emailSession + "\" target=\"_blank\">\n"
					+ "        Confirmar e-mail\n"
					+ "      </a>\n"
					+ "  </div>\n"
					+ "</body>\n"
					+ "</html>";
			
			EmailConfirmation sendEmail = new EmailConfirmation();
			sendEmail.confirmation(user.getEmail(), messageSubject, messageText);
			
			LOG.exiting(NAME, "CreateUserDB");
			return true;
		}
		catch(SQLException e) {
			LOG.log(Level.SEVERE, "User not created on the database: ", e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "CreateUserDB");
		return false;
	}
}