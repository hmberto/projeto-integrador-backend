package br.com.pucsp.projetointegrador.product.products;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.pucsp.projetointegrador.product.db.DB;
import br.com.pucsp.projetointegrador.product.utils.GetAllProducts;

public class ProductsDB {
	public static String NAME = ProductsDB.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(ProductsDB.class.getName());
	
	public StringBuffer getProducts(Map <String, String> variables, String sql1, String sql3p1) {
		LOG.entering(NAME, "getProducts");
		
		StringBuffer payload = new StringBuffer();
		
		try {
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