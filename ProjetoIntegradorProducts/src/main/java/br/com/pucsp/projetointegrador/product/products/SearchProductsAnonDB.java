package br.com.pucsp.projetointegrador.product.products;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.product.db.DB;
import br.com.pucsp.projetointegrador.product.utils.GetAllProducts;

public class SearchProductsAnonDB {
	public static String NAME = SearchProductsAnonDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(SearchProductsAnonDB.class.getName());
	
	public StringBuffer getProducts(Map <String, String> variables, String distance, String lat, String lon, String productName) {
		LOG.entering(NAME, "getProducts");
				
		StringBuffer payload = new StringBuffer();
		
		try {
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