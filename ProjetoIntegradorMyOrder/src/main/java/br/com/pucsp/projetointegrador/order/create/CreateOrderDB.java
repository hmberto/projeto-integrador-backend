package br.com.pucsp.projetointegrador.order.create;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.order.db.DB;

public class CreateOrderDB {
	public static String NAME = CreateOrderDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(CreateOrderDB.class.getName());
	
	public StringBuffer createOrder(Map <String, String> variables, NewOrder order) {
		LOG.entering(NAME, "createOrder");
		
		StringBuffer payload = new StringBuffer();
		JSONWriter createPayload = new JSONWriter(payload);
		
		try {
			LOG.log(Level.INFO, "---------------------------------");
			LOG.log(Level.INFO, "Teste 1: " + order.getProducts()[0]);
			LOG.log(Level.INFO, "---------------------------------");
			LOG.log(Level.INFO, "Teste 2: " + order.getProducts()[1]);
			LOG.log(Level.INFO, "---------------------------------");
			LOG.log(Level.INFO, "Teste 3: " + order.getCardDoc());
			LOG.log(Level.INFO, "---------------------------------");
			LOG.log(Level.INFO, "Teste 4: " + order.getDeliveryFee());
			LOG.log(Level.INFO, "---------------------------------");
			LOG.log(Level.INFO, "Teste 5: " + order.getPharmacyDistance());
			LOG.log(Level.INFO, "---------------------------------");
			LOG.log(Level.INFO, "Teste 6: " + order.getDeliveryTime());
			LOG.log(Level.INFO, "---------------------------------");
			
			String alphaNumeric = "123456789";
			String token = "";
			
			for(int i = 0; i < 6; i++) {
				int myindex = (int)(alphaNumeric.length() * Math.random());
				
				token = token + alphaNumeric.charAt(myindex);
			}
			
			createPayload.object();
			
			createPayload.key("orderId").value(token);
			
			createPayload.endObject();
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "Order not created at the database: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		// LOG.exiting(NAME, "GetCar");
		LOG.log(Level.INFO, "Order ID: " + payload);
		LOG.exiting(NAME, "createOrder");
		return payload;
	}
}