package br.com.pucsp.projetointegrador.product.products;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.product.db.DB;
import br.com.pucsp.projetointegrador.product.utils.CreatePayload;
import br.com.pucsp.projetointegrador.product.utils.GetPharmaciesID;
import br.com.pucsp.projetointegrador.product.utils.LogMessage;

public class ProductsDB {
	private static String name = ProductsDB.class.getSimpleName();
	private static Logger log = Logger.getLogger(ProductsDB.class.getName());
	
	public StringBuilder products(Map <String, String> variables, String sql, String sql3p1) throws SQLException {
		String methodName = "products";
		log.entering(name, methodName);
		
		GetPharmaciesID getPharmaciesID = new GetPharmaciesID();
		List<String> pharmaciesId = getPharmaciesID.pharmaciesID(variables, sql);
		
		StringBuilder stringfy = new StringBuilder();
		
		stringfy.append(pharmaciesId.toString().replace("[", "").replace("]", ""));
		stringfy.toString();
		
		List<String> productsId = new ArrayList<String>();
		List<String> newProductsList0 = new ArrayList<String>();
		
		String sql2 = "select id_produto from Produto_Farmacia where id_farmacia in (" + stringfy + ");";
		
		StringBuilder stringfyProducts = getProductsID(variables, productsId, newProductsList0, sql2);
		
		stringfyProducts.toString();
		
		List<String> productsSearchIds = new ArrayList<String>();
		
		String sql3 = sql3p1 + stringfyProducts + ");";
		List<String> newProductsList1 = productsInformation(variables, productsSearchIds, newProductsList0, sql3);
		
		StringBuilder productsSearchId = new StringBuilder();
		
		productsSearchId.append(productsSearchIds.toString().replace("[", "").replace("]", ""));
		productsSearchId.toString();
		
		String sql4 = "select id_farmacia,id_produto,valor_unitario from Produto_Farmacia where id_produto in (" + productsSearchId + ");";
		
		
		StringBuilder payload = getProducts(variables, sql4, newProductsList1);
		
		log.exiting(name, methodName);
		return payload;
	}
	
	private StringBuilder getProductsID(Map <String, String> variables, List<String> productsId, List<String> newProductsList0, String sql2) throws SQLException {
		String methodName = "getProductsID";
		log.entering(name, methodName);
		
		PreparedStatement statement2 = DB.connect(variables).prepareStatement(sql2);
		
		try {
			ResultSet f2 = statement2.executeQuery();
			
			while(f2.next()) {
				productsId.add(f2.getString(1));
				newProductsList0.add("{ id: '" + f2.getString(1) + "', amount: '" + "AMOUNT_" + "', name: '" + "NAME_" + "', pharmacy: 'PHARMACY_NAME', price: 'R$ PRICE_FLOAT', image: '" + "IMAGE_" + "', description: '" + "DESCRIPTION_" + "' }");
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement2.close();
			DB.disconnect();
		}
		
		StringBuilder stringfyProducts = new StringBuilder();
		
		stringfyProducts.append(productsId.toString().replace("[", "").replace("]", ""));
		
		log.exiting(name, methodName);
		return stringfyProducts;
	}
	
	private List<String> productsInformation(Map <String, String> variables, List<String> productsSearchIds, List<String> newProductsList0, String sql3) throws SQLException {
		String methodName = "productsInformation";
		log.entering(name, methodName);
		
		PreparedStatement statement3 = DB.connect(variables).prepareStatement(sql3);
		
		List<String> newProductsList1 = new ArrayList<String>();
		
		try {
			ResultSet f3 = statement3.executeQuery();
			
			while(f3.next()) {
				productsSearchIds.add(f3.getString(1));
				
				for(int i = 0; i < newProductsList0.size(); i++) {
					JSONObject item = new JSONObject(newProductsList0.get(i));
					
					if(item.get("id").equals(f3.getString(1))) {
						String tmp0 = newProductsList0.get(i).replace("AMOUNT_", f3.getString(3));
						String tmp1 = tmp0.replace("NAME_", f3.getString(2));
						String tmp2 = tmp1.replace("IMAGE_", f3.getString(4));
						String tmp3 = tmp2.replace("DESCRIPTION_", f3.getString(7));
						
						newProductsList1.add(tmp3);
					}
				}
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement3.close();
			DB.disconnect();
		}
		
		return newProductsList1;
	}
	
	private StringBuilder getProducts(Map <String, String> variables, String sql4, List<String> newProductsList1) throws SQLException {
		String methodName = "getProducts";
		log.entering(name, methodName);
		
		PreparedStatement statement4 = DB.connect(variables).prepareStatement(sql4);
		
		List<String> pharmaciesIds = new ArrayList<String>();
		List<String> newProductsList2 = new ArrayList<String>();
		
		try {
			ResultSet f4 = statement4.executeQuery();
			
			while(f4.next()) {
				pharmaciesIds.add(f4.getString(1));
				String priceProduct = String.format("%.2f", f4.getFloat(3)).replace(".", ",");
				String pharmacyNumberId = f4.getString(1);
				
				for(int i = 0; i < newProductsList1.size(); i++) {
					JSONObject item = new JSONObject(newProductsList1.get(i));
					
					if(item.get("id").equals(f4.getString(2))) {
						String tmp0 = newProductsList1.get(i);
						String tmp1 = tmp0.replace("PRICE_FLOAT", priceProduct);
						String tmp2 = tmp1.replace("PHARMACY_NAME", "PMY_" + pharmacyNumberId);
						
						if(!newProductsList2.contains(tmp2)) {
							newProductsList2.add(tmp2);
						}
					}
				}
			}
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement4.close();
			DB.disconnect();
		}
		
		StringBuilder stringfyPharmaciesIds = new StringBuilder();
		
		stringfyPharmaciesIds.append(pharmaciesIds.toString().replace("[", "").replace("]", ""));
		stringfyPharmaciesIds.toString();
		
		String sql5 = "select id_farmacia,nome from Farmacia where id_farmacia in (" + stringfyPharmaciesIds + ");";
		
		log.exiting(name, methodName);
		return createProducts(variables, newProductsList2, sql5);
	}
	
	private StringBuilder createProducts(Map <String, String> variables, List<String> newProductsList2, String sql5) throws SQLException {
		PreparedStatement statement5 = DB.connect(variables).prepareStatement(sql5);
		
		List<String> pharmacies = new ArrayList<String>();
		List<String> newProductsList = new ArrayList<String>();
		
		try {
			ResultSet f5 = statement5.executeQuery();
			
			while(f5.next()) {
				pharmacies.add(f5.getString(1) + "-" + f5.getString(2));
				
				for(int i = 0; i < newProductsList2.size(); i++) {
					JSONObject item = new JSONObject(newProductsList2.get(i));
					
					if(item.get("pharmacy").equals("PMY_" + f5.getString(1))) {
						String tmp0 = newProductsList2.get(i);
						String tmp1 = tmp0.replace("PMY_" + f5.getString(1), f5.getString(2));
						
						if(!newProductsList.contains(tmp1)) {
							newProductsList.add(tmp1);
						}
					}
				}
			}
			
			CreatePayload createPayload = new CreatePayload();
			return createPayload.payload(pharmacies, newProductsList);
		}
		catch (SQLException e) {
			throw new SQLException(LogMessage.message(e.toString()));
		}
		finally {
			statement5.close();
			DB.disconnect();
		}
	}
}