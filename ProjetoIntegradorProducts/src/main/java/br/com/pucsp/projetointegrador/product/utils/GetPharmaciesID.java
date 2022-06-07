package br.com.pucsp.projetointegrador.product.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.product.db.DB;

public class GetPharmaciesID {
	private static String name = GetPharmaciesID.class.getSimpleName();
	private static Logger log = Logger.getLogger(GetPharmaciesID.class.getName());
	
	public List<String> pharmaciesID(Map <String, String> variables, String sql) throws SQLException {
		log.entering(name, "products");
		
		PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
		
		List<String> pharmaciesId = new ArrayList<String>();
		
		try {
			ResultSet f = statement.executeQuery();
			
			while(f.next()) {
				pharmaciesId.add(f.getString(1));
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement.close();
			DB.disconnect();
		}
		
		return pharmaciesId;
	}
}