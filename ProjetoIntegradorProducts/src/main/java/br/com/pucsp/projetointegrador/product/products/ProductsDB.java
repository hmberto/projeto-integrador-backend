package br.com.pucsp.projetointegrador.product.products;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import br.com.pucsp.projetointegrador.product.db.DB;
import br.com.pucsp.projetointegrador.product.db.GetFromDB;

public class ProductsDB {
	public static String NAME = ProductsDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(ProductsDB.class.getName());
	
	public StringBuffer getProducts(Map <String, String> variables, String distance, String session) {
		LOG.entering(NAME, "getProducts");
		
		GetFromDB getFromDB = new GetFromDB();
		
		StringBuffer payload = new StringBuffer();
		JSONWriter createPayload = new JSONWriter(payload);
		
		try {
			String sqlA = "SELECT * FROM Login_Sessao WHERE (id_sessao LIKE ?);";
			PreparedStatement statementA = DB.connect(variables).prepareStatement(sqlA);
			statementA.setString(1, session);
			
			Map<String, String> getLoginSession = getFromDB.getFromDB(variables, statementA);
			statementA.close();
			
			String sqlB = "SELECT * FROM Usuario WHERE (id_usuario LIKE ?);";
			PreparedStatement statementB = DB.connect(variables).prepareStatement(sqlB);
			statementB.setString(1, getLoginSession.get("id_usuario"));
			
			Map<String, String> getUser = getFromDB.getFromDB(variables, statementB);
			statementB.close();
			
			String sqlC = "SELECT lat, lon FROM Endereco WHERE (id_endereco LIKE ?);";
			PreparedStatement statementC = DB.connect(variables).prepareStatement(sqlC);
			statementC.setString(1, getUser.get("id_endereco"));
			
			Map<String, String> getAddress = getFromDB.getFromDB(variables, statementC);
			statementC.close();
			
			String sql1 = "SELECT Farmacia.id_farmacia, (6371 *\n"
					+ "        acos(\n"
					+ "            cos(radians(" + getAddress.get("lat") + ")) *\n"
					+ "            cos(radians(lat)) *\n"
					+ "            cos(radians(" + getAddress.get("lon") + ") - radians(lon)) +\n"
					+ "            sin(radians(" + getAddress.get("lat") + ")) *\n"
					+ "            sin(radians(lat))\n"
					+ "        )) AS distance\n"
					+ "FROM Endereco, Farmacia WHERE Endereco.id_endereco = Farmacia.id_endereco HAVING distance <= " + distance + ";";
			
			PreparedStatement statement = DB.connect(variables).prepareStatement(sql1);
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
			
			String sql3 = "SELECT * FROM produto WHERE id_produto in (" + stringfyProducts + ");";
			
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
//				price.put(f4.getInt(2), priceProduct);
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
				String idProduct2 = ProductsPharmacies.get(f5.getInt(1)) + "";
				
				String tmp = finalProducts.get(idProduct);
				String tmpProduct = tmp.replace("PHARMACY_NAME", f5.getString(2));
				
				products.add(tmpProduct);
				pharmacies.add(f5.getString(2));
				
				for(int i = 0; i < newProductsList2.size(); i++) {
					JSONObject item = new JSONObject(newProductsList2.get(i));
					
					System.out.println("\n\n\n");
					System.out.println(item.get("pharmacy"));
					System.out.println("\n\n\n");
					System.out.println(f5.getString(1));
					System.out.println("\n\n\n");
					
					if(item.get("pharmacy").equals("PHARMACY_" + f5.getString(1))) {
						String tmp0 = newProductsList2.get(i);
						String tmp1 = tmp0.replace("PHARMACY_" + f5.getString(1), f5.getString(2));
						System.out.println(tmp1);
						
						if(newProductsList.contains(tmp1)) {}
						else {
							newProductsList.add(tmp1);
						}
					}
				}
			}
			
			System.out.println("\n\n\n");
			System.out.println(newProductsList0.size());
			System.out.println("\n\n\n");
			System.out.println(newProductsList1.size());
			System.out.println("\n\n\n");
			System.out.println(newProductsList2);
			System.out.println("\n\n\n");
			System.out.println(newProductsList);
			System.out.println("\n\n\n");
			
			createPayload.object();
			
//			int i = 0;
//			while(f.next()) {
			for(int a = 0; a < pharmacies.size(); a ++) {
				String pharmacyName = pharmacies.get(a);
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
						createPayload.key("price").value(item.get("price"));
						createPayload.key("image").value(item.get("image"));
						createPayload.key("description").value(item.get("description"));
						
						createPayload.endObject();
					}
				}
				
				createPayload.endObject();
				
//				i++;
			}
			
			createPayload.endObject();
			
//			statement.close();
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "Data not geted from the database: " + e);
		}
		finally {
			DB.disconnect();
		}
		
		// LOG.exiting(NAME, "GetCar");
		LOG.log(Level.INFO, "Nearby products payload: " + payload);
		LOG.exiting(NAME, "getProducts");
		return payload;
	}
}