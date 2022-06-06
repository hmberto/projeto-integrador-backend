package br.com.pucsp.projetointegrador.pharmacy.pharmacies;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.pharmacy.db.DB;
import br.com.pucsp.projetointegrador.pharmacy.db.GetFromDB;
import br.com.pucsp.projetointegrador.pharmacy.utils.ReplaceImageNames;

public class PharmacyDB {
	public static String NAME = PharmacyDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(PharmacyDB.class.getName());
	
	public StringBuffer getPharmacy(Map <String, String> variables, String pharmacyId) {
		LOG.entering(NAME, "getPharmacy");
		
		GetFromDB getFromDB = new GetFromDB();
		
		StringBuffer payload = new StringBuffer();
		
		try {
			String sqlPharmacy = "SELECT * FROM Farmacia WHERE (id_farmacia LIKE ?);";
			PreparedStatement statementPharmacy = DB.connect(variables).prepareStatement(sqlPharmacy);
			statementPharmacy.setString(1, pharmacyId);
			
			Map<String, String> getPharmacy = getFromDB.getFromDB(variables, statementPharmacy);
			statementPharmacy.close();
			
			List<String> pharmacies = new ArrayList<String>();
			pharmacies.add(getPharmacy.get("id_farmacia") + "-" + getPharmacy.get("nome"));
			
			String sqlProductPharmacy = "SELECT id_produto,valor_unitario FROM Produto_Farmacia WHERE (id_farmacia LIKE ?);";
			PreparedStatement statementProductPharmacy = DB.connect(variables).prepareStatement(sqlProductPharmacy);
			statementProductPharmacy.setString(1, pharmacyId);
			
			ResultSet productPharmacy = statementProductPharmacy.executeQuery();
			
			List<String> productsId = new ArrayList<String>();
			List<String> productsListA = new ArrayList<String>();
			
			while(productPharmacy.next()) {
				String priceProduct = String.format("%.2f", productPharmacy.getFloat(2)).replace(".", ",");
				
				productsId.add(productPharmacy.getString(1));
				productsListA.add("{ id: '" + productPharmacy.getString(1) + "', amount: '" + "AMOUNT_" + "', name: '" + "NAME_" + "', pharmacy: '" + getPharmacy.get("nome") + "', price: 'R$ " + priceProduct + "', image: '" + "IMAGE_" + "', description: '" + "DESCRIPTION_" + "' }");
			}
			
			StringBuffer stringfyProducts = new StringBuffer();
			
			stringfyProducts.append(productsId.toString().replace("[", "").replace("]", ""));
			stringfyProducts.toString();
			
			String sqlProducts = "SELECT * FROM Produto WHERE id_produto in (" + stringfyProducts + ");";
			
			PreparedStatement statementProducts = DB.connect(variables).prepareStatement(sqlProducts);
			ResultSet pProducts = statementProducts.executeQuery();
			
			List<String> productsList = new ArrayList<String>();
			
			while(pProducts.next()) {
				for(int i = 0; i < productsListA.size(); i++) {
					JSONObject item = new JSONObject(productsListA.get(i));
					
					if(item.get("id").equals(pProducts.getString(1))) {
						String tmp0 = productsListA.get(i).replace("AMOUNT_", pProducts.getString(3));
						String tmp1 = tmp0.replace("NAME_", pProducts.getString(2));
						String tmp2 = tmp1.replace("IMAGE_", pProducts.getString(4));
						String tmp3 = tmp2.replace("DESCRIPTION_", pProducts.getString(7));
						
						productsList.add(tmp3);
					}
				}
			}
			
			payload = payload(pharmacies, productsList);
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "PharmacyDB.getPharmacy: " + e);
		}
		
		LOG.exiting(NAME, "getPharmacy");
		
		return payload;
	}
	
	public StringBuffer payload(List<String> pharmacies, List<String> productsList) {
		StringBuffer payload = new StringBuffer();
		JSONWriter createPayload = new JSONWriter(payload);
		
		createPayload.object();
		
		for(int a = 0; a < pharmacies.size(); a ++) {
			String[] pharmacyNameId = pharmacies.get(a).split("-");
			String pharmacyId = pharmacyNameId[0];
			String pharmacyName = pharmacyNameId[1];
			
			createPayload.key("pharmacyName").value(pharmacyName);
			createPayload.key("pharmacyImage").value(ReplaceImageNames.replaceNames(pharmacyName.toLowerCase()) + ".png");
			
			createPayload.key("products");
			
			createPayload.object();
			
			for(int b = 0; b < productsList.size(); b ++) {
				JSONObject item = new JSONObject(productsList.get(b));
				if(pharmacyName.equals(item.get("pharmacy"))) {
					createPayload.key("product-" + (b+1));
					
					createPayload.object();
					
					createPayload.key("id").value(item.get("id"));
					createPayload.key("amount").value(item.get("amount"));
					createPayload.key("name").value(item.get("name"));
					createPayload.key("pharmacy").value(pharmacyName);
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