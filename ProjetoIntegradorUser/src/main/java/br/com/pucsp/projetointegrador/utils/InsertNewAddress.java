package br.com.pucsp.projetointegrador.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;

public class InsertNewAddress {
	private static String name = InsertNewAddress.class.getSimpleName();
	private static Logger log = Logger.getLogger(InsertNewAddress.class.getName());
	
	private InsertNewAddress () {}
	
	public static Map<String, String> updateAddress(Map<String, String> variables, String street, String city, String district, String state) throws SQLException, ClassNotFoundException {
		log.entering(name, "updateAddress");
		
		Map<String, String> states = AddressData.states();
		List<String> streetType = AddressData.streetType();
		
		Map<String, String> data = new HashMap<String, String>();
		
		String userStreetType = "";
		for(String type : streetType) {
		     if(street.toLowerCase().contains(type.toLowerCase())) {
		    	 userStreetType = type;
		     }
		}
		
		String idSiglaEstado = ReturnIdUfState.ufState(variables, city, district, state, userStreetType);
		
		String insertIfNotExistsState2 = "INSERT IGNORE INTO Estado (nome, id_sigla_estado) VALUES (?, ?);";
		PreparedStatement statement6 = DB.connect(variables).prepareStatement(insertIfNotExistsState2);
		
		try {
			statement6.setString(1, states.get(state));
			statement6.setString(2, idSiglaEstado);
			
			statement6.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement6.close();
			DB.disconnect();
		}
		
		String getFkCity = "SELECT id_cidade FROM Cidade WHERE (nome LIKE ?);";
		PreparedStatement statementFK1 = DB.connect(variables).prepareStatement(getFkCity);
		
		try {
			statementFK1.setString(1, city);
			
			ResultSet f1 = statementFK1.executeQuery();
			while(f1.next()) {
				data.put("fkCity", f1.getString(1));
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementFK1.close();
			DB.disconnect();
		}
		
		String getFkDistrict = "SELECT id_bairro FROM Bairro WHERE (nome LIKE ?);";
		PreparedStatement statementFK2 = DB.connect(variables).prepareStatement(getFkDistrict);
		
		try {
			statementFK2.setString(1, district);
			
			ResultSet f2 = statementFK2.executeQuery();
			while(f2.next()) {
				data.put("fkDistrict", f2.getString(1));
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementFK2.close();
			DB.disconnect();
		}
		
		String getFkState = "SELECT id_estado FROM Estado WHERE (nome LIKE ?);";
		PreparedStatement statementFK3 = DB.connect(variables).prepareStatement(getFkState);
		
		try {
			statementFK3.setString(1, states.get(state));
			
			ResultSet f3 = statementFK3.executeQuery();
			while(f3.next()) {
				data.put("fkState", f3.getString(1));
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementFK3.close();
			DB.disconnect();
		}
		
		String getFkAddressType = "SELECT id_tipo_endereco FROM Tipo_Endereco WHERE (nome LIKE ?);";
		PreparedStatement statementFK4 = DB.connect(variables).prepareStatement(getFkAddressType);
		
		try {
			statementFK4.setString(1, userStreetType);
			
			ResultSet f4 = statementFK4.executeQuery();
			while(f4.next()) {
				data.put("fkAddressType", f4.getString(1));
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementFK4.close();
			DB.disconnect();
		}
		
		return data;
	}
}