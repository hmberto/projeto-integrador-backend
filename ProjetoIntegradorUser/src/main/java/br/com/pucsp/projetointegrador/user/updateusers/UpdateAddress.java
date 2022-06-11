package br.com.pucsp.projetointegrador.user.updateusers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.db.DB;
import br.com.pucsp.projetointegrador.utils.AddressData;
import br.com.pucsp.projetointegrador.utils.InsertNewAddress;
import br.com.pucsp.projetointegrador.utils.LogMessage;

public class UpdateAddress {
	private static String name = UpdateAddress.class.getSimpleName();
	private static Logger log = Logger.getLogger(UpdateAddress.class.getName());
	
	private UpdateAddress () {}
	
	public static void updateAddress(Map<String, String> variables, UpdateUsers user, String lat, String lon, String addressId) throws SQLException, ClassNotFoundException {
		log.entering(name, "updateAddress");
		
		List<String> streetType = AddressData.streetType();
		
		Map<String, String> data = InsertNewAddress.updateAddress(variables, user.getStreet(), user.getCity(), user.getDistrict(), user.getState());
		
		String userStreetType = "";
		for(String type : streetType) {
		     if(user.getStreet().toLowerCase().contains(type.toLowerCase())) {
		    	 userStreetType = type;
		     }
		}
		
		String sqlAddress = "UPDATE Endereco SET nome = ?, numero = ?, complemento = ?, cep = ?, lat = ?, lon = ?, id_cidade = ?, id_bairro = ?, id_estado = ?, id_tipo_endereco = ? WHERE (id_endereco LIKE ?);";
		PreparedStatement statementAddress = DB.connect(variables).prepareStatement(sqlAddress);
		String newStreet = user.getStreet().replace(userStreetType, "");
		
		try {
			statementAddress.setString(1, newStreet.trim());
			statementAddress.setString(2, user.getNumber());
			statementAddress.setString(3, user.getComplement());
			statementAddress.setString(4, user.getZipCode());
			statementAddress.setString(5, lat);
			statementAddress.setString(6, lon);
			statementAddress.setString(7, data.get("fkCity"));
			statementAddress.setString(8, data.get("fkDistrict"));
			statementAddress.setString(9, data.get("fkState"));
			statementAddress.setString(10, data.get("fkAddressType"));
			statementAddress.setString(11, addressId);
			
			statementAddress.execute();
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementAddress.close();
			DB.disconnect();
		}
	}
}