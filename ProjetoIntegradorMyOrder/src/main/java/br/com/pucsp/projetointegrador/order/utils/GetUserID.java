package br.com.pucsp.projetointegrador.order.utils;

import java.sql.PreparedStatement;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.order.db.DB;
import br.com.pucsp.projetointegrador.order.db.GetFromDB;

public class GetUserID {
	public static String NAME = GetUserID.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(GetUserID.class.getName());
	
	GetFromDB getFromDB = new GetFromDB();
	
	public String userId(Map<String, String> variables, String session) {
		LOG.entering(NAME, "userId");
		try {
			String sqlA = "SELECT * FROM Login_Sessao WHERE (id_sessao LIKE ?);";
			PreparedStatement statementA = DB.connect(variables).prepareStatement(sqlA);
			statementA.setString(1, session);

			Map<String, String> getLoginSession = getFromDB.getFromDB(variables, statementA);
			statementA.close();
			
			if(getLoginSession.get("id_usuario") != null && getLoginSession.get("id_usuario") != "null") {
				LOG.log(Level.INFO, "User ID: " + getLoginSession.get("id_usuario"));
				LOG.exiting(NAME, "userId");
				return getLoginSession.get("id_usuario");
			}			
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "User not getted from the database: " + e);
		}
		
		LOG.exiting(NAME, "userId");
		return null;
	}
}