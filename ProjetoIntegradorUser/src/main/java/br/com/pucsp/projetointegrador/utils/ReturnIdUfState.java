package br.com.pucsp.projetointegrador.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import br.com.pucsp.projetointegrador.db.DB;

public class ReturnIdUfState {
	private ReturnIdUfState () {}
	
	public static String ufState(Map<String, String> variables, String city, String district, String state, String userStreetType) throws ClassNotFoundException, SQLException {
		String insertIfNotExistsCity = SQLStrings.insertIfNotExistsCity(city);
		PreparedStatement statement1 = DB.connect(variables).prepareStatement(insertIfNotExistsCity);
		
		try {
			statement1.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement1.close();
			DB.disconnect();
		}
		
		String insertIfNotExistsDistrict = SQLStrings.insertIfNotExistsDistrict(district);
		PreparedStatement statement2 = DB.connect(variables).prepareStatement(insertIfNotExistsDistrict);
		
		try {
			statement2.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement2.close();
			DB.disconnect();
		}
		
		String insertIfNotExistsUf = SQLStrings.insertIfNotExistsUf(state);
		PreparedStatement statement3 = DB.connect(variables).prepareStatement(insertIfNotExistsUf);
		
		try {
			statement3.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement3.close();
			DB.disconnect();
		}
		
		String insertIfNotExistsAddressType = SQLStrings.insertIfNotExistsAddressType(userStreetType);
		PreparedStatement statement4 = DB.connect(variables).prepareStatement(insertIfNotExistsAddressType);
		
		try {
			statement4.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement4.close();
			DB.disconnect();
		}
		
		String insertIfNotExistsState1 = "SELECT id_sigla_estado FROM Sigla_Estado WHERE (nome LIKE ?);";
		PreparedStatement statement5 = DB.connect(variables).prepareStatement(insertIfNotExistsState1);
		String idSiglaEstado = "";
		
		try {
			statement5.setString(1, state);
			
			ResultSet f = statement5.executeQuery();
			
			while(f.next()) {
				idSiglaEstado = f.getString(1);
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement5.close();
			DB.disconnect();
		}
		
		return idSiglaEstado;
	}
}