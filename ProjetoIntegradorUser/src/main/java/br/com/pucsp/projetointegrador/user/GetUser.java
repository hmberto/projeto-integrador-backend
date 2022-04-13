package br.com.pucsp.projetointegrador.user;

import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.user.getuser.GetUserDB;

public class GetUser {
	public static String NAME = GetUser.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(GetUser.class.getName());
	
	public Map<String, String> user(Map <String, String> variables, String sessionId) {
		LOG.entering(NAME, "user");
		
		GetUserDB getUserDB = new GetUserDB();
		Map<String, String> user = getUserDB.getUserDB(variables, sessionId);
		
		LOG.exiting(NAME, "user");
		return user;
	}
}