package br.com.pucsp.projetointegrador.user;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.user.getuser.GetUserDB;

public class GetUser {
	private static String name = GetUser.class.getSimpleName();
	private static Logger log = Logger.getLogger(GetUser.class.getName());
	
	public Map<String, String> user(Map <String, String> variables, String sessionId) throws SQLException, ClassNotFoundException {
		log.entering(name, "user");
		
		GetUserDB getUserDB = new GetUserDB();
		Map<String, String> user = getUserDB.getUserDB(variables, sessionId);
		
		log.exiting(name, "user");
		return user;
	}
}