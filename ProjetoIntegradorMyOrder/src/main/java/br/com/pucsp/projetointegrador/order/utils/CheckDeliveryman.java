package br.com.pucsp.projetointegrador.order.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.pucsp.projetointegrador.order.db.DB;

public class CheckDeliveryman {
	public boolean checkDeliveryman(Map<String, String> variables, String deliverymanID, String orderID, String statusID) throws SQLException {
		boolean check = true;

		String sqlCheckOrders = "SELECT id_entrega from Compra WHERE (id_status LIKE ?)";
		PreparedStatement statementOrders = DB.connect(variables).prepareStatement(sqlCheckOrders);

		List<String> orderList = new ArrayList<String>();

		try {
			statementOrders.setString(1, statusID);

			ResultSet g = statementOrders.executeQuery();
			while (g.next()) {
				orderList.add(g.getString(1));
			}
		} catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		} finally {
			statementOrders.close();
			DB.disconnect();
		}

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
		
		for(int i = 0; i < deliveryList.size(); i++) {
			if(orderList.contains(deliveryList.get(i))) {
				check = false;
			}
		}

		return check;
	}
}