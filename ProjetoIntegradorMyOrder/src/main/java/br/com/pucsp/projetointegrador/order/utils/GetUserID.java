package br.com.pucsp.projetointegrador.order.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.order.db.DB;
import br.com.pucsp.projetointegrador.order.db.GetFromDB;

public class GetUserID {
	private static String name = GetUserID.class.getSimpleName();
	private static Logger log = Logger.getLogger(GetUserID.class.getName());
	
	GetFromDB getFromDB = new GetFromDB();
	
	public String userId(Map<String, String> variables, String session) throws SQLException {
		log.entering(name, "userId");

		String sqlA = "SELECT * FROM Login_Sessao WHERE (id_sessao LIKE ?);";
		PreparedStatement statementA = DB.connect(variables).prepareStatement(sqlA);
		
		try {
			statementA.setString(1, session);

			Map<String, String> getLoginSession = getFromDB.getFromDB(statementA);
			
			if(getLoginSession.get("id_usuario") != null && !getLoginSession.get("id_usuario").equals("null")) {
				log.exiting(name, "userId");
				return getLoginSession.get("id_usuario");
			}			
		}
		catch (Exception e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementA.close();
		}
		
		return null;
	}
}