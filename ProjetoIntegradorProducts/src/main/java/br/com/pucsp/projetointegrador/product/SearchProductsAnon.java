package br.com.pucsp.projetointegrador.product;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.product.products.ProductsDB;

public class SearchProductsAnon {
	public static String NAME = SearchProductsAnon.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(SearchProductsAnon.class.getName());
	
	public JSONObject getProducts(Map <String, String> variables, String distance, String lat, String lon, String productName) {
		LOG.entering(NAME, "getProducts");
		ProductsDB productsDB = new ProductsDB();
		
		if(distance.length() < 3) {
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