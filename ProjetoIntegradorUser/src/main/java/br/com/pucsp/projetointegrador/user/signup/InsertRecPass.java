package br.com.pucsp.projetointegrador.user.signup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;

public class InsertRecPass {
	public static String NAME = InsertRecPass.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(InsertRecPass.class.getName());
	
	public static String insertRecPass(Map<String, String> variables, String pass, String emailSession) {
		LOG.entering(NAME, "insertRecPass");
		String time = "";
		
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    time = dtf.format(LocalDateTime.now());
			
			String sql = "INSERT INTO Rec_Senha (data_rec, token_rec_senha) values (?, ?);";
			PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
			statement.setString(1, time);
			statement.setString(2, emailSession);
			
			statement.execute();
			statement.close();
			
			String getIdRecPass = "SELECT id_rec_senha FROM Rec_Senha WHERE (data_rec LIKE ?) AND (token_rec_senha LIKE ?);";
			
			PreparedStatement getIdRecPassStat = DB.connect(variables).prepareStatement(getIdRecPass);
			getIdRecPassStat.setString(1, time);
			getIdRecPassStat.setString(2, emailSession);
			
			String idRecPassGetted = "";
			
			ResultSet g = getIdRecPassStat.executeQuery();
			while(g.next()) {
				idRecPassGetted = g.getString(1);
			}
			
			getIdRecPassStat.close();
			
			String removeToken = "UPDATE Rec_Senha SET token_rec_senha = ? WHERE (id_rec_senha LIKE ?) AND (token_rec_senha LIKE ?);";
			PreparedStatement statementRemoveToken = DB.connect(variables).prepareStatement(removeToken);
			statementRemoveToken.setString(1, "NULL");
			statementRemoveToken.setString(2, idRecPassGetted);
			statementRemoveToken.setString(3, emailSession);
			
			statementRemoveToken.execute();
			statementRemoveToken.close();
			
			LOG.log(Level.INFO, "User rec pass created at rec pass table! RecPass ID: " + idRecPassGetted);
			LOG.exiting(NAME, "insertPass");
			return idRecPassGetted;
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "insertRecPass.insertRecPass: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "insertRecPass");
		return "";
	}
}