package br.com.pucsp.projetointegrador.user.logout;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class LogoutDB {
	private static String name = LogoutDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(LogoutDB.class.getName());
	private boolean check = false;
	
	public boolean logoutUser(Map <String, String> variables, String sessionId) throws ClassNotFoundException, SQLException {
		log.entering(name, "LogoutUser");
		
		String sql = "UPDATE Login_Sessao SET id_sessao = ? WHERE (id_sessao LIKE ?);";
		PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
		
		try {
			statement.setString(1, "NULL");
			statement.setString(2, sessionId);
			
			statement.execute();			
			
			check = true;
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement.close();
			DB.disconnect();
		}
		
		log.exiting(name, "LogoutUser");
		return check;
	}
}