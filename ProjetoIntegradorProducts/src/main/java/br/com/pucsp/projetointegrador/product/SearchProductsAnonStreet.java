package br.com.pucsp.projetointegrador.product;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.pucsp.projetointegrador.product.products.ProductsDB;
import br.com.pucsp.projetointegrador.product.utils.GetUserCoordinates;

public class SearchProductsAnonStreet {
	public static String NAME = SearchProductsAnonStreet.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(SearchProductsAnonStreet.class.getName());
	
	public JSONObject getProducts(Map <String, String> variables, String distance, String street, String district, String state, String city, String productName) {
		LOG.entering(NAME, "getProducts");
		ProductsDB productsDB = new ProductsDB();
		
		if(distance.length() < 3) {
			String lat = "";
			String lon = "";
			
			GetUserCoordinates getUserCoordinates = new GetUserCoordinates();
			
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
			
			JSONObject payload = new JSONObject(productsDB.getProducts(variables, sql1, sql3p1).toString());
			
			LOG.exiting(NAME, "getProducts");
			return payload;
		}
		
		LOG.exiting(NAME, "getProducts");
		return null;
	}
}