package br.com.pucsp.projetointegrador.order;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.order.db.DB;
import br.com.pucsp.projetointegrador.order.get.GetOrdersDB;
import br.com.pucsp.projetointegrador.order.utils.GetUserID;
import br.com.pucsp.projetointegrador.order.utils.LogMessage;

public class GetOrder {
	private static String name = GetOrder.class.getSimpleName();
	private static Logger log = Logger.getLogger(GetOrder.class.getName());
	
	public JSONObject getOrder(Map <String, String> variables, String orderId, String session) throws SQLException {
		log.entering(name, "getOrder");
		
		GetUserID getUserID = new GetUserID();
		String userId = getUserID.userId(variables, session);
		
		String sqlOrders = "SELECT id_compra,data_compra,distancia_farmacia,tempo_entrega,taxa_entrega,local_entrega,id_farmacia,id_entrega,id_forma_pagamento,id_status FROM Compra WHERE (id_usuario LIKE ?) AND (id_compra LIKE ?);";
		PreparedStatement statementOrders = DB.connect(variables).prepareStatement(sqlOrders);
		
		try {
			statementOrders.setString(1, userId);
			statementOrders.setString(2, orderId);
			
			GetOrdersDB getOrderDB = new GetOrdersDB();
			
			JSONObject payload = new JSONObject(getOrderDB.getOrders(variables, statementOrders).toString());
			
			log.exiting(name, "getOrder");
			return payload;
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statementOrders.close();
		}
	}
}