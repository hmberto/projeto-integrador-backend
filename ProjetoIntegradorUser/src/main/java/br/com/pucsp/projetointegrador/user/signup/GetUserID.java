package br.com.pucsp.projetointegrador.user.signup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;

public class GetUserID {
	public static String NAME = GetUserID.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(GetUserID.class.getName());
	
	public static String getUserID(Map<String, String> variables, String email, String cpf) {
		LOG.entering(NAME, "getUserID");
		
		try {
			String getIdUser = "SELECT id_usuario FROM Usuario WHERE (email LIKE ?) AND (cpf LIKE ?);";
			
			PreparedStatement getIdUserStat = DB.connect(variables).prepareStatement(getIdUser);
			getIdUserStat.setString(1, email);
			getIdUserStat.setString(2, cpf);
			
			String idUserGetted = "";
			
			ResultSet g = getIdUserStat.executeQuery();
			while(g.next()) {
				idUserGetted = g.getString(1);
			}
			
			getIdUserStat.close();
			
			LOG.log(Level.INFO, "User ID getted from User table! User ID: " + idUserGetted);
			LOG.exiting(NAME, "getUserID");
			return idUserGetted;
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "GetUserID.getUserID: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "getUserID");
		return "";
	}
}