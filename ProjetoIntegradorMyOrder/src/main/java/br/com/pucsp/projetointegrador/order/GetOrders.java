package br.com.pucsp.projetointegrador.order;

import java.sql.PreparedStatement;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.order.db.DB;
import br.com.pucsp.projetointegrador.order.get.GetOrdersDB;
import br.com.pucsp.projetointegrador.order.utils.GetUserID;

public class GetOrders {
	public static String NAME = Products.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(Products.class.getName());
	
	public JSONObject getOrders(Map <String, String> variables, String session) {
		LOG.entering(NAME, "getOrders");
		GetUserID getUserID = new GetUserID();
		String userId = getUserID.userId(variables, session);
		
		if (userId == null || userId == "null") {
			return null;
		}
		
		try {
			String sqlOrders = "SELECT id_compra,data_compra,distancia_farmacia,tempo_entrega,taxa_entrega,local_entrega,id_farmacia,id_entrega,id_forma_pagamento,id_status FROM Compra WHERE (id_usuario LIKE ?);";
			PreparedStatement statementOrders = DB.connect(variables).prepareStatement(sqlOrders);
			statementOrders.setString(1, userId);
			
			GetOrdersDB getOrderDB = new GetOrdersDB();
			
			JSONObject payload = new JSONObject(getOrderDB.getOrders(variables, session, statementOrders).toString());
			
			LOG.exiting(NAME, "getOrders");
			return payload;
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "Orders not getted from the database: " + e);
			return null;
		}
	}
}