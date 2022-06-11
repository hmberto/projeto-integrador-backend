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

public class InsertPass {
	private static String name = InsertPass.class.getSimpleName();
	private static Logger log = Logger.getLogger(InsertPass.class.getName());
	
	private InsertPass () {}
	
	public static String insertPass(Map<String, String> variables, String pass, String userID, String emailSession) throws SQLException, ClassNotFoundException {
		log.entering(name, "insertPass");
		String time = "";
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		time = dtf.format(LocalDateTime.now());
		
		String insertRecPass = InsertRecPass.insertRecPass(variables, emailSession);
		String sql = "INSERT INTO Senha (senha, data_criacao, ativo, id_usuario, id_rec_senha) values (?, ?, ?, ?, ?);";
		PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
		
		try {
			statement.setString(1, pass);
			statement.setString(2, time);
			statement.setBoolean(3, true);
			statement.setString(4, userID);
			statement.setString(5, insertRecPass);
			
			statement.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement.close();
			DB.disconnect();
		}
		
		String getIdPass = "SELECT id_senha FROM Senha WHERE (senha LIKE ?) AND (id_usuario LIKE ?);";
		PreparedStatement getIdPassStat = DB.connect(variables).prepareStatement(getIdPass);
		String idPassGetted = "";
		
		try {
			getIdPassStat.setString(1, pass);
			getIdPassStat.setString(2, userID);
			
			ResultSet g = getIdPassStat.executeQuery();
			while(g.next()) {
				idPassGetted = g.getString(1);
			}
			
			getIdPassStat.close();
		}
		catch (Exception e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			getIdPassStat.close();
			DB.disconnect();
		}
		
		log.exiting(name, "insertPass");
		return idPassGetted;
	}
}