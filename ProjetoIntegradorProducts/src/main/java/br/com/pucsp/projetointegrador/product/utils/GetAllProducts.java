package br.com.pucsp.projetointegrador.product.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.product.db.DB;

public class GetAllProducts {
	public static String NAME = GetAllProducts.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(GetAllProducts.class.getName());
	
	public StringBuffer products(Map <String, String> variables, String sql, String sql3p1) {
		StringBuffer payload = new StringBuffer();
		
		try {
			PreparedStatement statement = DB.connect(variables).prepareStatement(sql);
			ResultSet f = statement.executeQuery();
			
			List<String> pharmaciesId = new ArrayList<String>(); 
			
			while(f.next()) {
				pharmaciesId.add(f.getString(1));
			}
			
			StringBuffer stringfy = new StringBuffer();
			
			stringfy.append(pharmaciesId.toString().replace("[", "").replace("]", ""));
			stringfy.toString();
			
			String sql2 = "select id_produto from Produto_Farmacia where id_farmacia in (" + stringfy + ");";
			
			PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
			ResultSet f2 = statement2.executeQuery();
			
			List<String> productsId = new ArrayList<String>();
			
			List<String> newProductsList0 = new ArrayList<String>();
			
			while(f2.next()) {
				productsId.add(f2.getString(1));
				newProductsList0.add("{ id: '" + f2.getString(1) + "', amount: '" + "AMOUNT_" + "', name: '" + "NAME_" + "', pharmacy: 'PHARMACY_NAME', price: 'R$ PRICE_FLOAT', image: '" + "IMAGE_" + "', description: '" + "DESCRIPTION_" + "' }");
			}
			
			StringBuffer stringfyProducts = new StringBuffer();
			
			stringfyProducts.append(productsId.toString().replace("[", "").replace("]", ""));
			stringfyProducts.toString();
			
			String sql3 = sql3p1 + stringfyProducts + ");";
			
			PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
			ResultSet f3 = statement3.executeQuery();
			
			List<String> productsSearchIds = new ArrayList<String>();
			Map<Integer, String> productsSearch = new HashMap<Integer, String>();
			
			List<String> newProductsList1 = new ArrayList<String>();
			
			while(f3.next()) {
				int productId = Integer.parseInt(f3.getString(1));
				productsSearch.put(productId ,"{ id: '" + f3.getString(1) + "', amount: '" + f3.getString(5) + "', name: '" + f3.getString(2) + "', pharmacy: 'PHARMACY_NAME', price: 'R$ PRICE_FLOAT', image: '" + f3.getString(6) + "', description: '" + f3.getString(7) + "' }");
				productsSearchIds.add(f3.getString(1));
				
				for(int i = 0; i < newProductsList0.size(); i++) {
					JSONObject item = new JSONObject(newProductsList0.get(i));
					
					if(item.get("id").equals(f3.getString(1))) {
						String tmp0 = newProductsList0.get(i).replace("AMOUNT_", f3.getString(5));
						String tmp1 = tmp0.replace("NAME_", f3.getString(2));
						String tmp2 = tmp1.replace("IMAGE_", f3.getString(6));
						String tmp3 = tmp2.replace("DESCRIPTION_", f3.getString(7));
						
						newProductsList1.add(tmp3);
					}
				}
			}
			
			StringBuffer productsSearchId = new StringBuffer();
			
			productsSearchId.append(productsSearchIds.toString().replace("[", "").replace("]", ""));
			productsSearchId.toString();
			
			String sql4 = "select id_farmacia,id_produto,valor_unitario from Produto_Farmacia where id_produto in (" + productsSearchId + ");";
			
			PreparedStatement statement4 = DB.connect(variables).prepareStatement(sql4);
			ResultSet f4 = statement4.executeQuery();
			
			List<String> pharmaciesIds = new ArrayList<String>();
			Map<Integer, Integer> ProductsPharmacies = new HashMap<Integer, Integer>();
			
			Map<Integer, String> finalProducts = new HashMap<Integer, String>();
			
			List<String> newProductsList2 = new ArrayList<String>();
			
			while(f4.next()) {
				pharmaciesIds.add(f4.getString(1));
				ProductsPharmacies.put(f4.getInt(1), f4.getInt(2));
				String priceProduct = String.format("%.2f", f4.getFloat(3));
				String pharmacyNumberId = f4.getString(1);
	//			price.put(f4.getInt(2), priceProduct);
				priceProduct.replace(".", ",");
				
				String tmp = productsSearch.get(f4.getInt(2));
				String tmpProduct = tmp.replace("PRICE_FLOAT", priceProduct);
				finalProducts.put(f4.getInt(2), tmpProduct);
				
				for(int i = 0; i < newProductsList1.size(); i++) {
					JSONObject item = new JSONObject(newProductsList1.get(i));
					
					if(item.get("id").equals(f4.getString(2))) {
						String tmp0 = newProductsList1.get(i);
						String tmp1 = tmp0.replace("PRICE_FLOAT", priceProduct);
						String tmp2 = tmp1.replace("PHARMACY_NAME", "PHARMACY_" + pharmacyNumberId);
						
						if(newProductsList2.contains(tmp2)) {}
						else {
							newProductsList2.add(tmp2);
						}
					}
				}
			}
			
			StringBuffer stringfyPharmaciesIds = new StringBuffer();
			
			stringfyPharmaciesIds.append(pharmaciesIds.toString().replace("[", "").replace("]", ""));
			stringfyPharmaciesIds.toString();
						
			String sql5 = "select id_farmacia,nome from Farmacia where id_farmacia in (" + stringfyPharmaciesIds + ");";
			PreparedStatement statement5 = DB.connect(variables).prepareStatement(sql5);
			ResultSet f5 = statement5.executeQuery();
			
			List<String> products = new ArrayList<String>();
			List<String> pharmacies = new ArrayList<String>();
			
			List<String> newProductsList = new ArrayList<String>();
			
			while(f5.next()) {
				int idProduct = ProductsPharmacies.get(f5.getInt(1));
				
				String tmp = finalProducts.get(idProduct);
				String tmpProduct = tmp.replace("PHARMACY_NAME", f5.getString(2));
				
				products.add(tmpProduct);
				pharmacies.add(f5.getString(2));
				
				for(int i = 0; i < newProductsList2.size(); i++) {
					JSONObject item = new JSONObject(newProductsList2.get(i));
					
					if(item.get("pharmacy").equals("PHARMACY_" + f5.getString(1))) {
						String tmp0 = newProductsList2.get(i);
						String tmp1 = tmp0.replace("PHARMACY_" + f5.getString(1), f5.getString(2));
						
						if(newProductsList.contains(tmp1)) {}
						else {
							newProductsList.add(tmp1);
						}
					}
				}
			}
			
			CreatePayload createPayload = new CreatePayload();
			payload = createPayload.payload(pharmacies, newProductsList);
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "Data not geted from the database: " + e);
		}
				
		return payload;
	}
}