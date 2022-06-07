package br.com.pucsp.projetointegrador.product;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.product.products.ProductsDB;

public class Products {
	private static String name = Products.class.getSimpleName();
	private static Logger log = Logger.getLogger(Products.class.getName());
	
	public JSONObject getProducts(Map <String, String> variables, String sql, String sql3p1) throws SQLException {
		String methodName = "getProducts";
		log.entering(name, methodName);
		
		ProductsDB getAllProducts = new ProductsDB();
		JSONObject payload = new JSONObject(getAllProducts.products(variables, sql, sql3p1).toString());
		
		log.exiting(name, methodName);
		return payload;
	}
}