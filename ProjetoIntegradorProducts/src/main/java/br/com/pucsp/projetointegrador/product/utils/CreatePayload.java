package br.com.pucsp.projetointegrador.product.utils;

import java.util.List;

import org.json.JSONObject;
import org.json.JSONWriter;

public class CreatePayload {
	public StringBuffer payload(List<String> pharmacies, List<String> newProductsList) {
		StringBuffer payload = new StringBuffer();
		JSONWriter createPayload = new JSONWriter(payload);
		
		createPayload.object();
		
		for(int a = 0; a < pharmacies.size(); a ++) {
			String[] pharmacyNameId = pharmacies.get(a).split("-");
			String pharmacyId = pharmacyNameId[0];
			String pharmacyName = pharmacyNameId[1];
			
			createPayload.key(pharmacyName);
			
			createPayload.object();
			
			for(int b = 0; b < newProductsList.size(); b ++) {
				JSONObject item = new JSONObject(newProductsList.get(b));
				if(pharmacyName.equals(item.get("pharmacy"))) {
					createPayload.key("product-" + (b+1));
					
					createPayload.object();
					
					createPayload.key("id").value(item.get("id"));
					createPayload.key("amount").value(item.get("amount"));
					createPayload.key("name").value(item.get("name"));
					createPayload.key("pharmacy").value(item.get("pharmacy"));
					createPayload.key("pharmacyId").value(pharmacyId);
					createPayload.key("price").value(item.get("price"));
					createPayload.key("image").value(item.get("image"));
					createPayload.key("description").value(item.get("description"));
					
					createPayload.endObject();
				}
			}
			
			createPayload.endObject();
		}
		
		createPayload.endObject();
		
		return payload;
	}
}