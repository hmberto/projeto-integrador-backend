package br.com.pucsp.projetointegrador.order;

import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.order.products.GetProducts;
import br.com.pucsp.projetointegrador.order.products.ProductsDB;

public class Products {
	public static String NAME = Products.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(Products.class.getName());
	
	public JSONObject getProducts(Map <String, String> variables, GetProducts cart) {
		LOG.entering(NAME, "getProducts");
		ProductsDB productsDB = new ProductsDB();
		
		JSONObject payload = new JSONObject(productsDB.getProducts(variables, cart).toString());
		
		LOG.exiting(NAME, "getProducts");
		return payload;
	}
}