package br.com.pucsp.projetointegrador.user.getuser;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.db.GetFromDB;

public class GetUserDB {
	public static String NAME = GetUserDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(GetUserDB.class.getName());
	
	public Map<String, String> getUserDB(Map <String, String> variables, String sessionId) {
		LOG.entering(NAME, "getUserDB");
		
		GetFromDB getFromDB = new GetFromDB();
		
		Map<String, String> user = new HashMap<String, String>();
		
		try {
			String sql1 = "SELECT * FROM Login_Sessao WHERE (id_session LIKE ?);";
			PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
			statement1.setString(1, sessionId);
			
			Map<String, String> getLoginSession = getFromDB.getFromDB(variables, statement1);
			statement1.close();
			
			String sql2 = "SELECT * FROM Usuario WHERE (id_usuario LIKE ?);";
			PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
			statement2.setString(1, getLoginSession.get("id_usuario"));
			
			Map<String, String> getUser = getFromDB.getFromDB(variables, statement2);
			statement2.close();
			
			String sql3 = "SELECT * FROM Endereco WHERE (id_endereco LIKE ?);";
			PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
			statement3.setString(1, getUser.get("id_endereco"));
			
			Map<String, String> getAddress = getFromDB.getFromDB(variables, statement3);
			statement3.close();
			
			String sql4 = "SELECT * FROM Genero WHERE (id_genero LIKE ?);";
			PreparedStatement statement4 = DB.connect(variables).prepareStatement(sql4);
			statement4.setString(1, getUser.get("id_genero"));
			
			Map<String, String> getSex = getFromDB.getFromDB(variables, statement4);
			statement4.close();
			
			String cpf = getUser.get("cpf");
			
			int index = 0;
			List<String> cpfSerparada = new ArrayList<String>();
			
			while (index < cpf.length()) {
				cpfSerparada.add(cpf.substring(index, Math.min(index+3,cpf.length())));
				index+= 3;
			}
			
			cpf = "***." + cpfSerparada.get(1) + "." + cpfSerparada.get(2) + "-**";
			
			user.put("name", getUser.get("nome"));
			user.put("number", getAddress.get("numero"));
			user.put("complement", getAddress.get("complemento"));
			user.put("zipCode", getAddress.get("cep"));
			user.put("sex", getSex.get("sigla"));
			user.put("cpf", cpf);
			user.put("email", getUser.get("email"));
			user.put("birthDate", getUser.get("birth_date"));
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "Data not geted from the database: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		LOG.exiting(NAME, "getUserDB");
		return user;
	}
}