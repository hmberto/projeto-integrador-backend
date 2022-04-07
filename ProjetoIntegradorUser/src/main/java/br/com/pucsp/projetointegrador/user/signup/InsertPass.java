package br.com.pucsp.projetointegrador.user.signup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;

public class InsertPass {
	public static String NAME = InsertPass.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(InsertPass.class.getName());
	
	public static String insertPass(Map<String, String> variables, String pass, String userID, String emailSession) {
		LOG.entering(NAME, "insertPass");
		String time = "";
		
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    time = dtf.format(LocalDateTime.now());
		    
			String insertRecPass = InsertRecPass.insertRecPass(variables, pass, emailSession);
			
			String sql = "INSERT INTO Senha (senha, data_criacao, ativo, id_usuario, id_rec_senha) values (?, ?, ?, ?, ?);";
			
			PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
			statement.setString(1, pass);
			statement.setString(2, time);
			statement.setBoolean(3, true);
			statement.setString(4, userID);
			statement.setString(5, insertRecPass);
			
			statement.execute();
			statement.close();
			
			String getIdPass = "SELECT id_senha FROM Senha WHERE (senha LIKE ?) AND (id_usuario LIKE ?);";
			
			PreparedStatement getIdPassStat = DB.connect(variables).prepareStatement(getIdPass);
			getIdPassStat.setString(1, pass);
			getIdPassStat.setString(2, userID);
			
			String idPassGetted = "";
			
			ResultSet g = getIdPassStat.executeQuery();
			while(g.next()) {
				idPassGetted = g.getString(1);
			}
			
			getIdPassStat.close();
			
			LOG.log(Level.INFO, "User Pass created at Pass table! Pass ID: " + idPassGetted);
			LOG.exiting(NAME, "insertPass");
			return idPassGetted;
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "InsertPass.insertPass: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "insertPass");
		return "";
	}
}