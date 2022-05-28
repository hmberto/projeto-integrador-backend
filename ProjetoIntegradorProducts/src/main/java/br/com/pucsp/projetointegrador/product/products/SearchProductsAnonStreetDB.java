package br.com.pucsp.projetointegrador.product.products;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import br.com.pucsp.projetointegrador.product.db.DB;
import br.com.pucsp.projetointegrador.product.utils.GetAllProducts;
import br.com.pucsp.projetointegrador.product.utils.GetUserCoordinates;

public class SearchProductsAnonStreetDB {
	public static String NAME = SearchProductsAnonStreetDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(SearchProductsAnonStreetDB.class.getName());
	
	public StringBuffer getProducts(Map <String, String> variables, String distance, String street, String district, String state, String city, String productName) {
		LOG.entering(NAME, "getProducts");
				
		StringBuffer payload = new StringBuffer();
		
		String lat = "";
		String lon = "";
		
		GetUserCoordinates getUserCoordinates = new GetUserCoordinates();
		
		try {
			String coordinates = getUserCoordinates.coordinates(street, "1");
			JSONArray jsonarray = new JSONArray(coordinates);
			
			for(int i=0; i<jsonarray.length(); i++){
		        JSONObject obj = jsonarray.getJSONObject(i);

		        lat = obj.getString("lat");
		        lon = obj.getString("lon");
			}
			
			String sql1 = "SELECT Farmacia.id_farmacia, (6371 *\n"
					+ "        acos(\n"
					+ "            cos(radians(" + lat + ")) *\n"
					+ "            cos(radians(lat)) *\n"
					+ "            cos(radians(" + lon + ") - radians(lon)) +\n"
					+ "            sin(radians(" + lat + ")) *\n"
					+ "            sin(radians(lat))\n"
					+ "        )) AS distance\n"
					+ "FROM Endereco, Farmacia WHERE Endereco.id_endereco = Farmacia.id_endereco HAVING distance <= " + distance + ";";
			
			String sql3p1 = "SELECT * FROM produto WHERE nome REGEXP '" + productName + "' AND id_produto in (";
			
			GetAllProducts getAllProducts = new GetAllProducts();
			payload = getAllProducts.products(variables, sql1, sql3p1);
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