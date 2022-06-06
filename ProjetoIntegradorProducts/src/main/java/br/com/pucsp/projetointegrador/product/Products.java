package br.com.pucsp.projetointegrador.product;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.pucsp.projetointegrador.product.products.ProductsDB;
import br.com.pucsp.projetointegrador.product.utils.GetUserCoordinates;

public class Products {
	public static String NAME = Products.class.getSimpleName();
	private static Logger LOG = Logger.getLogger(Products.class.getName());
	
	public JSONObject getProducts(Map <String, String> variables, String distance, String session) throws SQLException {
		LOG.entering(NAME, "getProducts");
		ProductsDB productsDB = new ProductsDB();
		
		int SESSION_LENGTH = Integer.parseInt(variables.get("SESSION_LENGTH"));
		if(distance.length() < 3) {
			if(session.length() == SESSION_LENGTH) {
				GetUserCoordinates getUserCoordinates = new GetUserCoordinates();
				Map<String, String> getAddress = getUserCoordinates.coordinatesFromDB(variables, session);
				
				String sql1 = "SELECT Farmacia.id_farmacia, (6371 *\n"
						+ "        acos(\n"
						+ "            cos(radians(" + getAddress.get("lat") + ")) *\n"
						+ "            cos(radians(lat)) *\n"
						+ "            cos(radians(" + getAddress.get("lon") + ") - radians(lon)) +\n"
						+ "            sin(radians(" + getAddress.get("lat") + ")) *\n"
						+ "            sin(radians(lat))\n"
						+ "        )) AS distance\n"
						+ "FROM Endereco, Farmacia WHERE Endereco.id_endereco = Farmacia.id_endereco HAVING distance <= " + distance + ";";
				
				String sql3p1 = "SELECT * FROM produto WHERE id_produto in (";
				
				JSONObject payload = new JSONObject(productsDB.getProducts(variables, sql1, sql3p1).toString());
				
				LOG.exiting(NAME, "getProducts");
				return payload;
			}
		}
		
		LOG.exiting(NAME, "getProducts");
		return null;
	}
}