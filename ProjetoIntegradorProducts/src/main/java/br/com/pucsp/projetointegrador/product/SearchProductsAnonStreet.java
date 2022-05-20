package br.com.pucsp.projetointegrador.product;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.product.products.SearchProductsAnonStreetDB;
import br.com.pucsp.projetointegrador.product.products.SearchProductsDB;

public class SearchProductsAnonStreet {
	public static String NAME = SearchProductsAnonStreet.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(SearchProductsAnonStreet.class.getName());
	
	public JSONObject getProducts(Map <String, String> variables, String distance, String street, String district, String state, String city, String productName) {
		LOG.entering(NAME, "getProducts");
		SearchProductsAnonStreetDB searchProductsDB = new SearchProductsAnonStreetDB();
		
		if(distance.length() < 3) {
			JSONObject payload = new JSONObject(searchProductsDB.getProducts(variables, distance, street, district, state, city, productName).toString());
			
			LOG.exiting(NAME, "getProducts");
			return payload;
		}
		
		LOG.exiting(NAME, "getProducts");
		return null;
	}
}