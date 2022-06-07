package br.com.pucsp.projetointegrador.order;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.order.products.GetProducts;
import br.com.pucsp.projetointegrador.order.products.ProductsDB;

public class Products {
	private static String name = Products.class.getSimpleName();
	private static Logger log = Logger.getLogger(Products.class.getName());
	
	public JSONObject getProducts(Map <String, String> variables, GetProducts cart) throws SQLException {
		log.entering(name, "getProducts");
		ProductsDB productsDB = new ProductsDB();
		
		JSONObject payload = new JSONObject(productsDB.getProducts(variables, cart).toString());
		
		log.exiting(name, "getProducts");
		return payload;
	}
}