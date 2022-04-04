package br.com.pucsp.projetointegrado.farmacias.client.password;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.db.DB;
import br.com.pucsp.projetointegrado.farmacias.mail.EmailConfirmation;

public class ChangePasswordDB {
	public static String NAME =  ChangePasswordDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger( ChangePasswordDB.class.getName());
	
	public boolean changePassword(Map<String, String> variables, ChangePass pass, int SESSION_LENGTH) {
		LOG.entering(NAME, "changePassword");
		
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    String time = dtf.format(LocalDateTime.now());
		    
			String getIdRecPass = "SELECT id_rec_senha FROM Rec_Senha WHERE (token_rec_senha LIKE ?);";
			PreparedStatement getIdRecPassStat = DB.connect(variables).prepareStatement(getIdRecPass);
			getIdRecPassStat.setString(1, pass.getToken());
			
			String idRecPassGetted = "";
			ResultSet g = getIdRecPassStat.executeQuery();
			while(g.next()) {
				idRecPassGetted = g.getString(1);
			}
			
			getIdRecPassStat.close();
			
			LOG.log(Level.INFO, "Rec Pass ID getted from User table! Rec_Pass ID: " + idRecPassGetted);
			
			String UpdatePass = "UPDATE Senha SET senha = ? AND data_criacao = ? WHERE (id_rec_senha LIKE ?);";
			
			PreparedStatement getIdPassStat = DB.connect(variables).prepareStatement(UpdatePass);
			getIdPassStat.setString(1, pass.getNewPass());
			getIdPassStat.setString(2, time);
			getIdPassStat.setString(3, idRecPassGetted);
			
			getIdPassStat.execute();
			getIdPassStat.close();
			
			String messageSubject = "Entrega de Farmácias - Senha alterada";
			
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
					+ "      Sua conta foi recuperada!" + "\n"
					+ "    </h1>\n"
					+ "  </div>\n"
					+ "  <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n"
					+ "    <h2 class=\"size-24\" style=\"Margin-top: 0;Margin-bottom: 16px;font-style: normal;font-weight: normal;color: #3d3b3d;font-size: 20px;line-height: 28px;text-align: center;\" lang=\"x-size-24\">\n"
					+ "      " + "Agora você pode usar suas novas informações de segurança para acessar sua conta." + "<br><br>\n"
					+ "    </h2>\n"
					+ "  </div>\n"
					+ "  <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n"
					+ "    <div class=\"btn btn--flat btn--large\" style=\"Margin-bottom: 20px;text-align: center;\">\n"
					+ "      <a style=\"border-radius: 4px;display: inline-block;font-size: 14px;font-weight: bold;line-height: 24px;padding: 12px 24px;text-align: center;text-decoration: none !important;transition: opacity 0.1s ease-in;color: #ffffff !important;background-color: #337ab7;font-family: sans-serif;\" href=\"https://projeto-integrador-frontend.herokuapp.com/login\" target=\"_blank\">\n"
					+ "        Acessar conta\n"
					+ "      </a>\n"
					+ "  </div>\n"
					+ "</body>\n"
					+ "</html>";
			
			EmailConfirmation sendEmail = new EmailConfirmation();
			sendEmail.confirmation(pass.getEmail().toLowerCase(), messageSubject, messageText);
			
			LOG.exiting(NAME, "changePassword");
			return true;
		}
		catch (SQLException e) {
			LOG.log(Level.SEVERE, "ChangePasswordDB.changePassword: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "changePassword");
		return false;
	}
}