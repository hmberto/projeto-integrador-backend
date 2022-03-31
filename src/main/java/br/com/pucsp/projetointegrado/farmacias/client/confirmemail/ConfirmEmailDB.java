package br.com.pucsp.projetointegrado.farmacias.client.confirmemail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrado.farmacias.db.DB;
import br.com.pucsp.projetointegrado.farmacias.mail.EmailConfirmation;

public class ConfirmEmailDB {
	public static String NAME = ConfirmEmailDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(ConfirmEmailDB.class.getName());
	
	public boolean confirmation(Map <String, String> variables, String token, String email) {
		LOG.entering(NAME, "confirmation");
		
		String sql = variables.get("EMAIL_CONFIRMATION_1");
		String sql1 = variables.get("EMAIL_CONFIRMATION_2");
		String sql2 = variables.get("EMAIL_CONFIRMATION_3");
		
		try {
			PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
			statement1.setString(1, email);
			
			ResultSet f = statement1.executeQuery();
			
			boolean validate = false;
			while(f.next()) {
				if(f.getBoolean(16) == false && f.getString(2).equals(email)) {
					validate = true;
				}
			}
						
			if(validate) {
				PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
				statement.setString(1, token);
				statement.setString(2, email);
				
				statement.execute();
				statement.close();
				
				PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
				statement2.setString(1, "NULL");
				statement2.setString(2, email);
				
				statement2.execute();
				statement2.close();
				
				LOG.log(Level.INFO, "Successfully confirmation - E-mail: " + email);
				
				String messageSubject = "Entrega de Farmácias - E-mail confirmado";

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
						+ "      " + "Obrigado por confirmar sua conta." + "\n"
						+ "    </h1>\n"
						+ "  </div>\n"
						+ "  <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n"
						+ "    <h2 class=\"size-24\" style=\"Margin-top: 0;Margin-bottom: 16px;font-style: normal;font-weight: normal;color: #3d3b3d;font-size: 20px;line-height: 28px;text-align: center;\" lang=\"x-size-24\">\n"
						+ "      " + "Agora você pode realizar suas compras e receber no conforto da sua casa. <br><br>Realize login na plataforma e comece a comprar." + "<br><br>\n"
						+ "    </h2>\n"
						+ "  </div>\n"
						+ "  <div style=\"Margin-left: 20px;Margin-right: 20px;\">\n"
						+ "    <div class=\"btn btn--flat btn--large\" style=\"Margin-bottom: 20px;text-align: center;\">\n"
						+ "      <a style=\"border-radius: 4px;display: inline-block;font-size: 14px;font-weight: bold;line-height: 24px;padding: 12px 24px;text-align: center;text-decoration: none !important;transition: opacity 0.1s ease-in;color: #ffffff !important;background-color: #337ab7;font-family: sans-serif;\" href=\"https://projeto-integrador-frontend.herokuapp.com\" target=\"_blank\">\n"
						+ "        Comprar\n"
						+ "      </a>\n"
						+ "  </div>\n"
						+ "</body>\n"
						+ "</html>";
				
				EmailConfirmation sendEmail = new EmailConfirmation();
				sendEmail.confirmation(email, messageSubject, messageText);
				
				LOG.exiting(NAME, "confirmation");
				return true;
			}
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