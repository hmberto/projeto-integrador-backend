package br.com.pucsp.projetointegrador.user.signup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class InsertRecPass {
	private static String name = InsertRecPass.class.getSimpleName();
	private static Logger log = Logger.getLogger(InsertRecPass.class.getName());
	
	private InsertRecPass () {}
	
	public static String insertRecPass(Map<String, String> variables, String emailSession) throws SQLException, ClassNotFoundException {
		log.entering(name, "insertRecPass");
		String time = "";
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		time = dtf.format(LocalDateTime.now());
		
		String sql = "INSERT INTO Rec_Senha (data_rec, token_rec_senha) values (?, ?);";
		PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
		
		try {
			statement.setString(1, time);
			statement.setString(2, emailSession);
			
			statement.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement.close();
			DB.disconnect();
		}
		
		String getIdRecPass = "SELECT id_rec_senha FROM Rec_Senha WHERE (data_rec LIKE ?) AND (token_rec_senha LIKE ?);";
		PreparedStatement getIdRecPassStat = DB.connect(variables).prepareStatement(getIdRecPass);
		String idRecPassGetted = "";
		
		try {
			getIdRecPassStat.setString(1, time);
			getIdRecPassStat.setString(2, emailSession);
			
			ResultSet g = getIdRecPassStat.executeQuery();
			while(g.next()) {
				idRecPassGetted = g.getString(1);
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			getIdRecPassStat.close();
			DB.disconnect();
		}
		
		String removeToken = "UPDATE Rec_Senha SET token_rec_senha = ? WHERE (id_rec_senha LIKE ?) AND (token_rec_senha LIKE ?);";
		PreparedStatement statementRemoveToken = DB.connect(variables).prepareStatement(removeToken);
		
		try {
			statementRemoveToken.setString(1, "NULL");
			statementRemoveToken.setString(2, idRecPassGetted);
			statementRemoveToken.setString(3, emailSession);
			
			statementRemoveToken.execute();
		}
		catch (Exception e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementRemoveToken.close();
			DB.disconnect();
		}
		
		log.exiting(name, "insertPass");
		return idRecPassGetted;
	}
}