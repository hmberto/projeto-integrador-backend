package br.com.pucsp.projetointegrador.user.logout;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;

public class LogoutDB {
	public static String NAME = LogoutDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(LogoutDB.class.getName());
	private boolean check = false;
	
	public boolean LogoutUser(Map <String, String> variables, String sessionId) {
		LOG.entering(NAME, "LogoutUser");
		
		String sql = "UPDATE Login_Sessao SET id_sessao = ? WHERE (id_sessao LIKE ?);";
		
		try {
			PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
			statement.setString(1, "NULL");
			statement.setString(2, sessionId);
			
			statement.execute();			
			statement.close();
			
			check = true;
		}
		catch (SQLException e) {
			LOG.log(Level.SEVERE, "LogoutDB.LogoutUser Error: : " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "LogoutUser");
		return check;
	}
}