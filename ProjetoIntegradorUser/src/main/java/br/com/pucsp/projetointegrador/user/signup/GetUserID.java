package br.com.pucsp.projetointegrador.user.signup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class GetUserID {
	private static String name = GetUserID.class.getSimpleName();
	private static Logger log = Logger.getLogger(GetUserID.class.getName());
	
	private GetUserID () {}
	
	public static String getUserID(Map<String, String> variables, String email) throws SQLException, ClassNotFoundException {
		log.entering(name, "getUserID");
		
		String getIdUser = "SELECT id_usuario FROM Usuario WHERE (email LIKE ?);";
		PreparedStatement getIdUserStat = DB.connect(variables).prepareStatement(getIdUser);
		String idUserGetted = "";
		
		try {
			getIdUserStat.setString(1, email);
			
			ResultSet g = getIdUserStat.executeQuery();
			while(g.next()) {
				idUserGetted = g.getString(1);
			}
			
			log.exiting(name, "getUserID");
			return idUserGetted;
		}
		catch (Exception e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			getIdUserStat.close();
			DB.disconnect();
		}
	}
}