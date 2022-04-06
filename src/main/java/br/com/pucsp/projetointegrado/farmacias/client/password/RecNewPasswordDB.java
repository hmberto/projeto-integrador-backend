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

public class RecNewPasswordDB {
	public static String NAME =  RecNewPasswordDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger( RecNewPasswordDB.class.getName());
	
	public boolean newPassword(Map<String, String> variables, NewPass pass, int SESSION_LENGTH) {
		LOG.entering(NAME, "newPassword");
		
		try {
			String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			String emailSession = "";
			
			for(int i = 0; i < SESSION_LENGTH; i++) {
				int myindex = (int)(alphaNumeric.length() * Math.random());
				
				emailSession = emailSession + alphaNumeric.charAt(myindex);
			}
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    String time = dtf.format(LocalDateTime.now());
		    
			String getIdUser = "SELECT id_usuario FROM Usuario WHERE (email LIKE ?);";
			PreparedStatement getIdUserStat = DB.connect(variables).prepareStatement(getIdUser);
			getIdUserStat.setString(1, pass.getEmail());
			
			String idUserGetted = "";
			ResultSet g = getIdUserStat.executeQuery();
			while(g.next()) {
				idUserGetted = g.getString(1);
			}
			
			getIdUserStat.close();
			
			LOG.log(Level.INFO, "User ID getted from User table! User ID: " + idUserGetted);
			
			String getIdRecPass = "SELECT id_rec_senha FROM Senha WHERE (id_usuario LIKE ?);";
			
			PreparedStatement getIdPassStat = DB.connect(variables).prepareStatement(getIdRecPass);
			getIdPassStat.setInt(1, Integer.parseInt(idUserGetted));
			
			String idRecPassGetted = "";
			ResultSet i = getIdPassStat.executeQuery();
			while(i.next()) {
				idRecPassGetted = i.getString(1);
			}
			
			getIdPassStat.close();
			
			LOG.log(Level.INFO, "Rec_Senha ID getted from Rec_Senha table! Rec_Senha ID: " + idRecPassGetted);
			
			String updatePass = "UPDATE Rec_Senha SET data_rec = ?, token_rec_senha = ? WHERE (id_rec_senha LIKE ?);";
			
			PreparedStatement updateRecPassStat = DB.connect(variables).prepareStatement(updatePass);
			updateRecPassStat.setString(1, time);
			updateRecPassStat.setString(2, emailSession);
			updateRecPassStat.setString(3, idRecPassGetted);
			
			updateRecPassStat.execute();
			updateRecPassStat.close();
			
			String messageSubject = "Entrega de Farmácias - Recuperação de conta";
			
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
					+ "      Recuperação de senha!" + "\n"
					+ "    </h1>\n"
					+ "  </div>\n"
					+ "  <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n"
					+ "    <h2 class=\"size-24\" style=\"Margin-top: 0;Margin-bottom: 16px;font-style: normal;font-weight: normal;color: #3d3b3d;font-size: 20px;line-height: 28px;text-align: center;\" lang=\"x-size-24\">\n"
					+ "      " + "Você está a um passo de recuperar sua conta. Clique no link abaixo para alterar a senha da sua conta." + "<br><br>\n"
					+ "    </h2>\n"
					+ "  </div>\n"
					+ "  <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n"
					+ "    <div class=\"btn btn--flat btn--large\" style=\"Margin-bottom: 20px;text-align: center;\">\n"
					+ "      <a style=\"border-radius: 4px;display: inline-block;font-size: 14px;font-weight: bold;line-height: 24px;padding: 12px 24px;text-align: center;text-decoration: none !important;transition: opacity 0.1s ease-in;color: #ffffff !important;background-color: #337ab7;font-family: sans-serif;\" href=\"https://projeto-integrador-frontend.herokuapp.com/recuperacao/" + pass.getEmail() + "/" + emailSession + "\" target=\"_blank\">\n"
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
			LOG.log(Level.SEVERE, "RecNewPasswordDB.newPassword: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "newPassword");
		return false;
	}
}