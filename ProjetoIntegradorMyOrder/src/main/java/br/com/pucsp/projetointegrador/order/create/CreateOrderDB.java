package br.com.pucsp.projetointegrador.order.create;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.order.db.DB;
import br.com.pucsp.projetointegrador.order.db.GetFromDB;

public class CreateOrderDB {
	public static String NAME = CreateOrderDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(CreateOrderDB.class.getName());
	
	public StringBuffer createOrder(Map <String, String> variables, NewOrder order) {
		LOG.entering(NAME, "createOrder");
		
		GetFromDB getFromDB = new GetFromDB();
		
		StringBuffer payload = new StringBuffer();
		JSONWriter createPayload = new JSONWriter(payload);
		
		try {
			LOG.log(Level.INFO, "---------------------------------");
			LOG.log(Level.INFO, "Teste 1: " + order.getProducts());
			LOG.log(Level.INFO, "---------------------------------");
			LOG.log(Level.INFO, "Teste 2: " + order.getSession());
			LOG.log(Level.INFO, "---------------------------------");
			LOG.log(Level.INFO, "Teste 3: " + order.getDistance());
			LOG.log(Level.INFO, "---------------------------------");
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "Data not geted from the database: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		// LOG.exiting(NAME, "GetCar");
		LOG.log(Level.INFO, "Nearby products payload: " + payload);
		LOG.exiting(NAME, "createOrder");
		return payload;
	}
}