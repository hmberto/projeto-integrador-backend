package br.com.pucsp.projetointegrador.user;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.db.GetFromDB;
import br.com.pucsp.projetointegrador.user.logout.LogoutDB;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class LogOut {
	private static String name = LogOut.class.getSimpleName();
	private static Logger log = Logger.getLogger(LogOut.class.getName());
	
	LogoutDB userFromDb = new LogoutDB();
	GetFromDB getFromDB = new GetFromDB();
	
	public boolean logout(Map <String, String> variables, String session) throws ClassNotFoundException, SQLException {
		String methodName = "logout";
		log.entering(name, methodName);
		
		if(session.toLowerCase().matches(variables.get("REGEX_EMAIL"))) {
			log.exiting(name, methodName);
			return makeLogout(variables, session);
		}
		else if(session.length() == Integer.parseInt(variables.get("SESSION_LENGTH"))) {
			log.exiting(name, methodName);
			return userFromDb.logoutUser(variables, session);
		}
		
		return false;
	}
	
	public boolean makeLogout(Map <String, String> variables, String session) throws ClassNotFoundException, SQLException {
		String methodName = "makeLogout";
		log.entering(name, methodName);
		Map<String, String> getUser = new HashMap<String, String>();
		
		String sql1 = "SELECT * FROM Usuario WHERE (email LIKE ?);";
		PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
		
		try {
			statement1.setString(1, session.toLowerCase());
			
			getUser = getFromDB.getFromDB(statement1);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement1.close();
			DB.disconnect();
		}
		
		Map<String, String> getLoginSession = new HashMap<String, String>();
		String sql2 = "SELECT * FROM Login_Sessao WHERE (id_usuario LIKE ?);";
		PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
		
		try {
			statement2.setString(1, getUser.get("id_usuario"));
			
			getLoginSession = getFromDB.getFromDB(statement2);
		} catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement2.close();
			DB.disconnect();
		}
		
		log.exiting(name, methodName);
		return userFromDb.logoutUser(variables, getLoginSession.get("id_sessao"));
	}
}