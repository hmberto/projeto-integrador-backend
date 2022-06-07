package br.com.pucsp.projetointegrador.order;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.order.create.CreateOrderDB;
import br.com.pucsp.projetointegrador.order.create.NewOrder;

public class CreateOrder {
	private static String name = CreateOrder.class.getSimpleName();
	private static Logger log = Logger.getLogger(CreateOrder.class.getName());
	
	public JSONObject createOrder(Map <String, String> variables, NewOrder order) throws SQLException {
		log.entering(name, "createOrder");
		
		CreateOrderDB createOrderDB = new CreateOrderDB();
		
		JSONObject payload = new JSONObject(createOrderDB.createOrder(variables, order).toString());
		
		log.exiting(name, "createOrder");
		return payload;
	}
}