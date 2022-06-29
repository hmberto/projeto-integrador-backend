package br.com.pucsp.projetointegrador.order;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.order.db.DB;
import br.com.pucsp.projetointegrador.order.db.GetFromDB;
import br.com.pucsp.projetointegrador.order.utils.CheckDeliveryman;
import br.com.pucsp.projetointegrador.order.utils.LogMessage;

public class DeliveryOrder {
	private static String name = DeliveryOrder.class.getSimpleName();
	private static Logger log = Logger.getLogger(DeliveryOrder.class.getName());
	
	public boolean deliveryOrder(Map<String, String> variables, String deliverymanID, String orderID, String statusID) throws SQLException {
		log.entering(name, "deliveryOrder");
		
		boolean check = true;
		
		CheckDeliveryman checkDeliveryman = new CheckDeliveryman();
		check = checkDeliveryman.checkDeliveryman(variables, deliverymanID, orderID, statusID);
		
		GetFromDB getFromDB = new GetFromDB();
		
		String sqlGetOrder = "SELECT * FROM Compra WHERE (id_compra LIKE ?)";
		PreparedStatement statementOrder = DB.connect(variables).prepareStatement(sqlGetOrder);
		
		Map<String, String> order = new HashMap<String, String>();
		try {
			statementOrder.setString(1, orderID);
			
			order = getFromDB.getFromDB(statementOrder);
		}
		catch (SQLException e) {
			check = false;
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementOrder.close();
			DB.disconnect();
		}
		
		String sqlGetDelivey = "SELECT * FROM Entrega WHERE (id_entrega LIKE ?)";
		PreparedStatement statementDelivery = DB.connect(variables).prepareStatement(sqlGetDelivey);
		
		Map<String, String> delivery = new HashMap<String, String>();
		try {
			statementDelivery.setString(1, order.get("id_entrega"));
			
			order = getFromDB.getFromDB(statementDelivery);
		}
		catch (SQLException e) {
			check = false;
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementDelivery.close();
			DB.disconnect();
		}
		
		String updateDeliveryman = "UPDATE Entrega SET id_entregador = ? WHERE (id_entrega LIKE ?);";
		PreparedStatement updateDeliveyman = DB.connect(variables).prepareStatement(updateDeliveryman);
		
		try {
			if(check && delivery.get("id_entregador") == null || delivery.get("id_entregador").equals("null")) {
				updateDeliveyman.setString(1, deliverymanID);
				updateDeliveyman.setString(2, delivery.get("id_entrega"));
				
				updateDeliveyman.execute();
			}
			else {
				check = false;
			}
		}
		catch (SQLException e) {
			check = false;
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			updateDeliveyman.close();
			DB.disconnect();
		}
		
		
		String updateStatus = "UPDATE Compra SET id_status = ? WHERE (id_compra LIKE ?);";
		PreparedStatement updateOrderStatus = DB.connect(variables).prepareStatement(updateStatus);
		
		try {
			if(check && delivery.get("id_entregador") == null || delivery.get("id_entregador").equals("null")) {
				updateOrderStatus.setString(1, statusID);
				updateOrderStatus.setString(2, orderID);
				
				updateOrderStatus.execute();
			}
			else {
				check = false;
			}
		}
		catch (SQLException e) {
			check = false;
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			updateOrderStatus.close();
			DB.disconnect();
		}
		
		log.exiting(name, "deliveryOrder");
		return check;
	}
}