package br.com.pucsp.projetointegrador.order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.order.db.DB;

public class DeliveryOrders {
	public JSONObject deliveryOrders(Map<String, String> variables, String deliverymanID) {
		StringBuilder payload = new StringBuilder();
		JSONWriter createResource = new JSONWriter(payload);
		
		String sql = "SELECT * FROM Compra WHERE (id_status LIKE ?)";
		
		createResource.object();
		
		try {
			PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
			statement.setInt(1, 4);
			
			ResultSet orders = statement.executeQuery();
			
			int i = 1;
			while(orders.next()) {
				createResource.key("delivery-" + i);
				createResource.object();
				
				createResource.key("pharmacyAddress").value("Avenida Teste");
				createResource.key("userAddress").value(orders.getString(6));
				createResource.key("distance").value(orders.getString(3));
				createResource.key("orderId").value(orders.getString(1));
				
				createResource.endObject();
				
				i++;
			}
		}
		catch (Exception e) {
		}
		
		createResource.endObject();
		
		JSONObject jsonPayload = new JSONObject(payload.toString());
		
		return jsonPayload;
	}
}