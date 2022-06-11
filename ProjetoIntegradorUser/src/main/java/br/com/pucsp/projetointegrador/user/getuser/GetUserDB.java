package br.com.pucsp.projetointegrador.user.getuser;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.db.GetFromDB;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class GetUserDB {
	private static String name = GetUserDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(GetUserDB.class.getName());
	
	public Map<String, String> getUserDB(Map <String, String> variables, String sessionId) throws SQLException, ClassNotFoundException {
		log.entering(name, "getUserDB");
		
		GetFromDB getFromDB = new GetFromDB();
		
		Map<String, String> user = new HashMap<String, String>();
		
		String sql1 = "SELECT * FROM Login_Sessao WHERE (id_sessao LIKE ?);";
		PreparedStatement statement1 = DB.connect(variables).prepareStatement(sql1);
		Map<String, String> getLoginSession = new HashMap<String, String>();
		
		try {
			statement1.setString(1, sessionId);
			getLoginSession = getFromDB.getFromDB(statement1);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement1.close();
			DB.disconnect();
		}
		
		String sql2 = "SELECT * FROM Usuario WHERE (id_usuario LIKE ?);";
		PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
		Map<String, String> getUser = new HashMap<String, String>();
		
		try {
			statement2.setString(1, getLoginSession.get("id_usuario"));
			getUser = getFromDB.getFromDB(statement2);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement2.close();
			DB.disconnect();
		}
		
		String sql3 = "SELECT * FROM Endereco WHERE (id_endereco LIKE ?);";
		PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
		Map<String, String> getAddress = new HashMap<String, String>();
		
		try {
			statement3.setString(1, getUser.get("id_endereco"));
			getAddress = getFromDB.getFromDB(statement3);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement3.close();
			DB.disconnect();
		}
		
		String sql4 = "SELECT * FROM Genero WHERE (id_genero LIKE ?);";
		PreparedStatement statement4 = DB.connect(variables).prepareStatement(sql4);
		
		try {
			statement4.setString(1, getUser.get("id_genero"));
			Map<String, String> getSex = getFromDB.getFromDB(statement4);
			
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
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement4.close();
			DB.disconnect();
		}
		
		log.exiting(name, "getUserDB");
		return user;
	}
}