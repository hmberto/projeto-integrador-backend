package br.com.pucsp.projetointegrador.product;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.product.products.ProductsAnonStreetDB;

public class ProductsAnonStreet {
	public static String NAME = ProductsAnonStreet.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(ProductsAnonStreet.class.getName());
	
	public JSONObject getProducts(Map <String, String> variables, String distance, String street, String district, String state, String city) {
		LOG.entering(NAME, "getProducts");
		ProductsAnonStreetDB productsAnonStreetDB = new ProductsAnonStreetDB();
		
		if(distance.length() < 3) {
			JSONObject payload = new JSONObject(productsAnonStreetDB.getProducts(variables, distance, street, district, state, city).toString());
			
			LOG.exiting(NAME, "getProducts");
			return payload;
		}
		return null;
	}
}