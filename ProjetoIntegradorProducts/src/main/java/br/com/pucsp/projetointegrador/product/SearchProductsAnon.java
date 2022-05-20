package br.com.pucsp.projetointegrador.product;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.product.products.SearchProductsAnonDB;
import br.com.pucsp.projetointegrador.product.products.SearchProductsDB;

public class SearchProductsAnon {
	public static String NAME = SearchProductsAnon.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(SearchProductsAnon.class.getName());
	
	public JSONObject getProducts(Map <String, String> variables, String distance, String lat, String lon, String productName) {
		LOG.entering(NAME, "getProducts");
		SearchProductsAnonDB searchProductsDB = new SearchProductsAnonDB();
		
		if(distance.length() < 3) {
			JSONObject payload = new JSONObject(searchProductsDB.getProducts(variables, distance, lat, lon, productName).toString());
			
			LOG.exiting(NAME, "getProducts");
			return payload;
		}
		
		LOG.exiting(NAME, "getProducts");
		return null;
	}
}