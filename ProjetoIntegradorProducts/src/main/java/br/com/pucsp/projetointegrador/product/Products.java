package br.com.pucsp.projetointegrador.product;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.product.products.ProductsDB;

public class Products {
	public static String NAME = Products.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(Products.class.getName());
	
	public JSONObject getProducts(Map <String, String> variables, String distance, String session) {
		LOG.entering(NAME, "getProducts");
		ProductsDB productsDB = new ProductsDB();
		
		int SESSION_LENGTH = Integer.parseInt(variables.get("SESSION_LENGTH"));
		if(distance.length() < 3) {
			if(session.length() == SESSION_LENGTH) {
				JSONObject payload = new JSONObject(productsDB.getProducts(variables, distance, session).toString());
				
				LOG.exiting(NAME, "getProducts");
				return payload;
			}
		}
		
		LOG.exiting(NAME, "getProducts");
		return null;
	}
}