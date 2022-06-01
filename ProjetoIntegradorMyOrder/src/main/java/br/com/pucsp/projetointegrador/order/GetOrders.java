package br.com.pucsp.projetointegrador.order;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.order.get.GetOrdersDB;

public class GetOrders {
	public static String NAME = Products.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(Products.class.getName());
	
	public JSONObject getOrder(Map <String, String> variables, String session) {
		LOG.entering(NAME, "createOrder");
		
		GetOrdersDB getOrderDB = new GetOrdersDB();
		
		JSONObject payload = new JSONObject(getOrderDB.getOrders(variables, session).toString());
		
		LOG.exiting(NAME, "createOrder");
		return payload;
	}
}