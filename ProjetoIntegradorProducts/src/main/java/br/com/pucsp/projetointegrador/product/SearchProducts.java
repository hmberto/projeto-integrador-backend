package br.com.pucsp.projetointegrador.product;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.product.products.SearchProductsDB;

public class SearchProducts {
	public static String NAME = SearchProducts.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(SearchProducts.class.getName());
	
	public JSONObject getProducts(Map <String, String> variables, String distance, String session, String productName) {
		LOG.entering(NAME, "getProducts");
		SearchProductsDB searchProductsDB = new SearchProductsDB();
		
		int SESSION_LENGTH = Integer.parseInt(variables.get("SESSION_LENGTH"));
		if(distance.length() < 3) {
			if(session.length() == SESSION_LENGTH) {
				JSONObject payload = new JSONObject(searchProductsDB.getProducts(variables, distance, session, productName).toString());
				
				LOG.exiting(NAME, "getProducts");
				return payload;
			}
		}
		
		LOG.exiting(NAME, "getProducts");
		return null;
	}
}