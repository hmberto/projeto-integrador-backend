package br.com.pucsp.projetointegrador.order.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.pucsp.projetointegrador.order.db.DB;

public class GetMyDelivery {
	public String checkDeliveryman(Map<String, String> variables, String deliverymanID) throws SQLException {
		String sqlCheckDeliveryman = "SELECT id_entrega from Entrega WHERE (id_entregador LIKE ?)";
		PreparedStatement statementCheckDeliveryman = DB.connect(variables).prepareStatement(sqlCheckDeliveryman);

		List<String> deliveryList = new ArrayList<String>();
		
		try {
			statementCheckDeliveryman.setString(1, deliverymanID);

			ResultSet g = statementCheckDeliveryman.executeQuery();
			while (g.next()) {
				deliveryList.add(g.getString(1));
			}
		} catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		} finally {
			statementCheckDeliveryman.close();
			DB.disconnect();
		}
		
		StringBuilder stringfy = new StringBuilder();
		
		stringfy.append(deliveryList.toString().replace("[", "").replace("]", ""));
		
		return stringfy.toString();
	}
}