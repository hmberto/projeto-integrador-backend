package br.com.pucsp.projetointegrador.user.signup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.utils.AddressData;
import br.com.pucsp.projetointegrador.utils.InsertNewAddress;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class InsertAddress {
	private static String name = InsertAddress.class.getSimpleName();
	private static Logger log = Logger.getLogger(InsertAddress.class.getName());
	
	private InsertAddress () {}
	
	public static String insertAddress(Map<String, String> variables, CreateUsers user, String lat, String lon) throws SQLException, ClassNotFoundException {
		log.entering(name, "insertAddress");
		
		String userStreetType = "";
		List<String> streetType = AddressData.streetType();
		Map<String, String> data = InsertNewAddress.updateAddress(variables, user.getStreet(), user.getCity(), user.getDistrict(), user.getState());
		
		for(String type : streetType) {
		     if(user.getStreet().toLowerCase().contains(type.toLowerCase())) {
		    	 userStreetType = type;
		     }
		}
		
		String sqlInsertAddress = "INSERT INTO Endereco (nome, numero, complemento, cep, lat, lon, id_cidade, id_bairro, id_estado, id_tipo_endereco) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement statementInsertAddress = DB.connect(variables).prepareStatement(sqlInsertAddress);
		String newStreet = user.getStreet().replace(userStreetType, "");
		
		try {
			statementInsertAddress.setString(1, newStreet.trim());
			statementInsertAddress.setString(2, user.getNumber());
			statementInsertAddress.setString(3, user.getComplement());
			statementInsertAddress.setString(4, user.getZipCode());
			statementInsertAddress.setString(5, lat);
			statementInsertAddress.setString(6, lon);
			statementInsertAddress.setString(7, data.get("fkCity"));
			statementInsertAddress.setString(8, data.get("fkDistrict"));
			statementInsertAddress.setString(9, data.get("fkState"));
			statementInsertAddress.setString(10, data.get("fkAddressType"));
			
			statementInsertAddress.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementInsertAddress.close();
			DB.disconnect();
		}
		
		String getIdAddress = "SELECT id_endereco FROM Endereco WHERE (nome LIKE ?) AND (numero LIKE ?) AND (complemento LIKE ?) AND (cep LIKE ?) AND (lat LIKE ?) AND (lon LIKE ?) AND (id_cidade LIKE ?) AND (id_bairro LIKE ?) AND (id_estado LIKE ?) AND (id_tipo_endereco LIKE ?);";
		PreparedStatement getIdAddressStat = DB.connect(variables).prepareStatement(getIdAddress);
		
		try {
			getIdAddressStat.setString(1, newStreet.trim());
			getIdAddressStat.setString(2, user.getNumber());
			getIdAddressStat.setString(3, user.getComplement());
			getIdAddressStat.setString(4, user.getZipCode());
			getIdAddressStat.setString(5, lat);
			getIdAddressStat.setString(6, lon);
			getIdAddressStat.setString(7, data.get("fkCity"));
			getIdAddressStat.setString(8, data.get("fkDistrict"));
			getIdAddressStat.setString(9, data.get("fkState"));
			getIdAddressStat.setString(10, data.get("fkAddressType"));
			
			String idAddressGetted = "";
			
			ResultSet f10 = getIdAddressStat.executeQuery();
			while(f10.next()) {
				idAddressGetted = f10.getString(1);
			}
			
			log.exiting(name, "insertAddress");
			return idAddressGetted;
		}
		catch (Exception e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			getIdAddressStat.close();
			DB.disconnect();
		}
	}
}