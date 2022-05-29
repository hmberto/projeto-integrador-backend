package br.com.pucsp.projetointegrador.order;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.order.create.CreateOrderDB;
import br.com.pucsp.projetointegrador.order.create.NewOrder;

public class CreateOrder {
	public static String NAME = Products.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(Products.class.getName());
	
	public JSONObject createOrder(Map <String, String> variables, NewOrder order) {
		LOG.entering(NAME, "createOrder");
		
		CreateOrderDB createOrderDB = new CreateOrderDB();
		
		JSONObject payload = new JSONObject(createOrderDB.createOrder(variables, order).toString());
		
		LOG.exiting(NAME, "createOrder");
		return payload;
	}
}