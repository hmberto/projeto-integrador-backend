package br.com.pucsp.projetointegrador.product;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.product.products.ProductsAnonDB;

public class ProductsAnon {
	public static String NAME = ProductsAnon.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(ProductsAnon.class.getName());
	
	public JSONObject getProducts(Map <String, String> variables, String distance, String lat, String lon) {
		LOG.entering(NAME, "getProducts");
		ProductsAnonDB productsAnonDB = new ProductsAnonDB();
		
		if(distance.length() < 3) {
			JSONObject payload = new JSONObject(productsAnonDB.getProducts(variables, distance, lat, lon).toString());
			
			LOG.exiting(NAME, "getProducts");
			return payload;
		}
		return null;
	}
}